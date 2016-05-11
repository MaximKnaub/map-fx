package de.knaubmaxim.mapfx.layer.shape.features;

import de.knaubmaxim.mapfx.coord.Coordinate;
import de.knaubmaxim.mapfx.layer.shape.features.style.ShapeStyle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.shape.Shape;

/**
 *
 * @author Maxim
 */
public class Line extends LabeledShape {
//    private final javafx.scene.shape.Line

    public Line(Coordinate coordinate) {
        super(coordinate);
    }

    @Override    public Shape getBasicShape() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override    public <T extends ShapeStyle> SimpleObjectProperty<T> shapeStyleProperty() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override    public <T extends ShapeStyle> T getShapeStyle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
