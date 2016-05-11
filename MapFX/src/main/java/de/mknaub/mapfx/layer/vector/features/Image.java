package de.mknaub.mapfx.layer.vector.features;

import de.mknaub.mapfx.MapView;
import de.mknaub.mapfx.coord.Bound;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

/**
 * TODO könnte auch Point erweitern und it styles angepasst werden
 *
 * @author maka
 */
public class Image extends BasicFeature {

    private javafx.scene.image.Image image;
    private final Bound bound;
    protected Bound sceneBound;
    protected double x, y, width = 250, height = 250;

    public Image(javafx.scene.image.Image image, Bound bound) {
        this.image = image;
        this.bound = bound;
    }

    @Override public void calculate(MapView mapView) {
        sceneBound = mapView.getMapPoint(bound);
        width = (sceneBound.getLeft() - sceneBound.getRight());
        width = width < 0 ? width *= -1 : width;
        height = (sceneBound.getTop() - sceneBound.getBottom());
        height = height < 0 ? height *= -1 : height;
        Point2D centerPoint = mapView.getMapPoint((bound.getLeft() + bound.getRight()) / 2.0, (bound.getTop() + bound.getBottom()) / 2.0);
        x = centerPoint.getX() - width / 2.0;
        y = centerPoint.getY() - height / 2.0;
    }

    @Override public void draw(GraphicsContext context) {
        context.setGlobalAlpha(getFillColor().getOpacity());
        context.drawImage(image, x, y, width, height);
    }

    @Override public boolean isOnScreen(Bound bound) {
//        return bound.isInside(this.bound)
//                || bound.isInside((this.bound.getLeft() + this.bound.getRight()) / 2.0, (this.bound.getTop() + this.bound.getBottom()) / 2.0);
        return true;
    }

    public void setImage(javafx.scene.image.Image image){
        this.image = image;
    }

}
