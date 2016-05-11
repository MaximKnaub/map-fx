package de.mknaub.mapfx.control.draw;

import de.mknaub.mapfx.coord.Bound;
import de.mknaub.mapfx.coord.Coordinate;
import de.mknaub.mapfx.layer.vector.features.BasicFeature;
import de.mknaub.mapfx.layer.vector.features.Rectangle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.MouseEvent;
import static javafx.scene.paint.Color.DODGERBLUE;

/**
 *
 * @author maka
 */
public class RectangleDrawHandler implements DrawHandler {

    private final ObjectProperty<DrawControl> drawControl = new SimpleObjectProperty<>();
    protected Rectangle rectangle;
    private boolean isMousePressed = false;
    private OnDrawFinish<Rectangle> onDrawFinish;

    public RectangleDrawHandler() {
    }

    @Override public void setDrawControl(DrawControl drawControl) {
        this.drawControl.setValue(drawControl);
    }

    @Override public void onMouseClicked(MouseEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override public void onMousePressed(MouseEvent event) {
        if (isMousePressed != true) {
            isMousePressed = true;
            Coordinate c = drawControl.getValue().getMapView().point2Coord(event.getSceneX(), event.getSceneY());
            rectangle = new Rectangle(new Coordinate(c.getLongitude(), c.getLatitude()));
            rectangle.setFillColor(DODGERBLUE);
            rectangle.setOpacity(0.5);
            rectangle.setCoordinate(new Coordinate(c.getLongitude(), c.getLatitude()));
            drawControl.getValue().getLayer().addFeature(rectangle);
        }
    }

    @Override public void onMouseReleased(MouseEvent event) {
        if (isMousePressed) {
            Rectangle r = initRectAngle(rectangle.getBound());
            drawControl.getValue().getLayer().removeFeature(rectangle);
            drawControl.getValue().getLayer().addFeature(r);
            isMousePressed = false;
            if (getOnDrawFinish() != null) {
                getOnDrawFinish().handle(rectangle);
            }
        }
    }

    @Override public void onMouseMoved(MouseEvent event) {
    }

    @Override public void onMouseDrag(MouseEvent event) {
        if (isMousePressed) {
            Coordinate c = drawControl.getValue().getMapView().point2Coord(event.getSceneX(), event.getSceneY());
            rectangle.getBound().setRight(c.getLongitude());
            rectangle.getBound().setBottom(c.getLatitude());
        }
    }

    @Override public BasicFeature getFeature() {
        return rectangle;
    }

    public OnDrawFinish<Rectangle> getOnDrawFinish() {
        return onDrawFinish;
    }

    public void setOnDrawFinish(OnDrawFinish<Rectangle> onDrawFinish) {
        this.onDrawFinish = onDrawFinish;
    }

    private Rectangle initRectAngle(Bound bound) {
        Rectangle r = new Rectangle(bound);
        r.setFillColor(DODGERBLUE);
        r.setOpacity(0.5);
        return r;
    }
}
