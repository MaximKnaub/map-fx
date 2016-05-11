package de.mknaub.mapfx.layer;

import de.mknaub.mapfx.MapView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author maka
 */
public abstract class Layer implements ILayer {

    protected SimpleStringProperty layerName = new SimpleStringProperty();
    protected SimpleObjectProperty<MapView> mapView = new SimpleObjectProperty<>();
    protected SimpleObjectProperty<Boolean> isBaseLayer = new SimpleObjectProperty<>(false);

    public Layer(MapView mapView) {
        this.mapView.setValue(mapView);
    }

    @Override public String getName() {
        return this.layerName.getValue();
    }

    @Override public void setName(String name) {
        this.layerName.setValue(name);
    }

    public MapView getMapView() {
        return this.mapView.getValue();
    }

    @Override public boolean isBaseLayer() {
        return isBaseLayer.getValue();
    }

    @Override public void setBaseLayer(boolean isBaseLayer) {
        this.isBaseLayer.setValue(isBaseLayer);
    }

    abstract public void renderRequest();

    @Override public void setVisible(boolean visible) {
        getView().setVisible(visible);
    }

    public void setZoom(double zoom) {

    }

    public void zoom(double delta, double x, double y) {
    }
    
    public void move(double x, double y) {
    }

    public void moveX(double x) {

    }

    public void moveY(double y) {

    }

}
