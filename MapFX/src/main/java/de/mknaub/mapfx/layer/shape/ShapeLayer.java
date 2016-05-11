package de.knaubmaxim.mapfx.layer.shape;

import de.knaubmaxim.mapfx.MapView;
import de.knaubmaxim.mapfx.coord.Coordinate;
import de.knaubmaxim.mapfx.layer.Layer;
import de.knaubmaxim.mapfx.layer.shape.features.BasicShape;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javafx.beans.Observable;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author maka
 */
@Slf4j
public class ShapeLayer extends Layer {

    private final Pane layerPane;
    private final List<Shape> shapes = new LinkedList<>();
    private final Map<Shape, Coordinate> shapeCoordMap = new HashMap<>();
    ///// experimental //
    private final ObservableList<BasicShape> basicShapes = observableArrayList();

    public ShapeLayer(MapView mapView) {
        super(mapView);
        layerPane = new Pane();
        basicShapes.addListener((Observable observable) -> renderRequest());
    }

    @Override public void move(double x, double y) {
        moveX(x);
        moveY(y);
    }

    @Override public void moveX(double x) {
        layerPane.setTranslateX(layerPane.getTranslateX() - x);
    }

    @Override public void moveY(double y) {
        layerPane.setTranslateY(layerPane.getTranslateY() - y);
    }

    @Override public void renderRequest() {
        layerPane.setTranslateX(0);
        layerPane.setTranslateY(0);
        shapes.stream().forEach(shape -> {
            Coordinate coord = shapeCoordMap.get(shape);
            Point2D mapPoint = super.mapView.get().getMapPoint(coord);
//            log.trace("coor: " + coord + " ==>> " + " point: " + mapPoint);
            shape.setTranslateX(mapPoint.getX());
            shape.setTranslateY(mapPoint.getY());
        });
        basicShapes.stream().forEach(shape -> {
            Point2D mapPoint = super.mapView.get().getMapPoint(shape.getPosition());
//            log.trace("coor: " + coord + " ==>> " + " point: " + mapPoint);
            shape.setTranslateX(mapPoint.getX());
            shape.setTranslateY(mapPoint.getY());
        });
    }

    @Override public Node getView() {
        return layerPane;
    }

    public void addShape(Shape shape, Coordinate coordinate) {
        shape.setManaged(false);
        shapes.add(shape);
        shapeCoordMap.put(shape, coordinate);
        layerPane.getChildren().add(shape);
    }

    public void clearShapes() {
        layerPane.getChildren().clear();
        shapes.clear();
        shapeCoordMap.clear();
    }

    public void removeShape(Shape shape) {
        shapes.remove(shape);
        shapeCoordMap.remove(shape);
        layerPane.getChildren().remove(shape);
    }

    @Override public double getTranslateX() {
        return layerPane.getTranslateX();
    }

    @Override public double getTranslateY() {
        return layerPane.getTranslateY();
    }

    ///// experimental //
    public void addBasicShape(BasicShape basicShape) {
        basicShapes.add(basicShape);
        layerPane.getChildren().add(basicShape);
    }

    public void addAllBasicShapes(Collection<? extends BasicShape> basicShapes) {
        this.basicShapes.addAll(basicShapes);
        layerPane.getChildren().addAll(basicShapes);
    }

    public void removeBasicShape(BasicShape basicShape) {
        basicShapes.remove(basicShape);
        layerPane.getChildren().remove(basicShape);
    }

    public void removeAllBasicShapes(Collection<? extends BasicShape> basicShapes) {
        this.basicShapes.removeAll(basicShapes);
        layerPane.getChildren().removeAll(basicShapes);
    }

}
