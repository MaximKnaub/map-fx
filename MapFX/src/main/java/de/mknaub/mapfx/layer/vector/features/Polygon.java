package de.knaubmaxim.mapfx.layer.vector.features;

import de.knaubmaxim.mapfx.MapView;
import de.knaubmaxim.mapfx.coord.Bound;
import de.knaubmaxim.mapfx.coord.Coordinate;
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
public class Polygon extends BasicFeature {

    protected ObservableList<Coordinate> coordinates;
    private double[] lonArray, latArray;

    public Polygon() {
        this.coordinates = observableArrayList();
    }

    public Polygon(ObservableList<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public Polygon(List<Coordinate> coordinates) {
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
        context.fillPolygon(lonArray, latArray, lonArray.length);
        context.restore();
        context.setLineWidth(getStrokeWidth());
        context.setStroke(getStrokeColor());
        context.strokePolygon(lonArray, latArray, lonArray.length);
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
