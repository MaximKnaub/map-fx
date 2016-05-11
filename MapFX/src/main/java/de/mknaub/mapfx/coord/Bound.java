package de.mknaub.mapfx.coord;

import java.util.List;

/**
 *
 * Diese Klasse berechnet den Bereich um die Antenne, der berechnet werden soll.
 * <br>
 * Hierzu werden Grenzen für Min/Max Lng/Lat definiert, die in Abhängigkeit vom
 * Standort der Antenne gesetzt werden.
 *
 * @author maka, seda
 * @date 29.04.2013
 */
public class Bound {

    private Double top;
    private Double right;
    private Double bottom;
    private Double left;

    public Bound() {
    }

    public Bound(double top, double right, double bottom, double left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public Bound(double longitude, double latitude) {
        this(latitude, longitude, latitude, longitude);
    }

    public Bound(Coordinate coord) {
        this(coord.getLongitude(), coord.getLatitude());
    }

    /**
     * Mit hilfe der Methode "destinationPoint()" werden hier die min/max
     * Lon/Lat koordinaten ermittelt. Diese Koordinaten werden zum Berechenen
     * und zum auflösen der Ergebnisse benötigt.
     *
     * p2 --------------- | | | (Zentrum) | | | --------------- p1
     *
     * p1 = <minLngBound, minLatBound>
     * p2 = <maxLngBound, maxLatBound>
     * Zentrum = antrag.getGeoCoord();
     *
     * @param coordinate
     * @param radius
     */
    public Bound(Coordinate coordinate, double radius) {
        this.left = coordinate.destinationPoint(270, radius).getLongitude();
        this.right = coordinate.destinationPoint(90, radius).getLongitude();
        this.bottom = coordinate.destinationPoint(180, radius).getLatitude();
        this.top = coordinate.destinationPoint(0, radius).getLatitude();
    }

    public Bound(List<Coordinate> coordList) {
        if (coordList.isEmpty() == false) {
            this.top = coordList.stream().mapToDouble(Coordinate::getLatitude).max().getAsDouble();
            this.right = coordList.stream().mapToDouble(Coordinate::getLongitude).max().getAsDouble();
            this.bottom = coordList.stream().mapToDouble(Coordinate::getLatitude).min().getAsDouble();
            this.left = coordList.stream().mapToDouble(Coordinate::getLongitude).min().getAsDouble();
        }
    }

    public void extend(Bound bound) {
        extend(bound.left, bound.top);
        extend(bound.right, bound.bottom);
    }

    public void extend(Coordinate coord) {
        extend(coord.getLongitude(), coord.getLatitude());
    }

    public void extend(double longitude, double latitude) {
        if (right != null) {
            right = longitude > right ? longitude : right;
        } else {
            right = longitude;
        }
        if (left != null) {
            left = longitude < left ? longitude : left;
        } else {
            left = longitude;
        }
        if (top != null) {
            top = latitude > top ? latitude : top;
        } else {
            top = latitude;
        }
        if (bottom != null) {
            bottom = latitude < bottom ? latitude : bottom;
        } else {
            bottom = latitude;
        }
    }

    public boolean isInside(Coordinate coord) {
        return isInside(coord.getLongitude(), coord.getLatitude());
    }

    public boolean isInside(double longitute, double latitude) {
        if (top < latitude) {
            return false;
        } else if (bottom > latitude) {
            return false;
        } else if (left > longitute) {
            return false;
        } else {
            return right >= longitute;
        }
    }

    public boolean isInside(Bound bound) {
        return isInside(bound.left, bound.top)
                || isInside(bound.right, bound.top)
                || isInside(bound.right, bound.bottom)
                || isInside(bound.left, bound.bottom);
    }

    public boolean intersects(Bound bound) {
        boolean comp0 = this.left <= bound.right;
        boolean comp1 = this.right >= bound.left;
        boolean comp2 = this.top >= bound.bottom;
        boolean comp3 = this.bottom <= bound.top;
        return comp0 && comp1 && comp2 && comp3;
    }

    public Double getTop() {
        return top;
    }

    public void setTop(Double top) {
        this.top = top;
    }

    public Double getRight() {
        return right;
    }

    public void setRight(Double right) {
        this.right = right;
    }

    public Double getBottom() {
        return bottom;
    }

    public void setBottom(Double bottom) {
        this.bottom = bottom;
    }

    public Double getLeft() {
        return left;
    }

    public void setLeft(Double left) {
        this.left = left;
    }

    @Override
    public String toString() {
        return "Bound{" + "top=" + top + ", right=" + right + ", bottom=" + bottom + ", left=" + left + '}';
    }
}
