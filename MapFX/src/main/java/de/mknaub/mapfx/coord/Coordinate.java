package de.knaubmaxim.mapfx.coord;

import static de.knaubmaxim.mapfx.coord.CoordinateConverter.LAENGEN_BREITEN_GRAD.BREITENGRAD;
import static de.knaubmaxim.mapfx.coord.CoordinateConverter.LAENGEN_BREITEN_GRAD.LAENGENGRAD;
import static de.knaubmaxim.mapfx.coord.CoordinateConverter.WGS84ToDez;
import static de.knaubmaxim.mapfx.coord.CoordinateConverter.dez2grad;
import static de.knaubmaxim.mapfx.coord.CoordinateUtils.EARTH_RADIUS;
import static java.lang.Math.PI;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import java.util.Objects;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 *
 * @author maka
 */
public class Coordinate {

    private final DoubleProperty longitude = new SimpleDoubleProperty();
    private final DoubleProperty latitude = new SimpleDoubleProperty();

    public Coordinate() {
        longitude.setValue(0);
        latitude.setValue(0);
    }

    public Coordinate(double longitude, double latitude) {
        this.longitude.setValue(longitude);
        this.latitude.setValue(latitude);
    }

    public Coordinate(String lonWGS84, String latWGS84) {
        this.longitude.setValue(WGS84ToDez(lonWGS84));
        this.latitude.setValue(WGS84ToDez(latWGS84));
    }

    public DoubleProperty longitudeProperty() {
        return longitude;
    }

    public Double getLongitude() {
        return longitude.getValue();
    }

    public void setLongitude(double longitude) {
        this.longitude.setValue(longitude);
    }

    public String getWGS84Longitude() {
        return dez2grad(getLongitude(), LAENGENGRAD, false);
    }

    public void setWGS84Longitude(String value) {
        setLongitude(WGS84ToDez(value));
    }

    public DoubleProperty latitudeProperty() {
        return latitude;
    }

    public Double getLatitude() {
        return latitude.getValue();
    }

    public void setLatitude(double latitude) {
        this.latitude.setValue(latitude);
    }

    public String getWGS84Latitude() {
        return dez2grad(getLatitude(), BREITENGRAD, false);
    }

    public void setWGS84Latitude(String value) {
        setLatitude(WGS84ToDez(value));
    }

    public double bearingTo(Coordinate coordinate) {
        return CoordinateUtils.bearingTo(this, coordinate);
    }

    /**
     *
     * @param angle
     * @param distance in meter
     * @return
     */
    public Coordinate destinationPoint(double angle, double distance) {
        distance /= EARTH_RADIUS;
        angle = geoRadians(angle);  //
        double lon1 = geoRadians(this.longitude.getValue());
        double lat1 = geoRadians(this.latitude.getValue());
        double lat2 = asin(sin(lat1) * cos(distance) + cos(lat1) * sin(distance) * cos(angle));
        double lon2 = lon1 + atan2(sin(angle) * sin(distance) * cos(lat1), cos(distance) - sin(lat1) * sin(lat2));
        lon2 = (lon2 + 3 * PI) % (2 * PI) - PI;  // normalise to -180..+180º
        return new Coordinate(geoDegrees(lon2), geoDegrees(lat2));
    }

    /* degrees to radians */
    public static double geoRadians(double deg) {
        return deg * (PI / 180.0);
    }

    /* radians to degrees */

    public static double geoDegrees(double rad) {
        return rad * (180.0 / PI);
    }

    /**
     *
     * @param coordinate
     * @return distance in meter
     */
    public double distanceTo(Coordinate coordinate) {
        double R = EARTH_RADIUS;
        double lat1 = geoRadians(this.latitude.getValue()), lon1 = geoRadians(this.longitude.getValue());
        double lat2 = geoRadians(coordinate.latitude.getValue()), lon2 = geoRadians(coordinate.longitude.getValue());
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = sin(dLat / 2) * sin(dLat / 2)
                + cos(lat1) * cos(lat2)
                * sin(dLon / 2) * sin(dLon / 2);
        double c = 2 * atan2(sqrt(a), sqrt(1 - a));
        double d = R * c;
        return d;
    }

    /**
     * TODO information loss: new Coordinate("011E5914", "51N0728");
     * toGradString();
     *
     * @return
     */
    public String toGradString() {
        return dez2grad(longitude.getValue(), LAENGENGRAD, true)
                + dez2grad(latitude.getValue(), BREITENGRAD, true);
    }

    @Override public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.longitude);
        hash = 43 * hash + Objects.hashCode(this.latitude);
        return hash;
    }

    @Override public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Coordinate other = (Coordinate) obj;
        if (!Objects.equals(this.longitude, other.longitude)) {
            return false;
        }
        return Objects.equals(this.latitude, other.latitude);
    }

    @Override public String toString() {
        return "Coordinate{" + "longitude=" + longitude.getValue() + ", latitude=" + latitude.getValue() + '}';
    }
}
