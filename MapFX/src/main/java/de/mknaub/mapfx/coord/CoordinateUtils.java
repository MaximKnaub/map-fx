package de.mknaub.mapfx.coord;

import de.mknaub.mapfx.Viewport;
import static de.mknaub.mapfx.coord.CoordinateUtils.LAENGEN_BREITEN_GRAD.BREITENGRAD;
import static de.mknaub.mapfx.coord.CoordinateUtils.LAENGEN_BREITEN_GRAD.LAENGENGRAD;
import static java.lang.Double.parseDouble;
import static java.lang.Math.PI;
import static java.lang.Math.asin;
import static java.lang.Math.atan;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.floor;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import static java.lang.Math.sin;
import static java.lang.Math.sinh;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;
import static java.lang.Math.toDegrees;
import java.text.DecimalFormat;
import javafx.geometry.Point2D;

/**
 *
 * @author maka
 */
public class CoordinateUtils {

    public static final long EARTH_RADIUS = 6_378_137;

    /**
     * degrees to radians
     *
     * @param deg
     * @return
     */
    public static double geoRadians(double deg) {
        return deg * (PI / 180.0);
    }

    /**
     * radians to degrees
     *
     * @param rad
     * @return
     */
    public static double geoDegrees(double rad) {
        return rad * (180.0 / PI);
    }

    public static double bearingTo(Coordinate p1, Coordinate p2) {
        double lat1 = geoRadians(p1.getLatitude()), lat2 = geoRadians(p2.getLatitude());
        double dLon = geoRadians(p2.getLongitude() - p1.getLongitude());

        double y = sin(dLon) * cos(lat2);
        double x = cos(lat1) * sin(lat2)
                - sin(lat1) * cos(lat2) * cos(dLon);
        double brng = atan2(y, x);

        return (geoDegrees(brng) + 360) % 360;
    }

    /**
     *
     * @param geoKoord
     * @param angle
     * @param distance in meter
     * @return
     */
    public static Coordinate destinationPoint(Coordinate geoKoord, double angle, double distance) {
        distance /= EARTH_RADIUS;
        angle = geoRadians(angle);  //
        double lon1 = geoRadians(geoKoord.getLongitude());
        double lat1 = geoRadians(geoKoord.getLatitude());
        double lat2 = asin(sin(lat1) * cos(distance) + cos(lat1) * sin(distance) * cos(angle));
        double lon2 = lon1 + atan2(sin(angle) * sin(distance) * cos(lat1), cos(distance) - sin(lat1) * sin(lat2));
        lon2 = (lon2 + 3 * PI) % (2 * PI) - PI;  // normalise to -180..+180º
        return new Coordinate(geoDegrees(lon2), geoDegrees(lat2));
    }

    /**
     * converts coordinate to pixel
     *
     * @param viewport
     * @param coordinate
     * @return
     */
    public static Point2D coord2Point(Viewport viewport, Coordinate coordinate) {
        return coord2Point(viewport, coordinate.getLongitude(), coordinate.getLatitude());
    }

    /**
     * converts coordinate to pixel
     *
     * @param viewport
     * @param lon
     * @param lat
     * @return
     */
    public static Point2D coord2Point(Viewport viewport, double lon, double lat) {
        double n = pow(2, viewport.getZoom());
        double latRad = PI * lat / 180;
        double xD = n / 360.0 * (180.0 + lon);
        double yD = n * (1.0 - (log(tan(latRad) + 1 / cos(latRad)) / PI)) / 2;
        double mex = xD * 256.0;
        double mey = yD * 256.0;
        double x = viewport.getExtent().getMinX() + mex;
        double y = viewport.getExtent().getMinY() + mey;
        return new Point2D(x, y);
    }

    public static Point2D coord2Point(double z, double lon, double lat) {
        double n = pow(2, z);
        double latRad = PI * lat / 180;
        double xD = n / 360.0 * (180.0 + lon);
        double yD = n * (1.0 - (log(tan(latRad) + 1 / cos(latRad)) / PI)) / 2;
        double x = xD * 256.0;
        double y = yD * 256.0;
        return new Point2D(x, y);
    }

    /**
     * converts pixel to coordinate
     *
     * @param viewport
     * @param point
     * @return
     */
    public static Coordinate point2Coord(Viewport viewport, Point2D point) {
        return point2Coord(viewport, point.getX(), point.getY());
    }

    /**
     * converts pixel to coordinate
     *
     * @param viewport
     * @param x
     * @param y
     * @return
     */
    public static Coordinate point2Coord(Viewport viewport, double x, double y) {
        double z = viewport.getZoom();
        x = x - viewport.getExtent().getMinX();
        y = y - viewport.getExtent().getMinY();
        return point2Coord(z, x, y);
    }

    /**
     *
     * @param z
     * @param x
     * @param y
     * @return
     */
    public static Coordinate point2Coord(double z, double x, double y) {
        double latrad = PI - (2.0 * PI * y) / (pow(2, z) * 256.);
        double lat = toDegrees(atan(sinh(latrad)));
        double lon = x / (256 * pow(2, z)) * 360 - 180;
        return new Coordinate(lon, lat);
    }

