package de.knaubmaxim.mapfx.layer.shape.features.style;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.shape.ArcType;
import static javafx.scene.shape.ArcType.ROUND;

/**
 *
 * @author Maxim
 */
public class ArcShapeStyle extends ShapeStyle {

    private final SimpleObjectProperty<ArcType> arcType = new SimpleObjectProperty<>(ROUND);
    private final SimpleDoubleProperty radiusX = new SimpleDoubleProperty(0);
    private final SimpleDoubleProperty radiusY = new SimpleDoubleProperty(0);
    private final SimpleDoubleProperty startAngle = new SimpleDoubleProperty(0);
    private final SimpleDoubleProperty length = new SimpleDoubleProperty(0);

    public SimpleObjectProperty<ArcType> arcTypeProperty() {
        return arcType;
    }

    public ArcType getArcType() {
        return arcType.getValue();
    }

    public void setArcType(ArcType arcType) {
        this.arcType.setValue(arcType);
    }

    public SimpleDoubleProperty radiusXProperty() {
        return radiusX;
    }

    public double getRadiusX() {
        return radiusX.getValue();
    }

    public void setRadiusX(double radiusX) {
        this.radiusX.setValue(radiusX);
    }

    public SimpleDoubleProperty radiusYProperty() {
        return radiusY;
    }

    public double getRadiusY() {
        return radiusY.getValue();
    }

    public void setRadiusY(double radiusY) {
        this.radiusY.setValue(radiusY);
    }

    public SimpleDoubleProperty startAngleProperty() {
        return startAngle;
    }

    public double getStartAngle() {
        return startAngle.getValue();
    }

    public void setStartAngle(double startAngle) {
        this.startAngle.setValue(startAngle);
    }

    public SimpleDoubleProperty lengthProperty() {
        return length;
    }

    public double getLength() {
        return length.getValue();
    }

    public void setLength(double length) {
        this.length.setValue(length);
    }
}
