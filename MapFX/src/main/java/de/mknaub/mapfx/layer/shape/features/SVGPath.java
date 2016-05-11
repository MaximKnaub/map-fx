package de.knaubmaxim.mapfx.layer.shape.features;

import de.knaubmaxim.mapfx.coord.Coordinate;
import de.knaubmaxim.mapfx.layer.shape.features.style.CircleShapeStyle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.shape.Shape;

/**
 *
 * @author maka
 */
public class SVGPath extends LabeledShape {

    protected final SimpleObjectProperty<CircleShapeStyle> shapeStyle = new SimpleObjectProperty<>(new CircleShapeStyle());
    private final javafx.scene.shape.SVGPath shape;

    public SVGPath(Coordinate position, String svgConten) {
        super(position);
        this.shape = new javafx.scene.shape.SVGPath();
        System.out.println("pos: " + position);
        this.shape.setContent(svgConten);
        this.shape.getStyleClass().add("map-fx-shape");
        this.shape.setSmooth(true);
        getChildren().addAll(shape, getLabel());
        init();
        initView();
        getShapeStyle().setRadius(1);
    }

    @Override protected void init() {
        super.init();
    }

    private void initView() {
        getShapeStyle().setRadius(10);
        getShapeStyle().setStrokeWidth(2);
        shapeStyle.getValue().labelYOffsetProperty().bind(getLabel().heightProperty().add(shape.getLayoutBounds().getHeight()/4).negate());
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
