package de.knaubmaxim.mapfx.layer.shape.features;

import de.knaubmaxim.mapfx.coord.Coordinate;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;

/**
 *
 * @author Maxim
 */
public abstract class LabeledShape extends BasicShape {

    protected final Label label = new Label();

    public LabeledShape(Coordinate coordinate) {
        super(coordinate);
        label.getStyleClass().add("map-fx-label");
    }

    @Override protected void init() {
        super.init();
        getLabel().translateXProperty().bindBidirectional(shapeStyleProperty().getValue().labelXOffsetProperty());
        getLabel().translateYProperty().bindBidirectional(shapeStyleProperty().getValue().labelYOffsetProperty());
    }

    protected Label getLabel() {
        return label;
    }

    public String getText() {
        return this.label.getText();
    }

    public void setText(String text) {
        this.label.setText(text);
    }

    public StringProperty textProperty() {
        return label.textProperty();
    }
}
