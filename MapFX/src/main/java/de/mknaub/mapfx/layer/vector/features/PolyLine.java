package de.mknaub.mapfx.layer.vector.features;

import de.mknaub.mapfx.MapView;
import de.mknaub.mapfx.coord.Bound;
import de.mknaub.mapfx.coord.Coordinate;
import java.util.Collection;
import java.util.List;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author maka
 */
public class PolyLine extends BasicFeature {

    protected ObservableList<Coordinate> coordinates;
    private double[] lonArray, latArray;

    public PolyLine() {
        this.coordinates = observableArrayList();
    }

    public PolyLine(ObservableList<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public PolyLine(List<Coordinate> coordinates) {
        this(observableArrayList(coordinates));
    }

    @Override public void calculate(MapView mapView) {
        lonArray = new double[coordinates.size()];
        latArray = new double[coordinates.size()];

        for (int i = 0; i < coordinates.size(); i++) {
            Coordinate c = coordinates.get(i);
            Point2D scenePoint = mapView.getMapPoint(c);
            lonArray[i] = scenePoint.getX();
            latArray[i] = scenePoint.getY();
        }
    }

    @Override public void draw(GraphicsContext context) {
        context.setFill(getFillColor());
        context.setLineWidth(getStrokeWidth());
        context.setStroke(getStrokeColor());

        context.beginPath();
        for (int i = 0; i < lonArray.length; i++) {
            context.lineTo(lonArray[i], latArray[i]);
        }
        context.closePath();
//        context.fillPolygon(lonArray, latArray, lonArray.length);
        context.restore();
        context.strokePolyline(lonArray, latArray, lonArray.length);
    }

    @Override public boolean isOnScreen(Bound bound) {
//        return coordinates.stream().anyMatch((coordinate) -> (bound.isInside(coordinate)));
        return true;
    }

    public ObservableList<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void clearItems() {
        coordinates.clear();
    }

    public void addCoordinate(Coordinate coordinate) {
        coordinates.add(coordinate);
    }

    public void addAllCoordinates(Collection<? extends Coordinate> coordinates) {
        this.coordinates.addAll(coordinates);
    }

    public void removeCoordinate(Coordinate coordinate) {
        coordinates.remove(coordinate);
    }

    public void removeAllCoordinates(Collection<? extends Coordinate> coordinates) {
        this.coordinates.removeAll(coordinates);
    }

}
