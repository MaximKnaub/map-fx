package de.mknaub.mapfx;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 *
 * @author MaKa
 */
public class Extent {

    private final DoubleProperty minX = new SimpleDoubleProperty(0);
    private final DoubleProperty minY = new SimpleDoubleProperty(0);
    private final DoubleProperty maxX = new SimpleDoubleProperty(0);
    private final DoubleProperty maxY = new SimpleDoubleProperty(0);

    /**
     *
     */
    public Extent() {
    }

    public Extent(double minX, double minY, double maxX, double maxY) {
        this.minX.setValue(minX);
        this.minY.setValue(minY);
        this.maxX.setValue(maxX);
        this.maxY.setValue(maxY);
    }

    public void setAll(double minX, double minY, double maxX, double maxY) {
        setMinX(minX);
        setMinY(minY);
        setMaxX(maxX);
        setMaxY(maxY);
    }

    /**
     *
     * @return
     */
    public double getMinX() {
        return minX.get();
    }

    /**
     *
     * @param minX
     */
    public void setMinX(double minX) {
        this.minX.set(minX);
    }

    /**
     *
     * @return
     */
    public DoubleProperty minXProperty() {
        return minX;
    }

    /**
     *
     * @return
     */
    public double getMinY() {
        return minY.get();
    }

    /**
     *
     * @param minY
     */
    public void setMinY(double minY) {
        this.minY.set(minY);
    }

    /**
     *
     * @return
     */
    public DoubleProperty minYProperty() {
        return minY;
    }

    /**
     *
     * @return
     */
    public double getMaxX() {
        return maxX.get();
    }

    /**
     *
     * @param maxX
     */
    public void setMaxX(double maxX) {
        this.maxX.set(maxX);
    }

    /**
     *
     * @return
     */
    public DoubleProperty maxXProperty() {
        return maxX;
    }

    /**
     *
     * @return
     */
    public double getMaxY() {
        return maxY.get();
    }

    /**
     *
     * @param maxY
     */
    public void setMaxY(double maxY) {
        this.maxY.set(maxY);
    }

    /**
     *
     * @return
     */
    public DoubleProperty maxYProperty() {
        return maxY;
    }

}
