package de.knaubmaxim.mapfx.layer.shape.features.style;

import javafx.beans.property.SimpleDoubleProperty;

/**
 *
 * @author maka
 */
public class CircleShapeStyle extends ShapeStyle {

    private final SimpleDoubleProperty radius = new SimpleDoubleProperty(0);

    public SimpleDoubleProperty radiusProperty() {
        return radius;
    }

    public double getRadius() {
        return radius.getValue();
    }

    public void setRadius(double radius) {
        this.radius.setValue(radius);
    }
}
