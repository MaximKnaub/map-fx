package de.mknaub.mapfx.control.draw;

import de.mknaub.mapfx.coord.Coordinate;
import de.mknaub.mapfx.layer.vector.features.BasicFeature;
import de.mknaub.mapfx.layer.vector.features.Polygon;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author maka
 */
public class PolygonDrawHandler implements DrawHandler {

    private final ObjectProperty<DrawControl> drawControl = new SimpleObjectProperty<>();
    private Polygon polygon;
    private Coordinate lastCoordiante;
    private OnDrawFinish<Polygon> onDrawFinish;

    @Override public void setDrawControl(DrawControl drawControl) {
        this.drawControl.setValue(drawControl);
    }

    @Override public void onMouseClicked(MouseEvent event) {
        if (polygon == null) {
            polygon = new Polygon();
            polygon.setOpacity(0.75);
            lastCoordiante = drawControl.getValue().getMapView().point2Coord(event.getSceneX(), event.getSceneY());
            polygon.getCoordinates().add(lastCoordiante);
            lastCoordiante = new Coordinate(lastCoordiante.getLongitude(), lastCoordiante.getLatitude());
            polygon.getCoordinates().add(lastCoordiante);
            drawControl.getValue().getLayer().addFeature(polygon);
        } else {
            if (event.getClickCount() >= 2) {
                drawControl.getValue().getLayer().removeFeature(polygon);
                if (getOnDrawFinish() != null) {
                    getOnDrawFinish().handle(polygon);
                }
                polygon = null;
                lastCoordiante = null;
            } else {
                polygon.getCoordinates().add(lastCoordiante = drawControl.getValue().getMapView().point2Coord(event.getSceneX(), event.getSceneY()));
            }
        }
    }

    @Override public void onMousePressed(MouseEvent event) {
    }

    @Override public void onMouseReleased(MouseEvent event) {
    }

    @Override public void onMouseMoved(MouseEvent event) {
        if (lastCoordiante == null) {
            return;
        }
        Coordinate c = drawControl.getValue().getMapView().point2Coord(event.getSceneX(), event.getSceneY());
        lastCoordiante.setLongitude(c.getLongitude());
        lastCoordiante.setLatitude(c.getLatitude());
    }

    @Override public void onMouseDrag(MouseEvent event) {
        lastCoordiante = drawControl.getValue().getMapView().point2Coord(event.getSceneX(), event.getSceneY());
        if (polygon == null) {
            polygon = new Polygon();
            polygon.setOpacity(0.75);
            polygon.getCoordinates().add(lastCoordiante);
            lastCoordiante = new Coordinate(lastCoordiante.getLongitude(), lastCoordiante.getLatitude());
            polygon.getCoordinates().add(lastCoordiante);
            drawControl.getValue().getLayer().addFeature(polygon);
        } else {
            polygon.getCoordinates().add(lastCoordiante);
        }
    }

    @Override public BasicFeature getFeature() {
        return polygon;
    }

    public OnDrawFinish<Polygon> getOnDrawFinish() {
        return onDrawFinish;
    }

    public void setOnDrawFinish(OnDrawFinish<Polygon> onDrawFinish) {
        this.onDrawFinish = onDrawFinish;
    }

}
