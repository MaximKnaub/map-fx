package de.mknaub.mapfx;

import de.mknaub.mapfx.control.Control;
import de.mknaub.mapfx.control.NavigationControl;
import de.mknaub.mapfx.coord.Bound;
import de.mknaub.mapfx.coord.Coordinate;
import de.mknaub.mapfx.events.CenterEvent;
import de.mknaub.mapfx.events.DragEvent;
import de.mknaub.mapfx.events.MapEvent;
import de.mknaub.mapfx.events.ZoomEvent;
import de.mknaub.mapfx.layer.Layer;
import de.mknaub.mapfx.layer.TileLayer;
import java.util.Collection;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author maka
 */
public interface MapView {

    ///// Basic ////////////////////////////////////////////////////////////////
    Viewport getViewport();

    Node getMapPane();

    double getWidth();

    ReadOnlyDoubleProperty widthProperty();

    double getHeight();

    ReadOnlyDoubleProperty heightProperty();

    ///// Events ///////////////////////////////////////////////////////////////
    void setOnCenterChanged(CenterEvent event);

    void setOnDrag(DragEvent event);

    void setOnDragged(DragEvent event);

    void setOnZoom(MapEvent<ZoomEvent> event);

    void setOnZoomed(ZoomEvent event);

    void removeOnCenterChanged(CenterEvent event);

    void removeOnDragged(DragEvent event);

    void removeOnZoomed(ZoomEvent event);

    void setOnMouseClicked(MouseEvent event);

    void setOnMouseReleasedEvents(EventHandler<MouseEvent> event);

    void setOnMouseMovedEvents(EventHandler<MouseEvent> event);

    ///// map positioning //////////////////////////////////////////////////////
    void setCenter(double longitude, double latitude);

    void setCenter(Coordinate coord);

    Coordinate getCenter();

    Bound getBounds();

    SimpleObjectProperty<Coordinate> centerProperty();

    void moveTo(double longitude, double latitude);

    void moveTo(Coordinate coordinate);

    void zoomIn();

    void zoomOut();

    void setZoom(double zoom);

    double getZoom();

    DoubleProperty zoomProperty();

    DoubleProperty zoomStepProperty();

    double getZoomStep();

    void setZoomStep(double zoomStep);

    ///// Layers ///////////////////////////////////////////////////////////////
    void addLayer(Layer layer);

    void addLayer(int index, Layer layer);

    void addLayer(Layer... layers);

    void addLayer(Collection<Layer> layers);

    Layer getLayer(int index);

    ObservableList<Layer> getLayers();

    ObjectProperty<TileLayer> baseLayerProperty();

    TileLayer getBaseLayer();

    void setBaseLayer(TileLayer layer);

    void removeLayer(int index);

    void removeLayer(Layer layer);

    void removeLayer(Layer... layers);

    void removeLayer(Collection<Layer> layers);

    void setLayerIndex(Layer layer, int index);

    Bound getMapPoint(Bound bound);

    Point2D getMapPoint(Coordinate coordinate);

    Point2D getMapPoint(double lon, double lat);

    Point2D getMapPoint(double zoom, double lon, double lat);

    Coordinate point2Coord(double x, double y);

    ///// Controller ///////////////////////////////////////////////////////////
    NavigationControl getNavigationControl();

    void setNavigationControl(NavigationControl navigationControl);

    //
    void addControl(Control control);

    void addControl(Control... controls);

    void addControl(Collection<Control> controls);

    Control getControl(int index);

    ObservableList<Control> getControls();

    void removeControl(int index);

    void removeControl(Control control);

    void removeControl(Control... controls);

    void removeControl(Collection<Control> controls);
}
