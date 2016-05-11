package de.mknaub.mapfx.layer.vector.features;

import de.mknaub.mapfx.MapView;
import de.mknaub.mapfx.coord.Bound;
import de.mknaub.mapfx.coord.Coordinate;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import static javafx.scene.paint.Color.RED;

/**
 *
 * @author maka
 */
public class Circle extends BasicFeature {

    protected Coordinate coordinate;
    protected double radius;
    private double[] lonArray, latArray;

    /**
     *
     * @param coordinate Coordinate
     * @param radius in m
     */
    public Circle(Coordinate coordinate, double radius) {
        this.coordinate = coordinate;
        this.radius = radius;
        lonArray = new double[361];
        latArray = new double[361];
        setFillColor(RED);
    }

    @Override public void calculate(MapView mapView) {
        for (int i = 0; i <= 360; i++) {
            Point2D scenePoint = mapView.getMapPoint(coordinate.destinationPoint(i, radius));
            lonArray[i] = scenePoint.getX();
            latArray[i] = scenePoint.getY();
        }
    }

    @Override public void draw(GraphicsContext context) {
        context.setFill(getFillColor());
        context.setLineWidth(5);
        context.fillPolygon(lonArray, latArray, lonArray.length);
    }

    @Override public boolean isOnScreen(Bound bound) {
        return bound.isInside(coordinate);
    }
}
