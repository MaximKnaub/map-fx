package de.knaubmaxim.mapfx.layer.shape.features;

import de.knaubmaxim.mapfx.coord.Coordinate;
import de.knaubmaxim.mapfx.layer.shape.features.style.ArcShapeStyle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import static javafx.scene.shape.ArcType.ROUND;
import javafx.scene.shape.Shape;

/**
 *
 * @author Maxim
 */
public class Arc extends LabeledShape {

    protected final SimpleObjectProperty<ArcShapeStyle> shapeStyle = new SimpleObjectProperty<>(new ArcShapeStyle());
    protected final javafx.scene.shape.Arc shape;

    public Arc(Coordinate position) {
        super(position);
        this.shape = new javafx.scene.shape.Arc();
        this.shape.getStyleClass().add("map-fx-shape");
        getChildren().addAll(shape, getLabel());
        init();
        initView();
        shape.setOnMouseEntered(e -> shape.setFill(Color.RED));
        shape.setOnMouseExited(e -> shape.setFill(Color.DODGERBLUE));
    }

    @Override protected void init() {
        super.init();
        shape.typeProperty().bindBidirectional(shapeStyle.getValue().arcTypeProperty());
        shape.radiusXProperty().bindBidirectional(shapeStyle.getValue().radiusXProperty());
        shape.radiusYProperty().bindBidirectional(shapeStyle.getValue().radiusYProperty());
        shape.lengthProperty().bindBidirectional(shapeStyle.getValue().lengthProperty());
    }

    private void initView() {
        getShapeStyle().setArcType(ROUND);
        getShapeStyle().setRadiusX(45);
        getShapeStyle().setRadiusY(45);
        getShapeStyle().setStartAngle(70);
        getShapeStyle().setLength(45);
        getShapeStyle().strokeWidthProperty().setValue(2);
    }

    @Override public Shape getBasicShape() {
        return shape;
    }

    @Override public SimpleObjectProperty<ArcShapeStyle> shapeStyleProperty() {
        return shapeStyle;
    }

    @Override public ArcShapeStyle getShapeStyle() {
        return shapeStyle.getValue();
    }

}