    /**
     * Umwandlung von gradieller nach dezimaler Darstellung. Es muss
     * beruecksichtigt werden, dass bei der Umwandlung von geographischen
     * Koordinaten - insbesondere die Laenge - das spezielle Format
     * beruecksichtigt wird --> 0xxExxxx. Die fuehrende Null muss entfernt
     * werden. 03.11.2004, me11
     *
     * @param value
     * @return
     */
    public static double WGS84ToDez(String value) {
        String modifiedValue = value;

        // Null bzw. "" ist nicht gestattet. Des Weiteren ebenfalls keine negativen
        // Eingabe (07.11.2004, me11)
        if (value == null || value.length() == 0 || value.indexOf('-', 0) != -1) {
            return 0;
        }

        int index = value.indexOf('E');

        if (index == -1) {
            index = value.indexOf('N');
        }

        // Geographischen Laengengrad umwandeln
        if (index != -1) {
            modifiedValue = value.substring(value.substring(index, index + 1).equals("E") ? 1 : 0, index).trim();	// Vorkommaanteil

            String fractions = value.substring(index + 1).trim();

            while (fractions.length() < 4) {
                fractions += "0";
            }
            modifiedValue += fractions;	// Minuten/Sekunden
        }

        while (modifiedValue.trim().length() < 6) {
            modifiedValue = "0" + modifiedValue;
        }

        // seda 18.08.2011 - Rundung auf 4 Nachkommastellen
        DecimalFormat df = new DecimalFormat("#0.0000");

        return parseDouble(df.format(((Double.valueOf(modifiedValue.substring(4, 6)) / 60)
                + new Double(modifiedValue.substring(2, 4))) / 60
                + new Double(modifiedValue.substring(0, 2))).replace(",", "."));
        //    double res = ((new Double(modifiedValue.substring(4, 6)).doubleValue()/60)
        //            + new Double(modifiedValue.substring(2, 4)).doubleValue())/60
        //            + new Double(modifiedValue.substring(0, 2)).doubleValue();
    }

    /**
     *
     * @param value
     * @param laengen_breiten_grad
     * @param round
     * @return
     */
    public static String dez2grad(double value, LAENGEN_BREITEN_GRAD laengen_breiten_grad, boolean round) {
        String result;
        String minuten;
        String sekunden;

        String laenge_breite = "";

        // seda
        if (laengen_breiten_grad == LAENGENGRAD) {
            if (value >= 0) {
                laenge_breite = "E";
            } else {
                laenge_breite = "W";
            }
        } else if (laengen_breiten_grad == BREITENGRAD) {
            if (value >= 0) {
                laenge_breite = "N";
            } else {
                laenge_breite = "S";
            }
        }

        // Grad
        double grad = floor(value);

        value -= grad;

        // Minuten
        value *= 60;
        double min = floor(value);
        value -= min;

        // Sekunden
        value *= 60;

        int seconds;

        if (round) { // kaufmännische Rundung, das eigentlich korrekte Vorgehen
            seconds = (int) round(value);
            if (seconds == 60) { //Bei 60sec Umrechnung in Minuten
                seconds = 0;
                min = min + 1;
                if (min == 60) { //Bei 60min Umrechnung in Grad
                    grad = grad + 1;
                }
            }//seconds = 59;
        } else { // Abrundung aufgrund Kompatibilität zu FAST
            seconds = (int) floor(value);
        }

        result = "" + (int) grad;
        // fuer Bessel, ohne E/N
        int addiereEineNull = 0;
        if (laenge_breite.equalsIgnoreCase("E")) {
            addiereEineNull = 1;
        }

        while (result.trim().length() < 2 + addiereEineNull) {
            result = "0" + result;
        }

        result += laenge_breite;

        minuten = "" + (int) min;
        while (minuten.trim().length() < 2) {
            minuten = "0" + minuten;
        }
        result += minuten;

        while (minuten.trim().length() < 2) {
            minuten = "0" + minuten;
        }

        sekunden = "" + seconds;
        while (sekunden.trim().length() < 2) {
            sekunden = "0" + sekunden;
        }
        result += sekunden;

        return result.replace('-', ' ').trim();
    }

    /**
     *
     */
    public enum LAENGEN_BREITEN_GRAD {

        LAENGENGRAD,
        BREITENGRAD;
    }
    //////////////// neues //////////////////////

    /**
     * was ist das? kann glaube ich gelöscht werden!
     *
     * @param deziGrad
     * @param minDeziGrad
     * @param maxDeziGrad
     * @param imgSize
     * @return
     */
    @Deprecated
    public static int convertToPixelFromDeziGrad(double deziGrad, double minDeziGrad, double maxDeziGrad, int imgSize) {
        return (int) ((deziGrad - minDeziGrad) / (maxDeziGrad - minDeziGrad) * imgSize);
    }

    /**
     *
     * @author saka
     *
     * @param coord1
     * @param coord2
     * @return
     */
    public static double getDistanceOfKoords(Coordinate coord1, Coordinate coord2) {
        double dx = (70d * (coord1.getLongitude() - coord2.getLongitude()));
        double dy = (105d * (coord1.getLatitude() - coord2.getLatitude()));

        double distance = sqrt(dx * dx + dy * dy);

        return parseDouble(new DecimalFormat("#.0000").format(distance).replace(',', '.'));
    }

    /**
     * Prüft, ob eine Koordinate innerhalb des Berechnungsradius um die Antenne
     * ist.
     *
     * @param koord1 Die Koordinate der Antenne.
     * @param koord2 Die Koordinate, die geprüft werden soll.
     * @param radius radius in meter. Radius um die Antenne, innerhalb dem
     * gerechnet wird.
     * @return true, falls Distanz zwischen koord1 und koord <= radius, sonst
     * false.
     */
    @Deprecated
    public static boolean isKoordInCalcRadius(Coordinate koord1, Coordinate koord2, double radius) {
        return getDistanceOfKoords(koord1, koord2) <= radius;
    }
}
