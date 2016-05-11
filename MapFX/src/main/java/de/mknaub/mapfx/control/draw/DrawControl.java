package de.mknaub.mapfx.control.draw;

import de.mknaub.mapfx.control.Control;
import de.mknaub.mapfx.layer.vector.VectorLayer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.MouseEvent;

/**
 * TODO code some generic solution
 *
 * @author maka
 */
public class DrawControl extends Control {

    private final SimpleObjectProperty<VectorLayer> layer = new SimpleObjectProperty<>();
    protected final ObjectProperty<DrawHandler> drawHandler = new SimpleObjectProperty<>();

    public DrawControl(DrawHandler drawHandler) {
        drawHandler.setDrawControl(DrawControl.this);
        this.drawHandler.setValue(drawHandler);
        init();
    }

    private void init() {
        activeProperty().addListener((observable, oldValue, newValue) -> {
            mapViewProperty().getValue().getNavigationControl().activeProperty().setValue(!newValue);
        });
    }

    public VectorLayer getLayer() {
        return layer.getValue();
    }

    public void setLayer(VectorLayer layer) {
        this.layer.setValue(layer);
    }

    public ReadOnlyObjectProperty<VectorLayer> layerProperty() {
        return layer;
    }

    @Override public void onMouseClicked(MouseEvent event) {
        if (getMapView() != null) {
            if (isActive()) {
                if (getLayer() != null) {
                    getDrawHandler().onMouseClicked(event);
                    getLayer().renderRequest();
                }
            }
        }
    }

    @Override public void onMousePressed(MouseEvent event) {
        if (getMapView() != null) {
            if (isActive()) {
                if (getLayer() != null) {
                    getDrawHandler().onMousePressed(event);
                    getLayer().renderRequest();
                }
            }
        }
    }

    @Override public void onMouseDrag(MouseEvent event) {
        if (getMapView() != null) {
            if (isActive()) {
                if (getLayer() != null) {
                    getDrawHandler().onMouseDrag(event);
                    getLayer().renderRequest();
                }
            }
        }
    }

    @Override public void onMouseReleased(MouseEvent event) {
        if (getMapView() != null) {
            if (isActive()) {
                if (getLayer() != null) {
                    getDrawHandler().onMouseReleased(event);
                    getLayer().renderRequest();
                }
            }
        }
    }

    public void onMouseMoved(MouseEvent event) {
        if (getMapView() != null) {
            if (isActive()) {
                if (getLayer() != null) {
                    getDrawHandler().onMouseMoved(event);
                    getLayer().renderRequest();
                }
            }
        }
    }

    public ObjectProperty<DrawHandler> drawHandlerProperty() {
        return drawHandler;
    }

    public DrawHandler getDrawHandler() {
        return drawHandler.getValue();
    }

    public void setDrawHandler(DrawHandler drawHandler) {
        this.drawHandler.setValue(drawHandler);
    }
}
