package de.mknaub.mapfx.control;

import de.mknaub.mapfx.Extent;
import de.mknaub.mapfx.Viewport;
import de.mknaub.mapfx.coord.Coordinate;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import static javafx.scene.input.MouseButton.PRIMARY;
import static javafx.scene.input.MouseButton.SECONDARY;
import javafx.scene.input.MouseEvent;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author maka
 */
@Slf4j
public class NavigationControl extends Control {

    // zoom
    private final DoubleProperty centerLon = new SimpleDoubleProperty(126.0);
    private final DoubleProperty centerLat = new SimpleDoubleProperty(126.0);
    private final SimpleBooleanProperty zoomEnabled = new SimpleBooleanProperty(true);

    private final SimpleBooleanProperty dragEnabled = new SimpleBooleanProperty(true);
    private boolean dragInitiated = false;
    private double initDragX, initDragY, lastDragX, lastDragY;

    public NavigationControl() {
        init();
    }

    private void init() {
        mapViewProperty().addListener((ov, oldVal, newVal) -> {
            if (oldVal != null) {
                centerLon.unbind();
                centerLat.unbind();
            }
            if (newVal != null) {
//                onCenter(new Coordinate(11, 51));
                centerLon.bind(newVal.widthProperty().divide(2.0));
                centerLat.bind(newVal.heightProperty().divide(2.0));
            }
        });
    }

    protected void renderLayers() {
        getMapView().getBaseLayer().renderRequest();
        getMapView().getLayers().forEach(layer -> layer.renderRequest());
    }

    ///// MOUSE EVENTS //
    @Override public void onMouseClicked(MouseEvent event) {
        if (getMapView() != null) {
            if (isActive()) {
                if (event.getClickCount() >= 2) {
                    if (event.getButton().equals(PRIMARY)) {
                        onCenter(getMapView().point2Coord(event.getSceneX(), event.getSceneY()));
                        zoomIn();
                    } else if (event.getButton().equals(SECONDARY)) {
                        onCenter(getMapView().point2Coord(event.getSceneX(), event.getSceneY()));
                        zoomOut();
                    }
                }
            }
        }
    }

    @Override public void onMousePressed(MouseEvent event) {
        if (getMapView() != null) {
            if (isActive()) {
                initDragX = lastDragX = event.getSceneX();
                initDragY = lastDragY = event.getSceneY();
                dragInitiated = true;
            }
        }
    }

    @Override public void onMouseReleased(MouseEvent event) {
        if (getMapView() != null) {
            if (isActive()) {
                dragInitiated = false;
                initDragX = initDragY = lastDragX = lastDragY = 0;
            }
        }
    }

    @Override public void onMouseDrag(MouseEvent event) {
        if (getMapView() != null) {
            if (isActive()) {
                if (dragInitiated) {
                    final double x = lastDragX - event.getSceneX(),
                            y = lastDragY - event.getSceneY();
                    Extent e = getMapView().getViewport().getExtent();
                    getMapView().getViewport().getExtent().setAll(e.getMinX() - x, e.getMinY() - y, e.getMaxX() - x, e.getMaxY() - y);
                    getMapView().getBaseLayer().move(x, y);
                    getMapView().getLayers().forEach(layer -> layer.move(x, y));
                    lastDragX = event.getSceneX();
                    lastDragY = event.getSceneY();
                }
            }
        }
    }

    ///// ZOOM //
    @Override public void zoomIn() {
        if (getMapView().getMapPane().getScene() != null) {
            double x = getMapView().getWidth() / 2.0, y = getMapView().getHeight() / 2.0;
            onZoom(1, x, y);
        }
    }

    @Override public void zoomOut() {
        if (getMapView().getMapPane().getScene() != null) {
            double x = getMapView().getWidth() / 2.0, y = getMapView().getHeight() / 2.0;
            onZoom(-1, x, y);
        }
    }

    @Override public void onZoom(double zoom) {
        if (getMapView().getMapPane().getScene() != null) {
            getMapView().getBaseLayer().setZoom(zoom);
        }
    }

    @Override public void onZoom(double delta, double x, double y) {
        if (getMapView() != null) {
            if (isActive()) {
                if (isZoomEnabled()) {
                    Viewport v = getMapView().getViewport();
                    getMapView().getBaseLayer().zoom(delta, x, y);
                    getMapView().getLayers().forEach(layer -> layer.zoom(delta, x, y));
                    v.setZoom(delta > 0 ? v.getZoom() + v.getZoomStep() : v.getZoom() - v.getZoomStep());
                    v.getExtent().setMinX(getMapView().getBaseLayer().getTranslateX());
                    v.getExtent().setMinY(getMapView().getBaseLayer().getTranslateY());

                    renderLayers();
                }
            }
        }
    }

    ///// CENTER //
    @Override public void onCenter(double longitude, double latitude) {
        onCenter(new Coordinate(longitude, latitude));
    }

    @Override public void onCenter(Coordinate coordinate) {
        getMapView().getBaseLayer().setCenter(coordinate);
        getMapView().getBaseLayer().renderRequest();
        getMapView().getLayers().forEach(layer -> layer.renderRequest());
    }

    ///// PROPERTIES
    public SimpleBooleanProperty zoomEnabled() {
        return zoomEnabled;
    }

    public SimpleBooleanProperty dragEnabled() {
        return dragEnabled;
    }

    ///// GETTER & SETTER
    public boolean isZoomEnabled() {
        return zoomEnabled.getValue();
    }

    public void setZoomEnabled(boolean zoomEnabled) {
        this.zoomEnabled.setValue(zoomEnabled);
    }

    public boolean isDragEnabled() {
        return dragEnabled.getValue();
    }

    public void setDragEnabled(boolean zoomEnabled) {
        this.dragEnabled.setValue(zoomEnabled);
    }

}
