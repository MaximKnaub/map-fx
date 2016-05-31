package de.mknaub.mapfx.coord;

import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

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

    private final DoubleProperty top = new SimpleDoubleProperty();
    private final DoubleProperty right = new SimpleDoubleProperty();
    private final DoubleProperty bottom = new SimpleDoubleProperty();
    private final DoubleProperty left = new SimpleDoubleProperty();

    public Bound() {
    }

    public Bound(double top, double right, double bottom, double left) {
        this.top.setValue(top);
        this.right.setValue(right);
        this.bottom.setValue(bottom);
        this.left.setValue(left);
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
        this.left.setValue(coordinate.destinationPoint(270, radius).getLongitude());
        this.right.setValue(coordinate.destinationPoint(90, radius).getLongitude());
        this.bottom.setValue(coordinate.destinationPoint(180, radius).getLatitude());
        this.top.setValue(coordinate.destinationPoint(0, radius).getLatitude());
    }

    public Bound(List<Coordinate> coordList) {
        if (coordList.isEmpty() == false) {
            this.top.setValue(coordList.stream().mapToDouble(Coordinate::getLatitude).max().getAsDouble());
            this.right.setValue(coordList.stream().mapToDouble(Coordinate::getLongitude).max().getAsDouble());
            this.bottom.setValue(coordList.stream().mapToDouble(Coordinate::getLatitude).min().getAsDouble());
            this.left.setValue(coordList.stream().mapToDouble(Coordinate::getLongitude).min().getAsDouble());
        }
    }

    public void extend(Bound bound) {
        extend(bound.left.getValue(), bound.top.getValue());
        extend(bound.right.getValue(), bound.bottom.getValue());
    }

    public void extend(Coordinate coord) {
        extend(coord.getLongitude(), coord.getLatitude());
    }

    public void extend(double longitude, double latitude) {
        right.setValue(longitude > right.getValue() ? longitude : right.getValue());
        left.setValue(longitude < left.getValue() ? longitude : left.getValue());
        top.setValue(latitude > top.getValue() ? latitude : top.getValue());
        bottom.setValue(latitude < bottom.getValue() ? latitude : bottom.getValue());
    }

    public boolean isInside(Coordinate coord) {
        return isInside(coord.getLongitude(), coord.getLatitude());
    }

    public boolean isInside(double longitute, double latitude) {
        if (top.getValue() < latitude) {
            return false;
        } else if (bottom.getValue() > latitude) {
            return false;
        } else if (left.getValue() > longitute) {
            return false;
        } else {
            return right.getValue() >= longitute;
        }
    }

    public boolean isInside(Bound bound) {
        return isInside(bound.left.getValue(), bound.top.getValue())
                || isInside(bound.right.getValue(), bound.top.getValue())
                || isInside(bound.right.getValue(), bound.bottom.getValue())
                || isInside(bound.left.getValue(), bound.bottom.getValue());
    }

    public boolean intersects(Bound bound) {
        boolean comp0 = this.left.getValue() <= bound.right.getValue();
        boolean comp1 = this.right.getValue() >= bound.left.getValue();
        boolean comp2 = this.top.getValue() >= bound.bottom.getValue();
        boolean comp3 = this.bottom.getValue() <= bound.top.getValue();
        return comp0 && comp1 && comp2 && comp3;
    }

    public DoubleProperty topProperty() {
        return top;
    }

    public Double getTop() {
        return top.getValue();
    }

    public void setTop(Double top) {
        this.top.setValue(top);
    }

    public DoubleProperty rightProperty() {
        return right;
    }

    public Double getRight() {
        return right.getValue();
    }

    public void setRight(Double right) {
        this.right.setValue(right);
    }

    public DoubleProperty bottomProperty() {
        return bottom;
    }

    public Double getBottom() {
        return bottom.getValue();
    }

    public void setBottom(Double bottom) {
        this.bottom.setValue(bottom);
    }

    public DoubleProperty leftProperty() {
        return left;
    }

    public Double getLeft() {
        return left.getValue();
    }

    public void setLeft(Double left) {
        this.left.setValue(left);
    }

    @Override
    public String toString() {
        return "Bound{" + "top=" + top.getValue() + ", right=" + right.getValue() + ", bottom=" + bottom.getValue() + ", left=" + left.getValue() + '}';
    }
}
