package de.knaubmaxim.mapfx.layer.shape.features;

import de.knaubmaxim.mapfx.coord.Coordinate;
import de.knaubmaxim.mapfx.layer.shape.features.style.CircleShapeStyle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.shape.Shape;

/**
 *
 * @author maka
 */
public class Circle extends LabeledShape {

    protected final SimpleObjectProperty<CircleShapeStyle> shapeStyle = new SimpleObjectProperty<>(new CircleShapeStyle());
    private final javafx.scene.shape.Circle shape;

    public Circle(Coordinate position) {
        super(position);
        this.shape = new javafx.scene.shape.Circle();
        this.shape.getStyleClass().add("map-fx-shape");
        getChildren().addAll(shape, getLabel());
        init();
        initView();
        getShapeStyle().setRadius(1);
    }

    @Override protected void init() {
        super.init();
        shape.radiusProperty().bindBidirectional(shapeStyle.getValue().radiusProperty());
    }

    private void initView() {
        getShapeStyle().setRadius(10);
        getShapeStyle().setStrokeWidth(2);
        shapeStyle.getValue().labelYOffsetProperty().bind(getLabel().heightProperty().add(shapeStyleProperty().getValue().getRadius()).negate());
    }

    @Override public Shape getBasicShape() {
        return shape;
    }

    @Override public SimpleObjectProperty<CircleShapeStyle> shapeStyleProperty() {
        return shapeStyle;
    }

    @Override public CircleShapeStyle getShapeStyle() {
        return shapeStyle.getValue();
    }
}
