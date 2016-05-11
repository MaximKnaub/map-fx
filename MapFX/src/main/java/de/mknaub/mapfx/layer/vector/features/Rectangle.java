package de.knaubmaxim.mapfx.layer.vector.features;

import de.knaubmaxim.mapfx.MapView;
import de.knaubmaxim.mapfx.coord.Bound;
import de.knaubmaxim.mapfx.coord.Coordinate;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author maka
 */
@Slf4j
public class Rectangle extends BasicFeature {

    protected Coordinate coordinate;
    protected Bound bound;
    private Point2D upperLeft;
    private Point2D lowerRight;

    public Rectangle() {
        coordinate = new Coordinate(0, 0);
        bound = new Bound(coordinate);
    }

    public Rectangle(Coordinate coordinate, double radius) {
        this.coordinate = coordinate;
        this.bound = new Bound(coordinate, radius);
    }

    public Rectangle(Coordinate coordinate) {
        this.coordinate = coordinate;
        this.bound = new Bound(coordinate.getLatitude(), coordinate.getLongitude(), coordinate.getLatitude(), coordinate.getLongitude());
    }

    public Rectangle(Bound bound) {
        this.coordinate = new Coordinate(bound.getLeft(), bound.getTop());
        this.bound = bound;
    }

    @Override public void calculate(final MapView mapView) {
        if (Platform.isFxApplicationThread()) {
            upperLeft = mapView.getMapPoint(bound.getLeft(), bound.getTop());
            lowerRight = mapView.getMapPoint(bound.getRight(), bound.getBottom());
        } else {
            Platform.runLater(() -> calculate(mapView));
        }
    }

    @Override public void draw(final GraphicsContext context) {
        if (Platform.isFxApplicationThread()) {
            context.restore();
            // draw stroke
            context.setStroke(getStrokeColor());
            context.setLineWidth(getStrokeWidth());
            context.strokeRect(upperLeft.getX(), upperLeft.getY(), lowerRight.getX() - upperLeft.getX(), lowerRight.getY() - upperLeft.getY());

            // draw fill
            context.restore();
            context.setFill(getFillColor());
            context.fillRect(upperLeft.getX(), upperLeft.getY(), lowerRight.getX() - upperLeft.getX(), lowerRight.getY() - upperLeft.getY());
        } else {
            Platform.runLater(() -> draw(context));
        }
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Bound getBound() {
        return bound;
    }

    @Override public boolean isOnScreen(Bound bound) {
        return bound.isInside(this.bound);
    }

}
