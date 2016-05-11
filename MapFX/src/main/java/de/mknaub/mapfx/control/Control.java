package de.mknaub.mapfx.control;

import de.mknaub.mapfx.MapView;
import de.mknaub.mapfx.coord.Coordinate;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author maka
 */
public abstract class Control {

    private final SimpleObjectProperty<MapView> mapView = new SimpleObjectProperty<>();
    private final SimpleBooleanProperty isActive = new SimpleBooleanProperty(false);

    public void setMapView(MapView mapView) {
        this.mapView.setValue(mapView);
    }

    public MapView getMapView() {
        return this.mapView.getValue();
    }

    public ReadOnlyObjectProperty<MapView> mapViewProperty() {
        return mapView;
    }

    public final void activate() {
        isActive.setValue(TRUE);
    }

    public final void deactivate() {
        isActive.setValue(FALSE);
    }

    public final boolean isActive() {
        return isActive.getValue();
    }

    public final BooleanProperty activeProperty() {
        return isActive;
    }

    ///// mouse events //
    public void onMouseClicked(MouseEvent event) {
    }

    public void onMousePressed(MouseEvent event) {
    }

    public void onMouseReleased(MouseEvent event) {
    }

    public void onMouseMoved(MouseEvent event) {
    }

    public void onMouseDrag(MouseEvent event) {
    }

    public void onMouseDragged(MouseEvent event) {
    }

    ///// zoom //
    public void zoomIn() {
    }

    public void zoomOut() {
    }

    public void onZoom(double delta, double x, double y) {
    }

    public void onZoomed(double delta, double x, double y) {
    }

    public void onZoom(double zoom) {
    }

    public void onZoomed(double zoom) {
    }

    ///// center //
    public void onCenter(double longitude, double latitude) {
    }

    public void onCenter(Coordinate coordinate) {
    }
}
