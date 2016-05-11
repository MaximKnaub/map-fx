package de.mknaub.mapfx.layer.shape.features.style;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.DARKSLATEGREY;
import static javafx.scene.paint.Color.DODGERBLUE;
import javafx.scene.paint.Paint;

/**
 *
 * @author maka
 */
public class ShapeStyle {

    // Shape
    private final SimpleDoubleProperty opacity = new SimpleDoubleProperty(0.75);
    private final SimpleObjectProperty<Paint> fillColor = new SimpleObjectProperty<>(DODGERBLUE);
    private final SimpleObjectProperty<Paint> strokeColor = new SimpleObjectProperty<>(DARKSLATEGREY);
    private final SimpleDoubleProperty strokeWidth = new SimpleDoubleProperty(1.0);
    private final SimpleDoubleProperty shapeXOffset = new SimpleDoubleProperty(0);
    private final SimpleDoubleProperty shapeYOffset = new SimpleDoubleProperty(0);
    // Label
    private final SimpleDoubleProperty labelXOffset = new SimpleDoubleProperty(0);
    private final SimpleDoubleProperty labelYOffset = new SimpleDoubleProperty(0);

    public DoubleProperty opacityProperty() {
        return opacity;
    }

    public double getOpacity() {
        return opacity.getValue();
    }

    public void setOpacity(double opacity) {
        this.opacity.setValue(opacity);
    }

    public SimpleObjectProperty<Paint> fillProperty() {
        return fillColor;
    }

    public Color getFill() {
        return (Color) fillColor.getValue();
    }

    public void setFill(Color color) {
        this.fillColor.setValue(color);
    }

    public SimpleObjectProperty<Paint> strokeColorProperty() {
        return strokeColor;
    }

    public Color getStrokeColor() {
        return (Color) strokeColor.getValue();
    }

    public void setStrokeColor(Color color) {
        this.strokeColor.setValue(color);
    }

    public DoubleProperty strokeWidthProperty() {
        return strokeWidth;
    }

    public double getStrokeWidth() {
        return strokeWidth.getValue();
    }

    public void setStrokeWidth(double strokeWidth) {
        this.strokeWidth.setValue(strokeWidth);
    }

    public SimpleDoubleProperty shapeXOffsetProperty() {
        return shapeXOffset;
    }

    public double getShapeXOffset() {
        return shapeXOffset.getValue();
    }

    public void setShapeXOffset(double offset) {
        this.shapeXOffset.setValue(offset);
    }

    public SimpleDoubleProperty shapeYOffsetProperty() {
        return shapeYOffset;
    }

    public double getShapeYOffset() {
        return shapeYOffset.getValue();
    }

    public void setShapeYOffset(double offset) {
        this.shapeYOffset.setValue(offset);
    }

    // label
    public SimpleDoubleProperty labelXOffsetProperty() {
        return labelXOffset;
    }

    public double getLabelXOffset() {
        return labelXOffset.getValue();
    }

    public void setLabelXOffset(double offset) {
        this.labelXOffset.setValue(offset);
    }

    public SimpleDoubleProperty labelYOffsetProperty() {
        return labelYOffset;
    }

    public double getLabelYOffset() {
        return labelYOffset.getValue();
    }

    public void setLabelYOffset(double offset) {
        this.labelYOffset.setValue(offset);
    }
}
