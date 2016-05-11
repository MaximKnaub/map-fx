package de.mknaub.mapfx.layer.shape.features;

import de.mknaub.mapfx.coord.Coordinate;
import de.mknaub.mapfx.layer.shape.features.style.ShapeStyle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

/**
 *
 * @author maka
 */
public abstract class BasicShape extends Region {

    private Coordinate position;

    public BasicShape(Coordinate position) {
        this.position = position;
    }

    protected void init() {
        getBasicShape().opacityProperty().bindBidirectional(shapeStyleProperty().getValue().opacityProperty());
        getBasicShape().fillProperty().bindBidirectional(shapeStyleProperty().getValue().fillProperty());
        getBasicShape().strokeProperty().bindBidirectional(shapeStyleProperty().getValue().strokeColorProperty());
        getBasicShape().strokeWidthProperty().bindBidirectional(shapeStyleProperty().getValue().strokeWidthProperty());
        getBasicShape().translateXProperty().bind(shapeStyleProperty().getValue().shapeXOffsetProperty());
        getBasicShape().translateYProperty().bind(shapeStyleProperty().getValue().shapeYOffsetProperty());
    }

    abstract public Shape getBasicShape();

    @SuppressWarnings("unchecked") abstract public <T extends ShapeStyle> SimpleObjectProperty<T> shapeStyleProperty();

    @SuppressWarnings("unchecked") abstract public <T extends ShapeStyle> T getShapeStyle();

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    public void setFill(Color color) {
        getBasicShape().setFill(color);
    }
}
