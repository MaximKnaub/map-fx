package de.knaubmaxim.mapfx.layer.vector.features;

import de.knaubmaxim.mapfx.MapView;
import de.knaubmaxim.mapfx.coord.Bound;
import de.knaubmaxim.mapfx.coord.Coordinate;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author maka
 */
public class Point extends BasicFeature {

    protected Coordinate coordinate;
    protected double x, y, radius = 5.d;

    public Point(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Override public void calculate(MapView mapView) {
        Point2D scenePoint = mapView.getMapPoint(coordinate);
        x = scenePoint.getX() - radius;
        y = scenePoint.getY() - radius;
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setFill(getFillColor());
        context.fillOval(x, y, radius * 2.0, radius * 2.0);
    }

    @Override
    public boolean isOnScreen(Bound bound) {
        return bound.isInside(coordinate);
    }

    // tests
    public boolean isMouseHover(MouseEvent event) {
        Point2D l2s = getMapView().getMapPane().localToScene(0, 0);
        Point2D p = getMapView().getMapPoint(coordinate);
        double centerX = p.getX(), centerY = p.getY(), x = event.getSceneX() - l2s.getX(), y = event.getSceneY() - l2s.getY();
        if (x >= centerX - radius && x <= centerX + radius
                && y >= centerY - radius && y <= centerY + radius) {
            double dx = centerX - x;
            double dy = centerY - y;
            dx *= dx;
            dy *= dy;
            double distanceSquared = dx + dy;
            double radiusSquared = radius * radius;
            return distanceSquared <= radiusSquared;
        } else {
            return false;
        }
    }
}
