package de.knaubmaxim.mapfx;

import de.knaubmaxim.mapfx.control.Control;
import de.knaubmaxim.mapfx.control.NavigationControl;
import de.knaubmaxim.mapfx.control.ScaleLine;
import de.knaubmaxim.mapfx.coord.Bound;
import de.knaubmaxim.mapfx.coord.Coordinate;
import de.knaubmaxim.mapfx.coord.CoordinateUtils;
import de.knaubmaxim.mapfx.events.CenterEvent;
import de.knaubmaxim.mapfx.events.DragEvent;
import de.knaubmaxim.mapfx.events.MapEvent;
import de.knaubmaxim.mapfx.events.MapFXEventDispatcher;
import de.knaubmaxim.mapfx.events.ZoomEvent;
import de.knaubmaxim.mapfx.layer.Layer;
import de.knaubmaxim.mapfx.layer.TileLayer;
import de.knaubmaxim.mapfx.layer.tileprovider.OpenStreetMapLayer;
import static java.lang.Double.MAX_VALUE;
import static java.lang.Math.PI;
import static java.lang.Math.atan;
import static java.lang.Math.pow;
import static java.lang.Math.sinh;
import static java.lang.Math.toDegrees;
import static java.util.Arrays.asList;
import java.util.Collection;
import javafx.application.Platform;
import static javafx.application.Platform.runLater;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author maka
 */
@Slf4j
public class MapViewFX extends Pane implements MapView {

    // GUI
    private final StackPane layerPane;
    // MAP
    private final SimpleObjectProperty<TileLayer> baseLayerProperty;
    private final SimpleObjectProperty<NavigationControl> navigationControl = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Coordinate> center = new SimpleObjectProperty<>(new Coordinate(0, 0));
    private final ObservableList<Layer> layerList = observableArrayList();
    private final ObservableList<Control> controlList = observableArrayList();
    private final Viewport viewport;
    // Events
    @Deprecated private final ObservableList<CenterEvent> centerEvents = observableArrayList();
    @Deprecated private final ObservableList<DragEvent> dragEvents = observableArrayList();
    @Deprecated private final ObservableList<DragEvent> draggedEvents = observableArrayList();
    @Deprecated private final ObservableList<ZoomEvent> zoomEvents = observableArrayList();
    @Deprecated private final ObservableList<ZoomEvent> zoomedEvents = observableArrayList();

    @Deprecated private final ObservableList<EventHandler<MouseEvent>> mouseClickedEvents = observableArrayList();
    @Deprecated private final ObservableList<EventHandler<MouseEvent>> mouseReleasedEvents = observableArrayList();
    @Deprecated private final ObservableList<EventHandler<MouseEvent>> mouseMovedEvents = observableArrayList();
    //
    @Deprecated private double initDragX, initDragY;

    public MapViewFX() {
        baseLayerProperty = new SimpleObjectProperty<>();
        getStylesheets().add("/de/knaubmaxim/mapfx/css/default-style.css");
        layerPane = new StackPane();
        layerPane.setAlignment(Pos.CENTER);
        getChildren().add(layerPane);
        Rectangle r = new Rectangle(0, 0);
        r.widthProperty().bind(widthProperty());
        r.heightProperty().bind(heightProperty());

        setClip(r);

        this.viewport = new Viewport(this);

        initControls();
        initView();
        initListener();
        initEvents();
        addControl(new ScaleLine());
        baseLayerProperty.setValue(new OpenStreetMapLayer(this));
    }

    private void initControls() {
        NavigationControl navCtrl = new NavigationControl();
        navCtrl.setMapView(MapViewFX.this);
        navCtrl.activate();
        setNavigationControl(navCtrl);
        navigationCtrlProperty().addListener((observable, oldValue, newValue) -> {

        });
    }

    private void initView() {
        getStyleClass().addAll("map", "map-pane");
        setMaxSize(MAX_VALUE, MAX_VALUE);
    }

    private void initListener() {
        widthProperty().addListener((ov, oldVal, newVal) -> {
            layerPane.setMinWidth(newVal.doubleValue());
            layerPane.setPrefWidth(newVal.doubleValue());
            layerPane.setMaxWidth(newVal.doubleValue());
            renderLayers("widthProperty");
        });
        heightProperty().addListener((ov, oldVal, newVal) -> {
            layerPane.setMinHeight(newVal.doubleValue());
            layerPane.setPrefHeight(newVal.doubleValue());
            layerPane.setMaxHeight(newVal.doubleValue());
            renderLayers("heightProperty");
        });
        Rectangle clip = new Rectangle(getWidth(), getHeight());
        layerPane.setClip(clip);
        clip.heightProperty().bind(heightProperty());
        clip.widthProperty().bind(widthProperty());

        baseLayerProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                layerPane.getChildren().remove(oldValue.getView());
            }
            if (newValue != null) {
                layerPane.getChildren().add(0, newValue.getView());
            }
        });

        this.layerList.addListener((ListChangeListener.Change<? extends Layer> c) -> {
            while (c.next()) {
                c.getRemoved().forEach(removedLayer -> {
                    layerPane.getChildren().remove(removedLayer.getView());
                });
                c.getAddedSubList().forEach(newLayer -> {
                    layerPane.getChildren().add(newLayer.getView());
                });
            }
        });

        zoomProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            renderLayers("zoomProperty()");
        });
    }

    private void initEvents() {
        setOnScroll((t) -> {
            Point2D l = getMapPane().sceneToLocal(t.getSceneX(), t.getSceneY());
            double x = l.getX(), y = l.getY();
            getNavigationControl().onZoom(t.getDeltaY(), x, y);
            getControls().stream().filter(c -> c.isActive()).forEach(c -> c.onZoom(t.getDeltaY(), x, y));

            renderLayers("setOnScroll");
        });

        setOnMousePressed((MouseEvent event) -> {
            initDragX = event.getSceneX();
            initDragY = event.getSceneY();
//            mouseClickedEvents.forEach(e -> e.handle(event));
            getControls().stream().filter((Control t) -> t.isActive()).forEach(c -> runLater(() -> c.onMousePressed(event)));
            getNavigationControl().onMousePressed(event);
        });

        setOnMouseReleased((MouseEvent event) -> {
            mouseReleasedEvents.forEach(e -> e.handle(event));
            getControls().stream().filter((Control t) -> t.isActive()).forEach(c -> c.onMouseReleased(event));
            getNavigationControl().onMouseReleased(event);
            renderLayers("OnMouseReleased");
        });
        setOnMouseDragged((MouseEvent event) -> {
            getControls().stream().filter((Control t) -> t.isActive()).forEach(c -> c.onMouseDrag(event));
            getNavigationControl().onMouseDrag(event);
            calculateCenterCoordinate(getBaseLayer());
            Platform.runLater(() -> getBaseLayer().renderRequest());
//            getBaseLayer().renderRequest();
//            layerList.stream().filter((Layer t) -> !(t instanceof VectorLayer)).forEach(Layer::renderRequest);
//            renderLayers("OnMouseDragged");
        });

        setOnMouseClicked((MouseEvent event) -> {
            getNavigationControl().onMouseClicked(event);
            getControls().filtered(f -> f.isActive()).forEach(c -> c.onMouseClicked(event));
        });

        setOnMouseMoved((MouseEvent event) -> {
            getNavigationControl().onMouseMoved(event);
            getControls().stream().filter((Control t) -> t.isActive()).forEach(c -> c.onMouseMoved(event));
//            mouseMovedEvents.forEach(e -> e.handle(event));
        });

        EventDispatcher eventDispatcher = getEventDispatcher();
        setEventDispatcher(new MapFXEventDispatcher(this, eventDispatcher));

        zoomProperty().addListener((ov, oldValue, newValue) -> {
//            mustReRender = false;
//            renderLayers("zoomProperty");
//            calculateCenterCoordinate(getBaseLayer());
//            mustReRender = true;
        });

        centerProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue != null) {
//                centerEvents.forEach(event -> event.handle(newValue.getLongitude(), newValue.getLatitude()));
//                renderLayers("centerProperty");
            }
        });
    }

    protected void renderLayers(String method) {
        getBaseLayer().renderRequest();
        layerList.forEach(Layer::renderRequest);
    }

    @Override public Viewport getViewport() {
        return this.viewport;
    }

    @Override public Node getMapPane() {
        return this;
    }

    ////////////////////  Events  //////////////////////////////////////////////
    @Override public void setOnCenterChanged(CenterEvent event) {
        centerEvents.add(event);
    }

    @Override
    public void setOnDrag(DragEvent event) {
        dragEvents.add(event);
    }

    @Override public void setOnDragged(DragEvent event) {
        draggedEvents.add(event);
    }

    @Override
    public void setOnZoom(MapEvent<ZoomEvent> event) {
//        zoomEvents.add(event);
    }

    @Override
    public void setOnZoomed(ZoomEvent event) {
        zoomedEvents.add(event);
    }

    @Override public void removeOnCenterChanged(CenterEvent event) {
        centerEvents.remove(event);
    }

    @Override public void removeOnDragged(DragEvent event) {
        draggedEvents.remove(event);
    }

    @Override
    public void removeOnZoomed(ZoomEvent event) {
        zoomedEvents.remove(event);
    }

    @Override public void setOnMouseClicked(MouseEvent event) {

    }

    @Override public void setOnMouseReleasedEvents(EventHandler<MouseEvent> event) {
        mouseReleasedEvents.add(event);
    }

    @Override public void setOnMouseMovedEvents(EventHandler<MouseEvent> event) {
        mouseMovedEvents.add(event);
    }

    //
    @Override public void setCenter(double longitude, double latitude) {
        centerProperty().setValue(new Coordinate(longitude, latitude));
    }

    @Override
    public void setCenter(Coordinate coord) {
        getNavigationControl().onCenter(coord);
        centerProperty().setValue(coord);
    }

    @Override public Coordinate getCenter() {
        return centerProperty().getValue();
    }

    @Override public Bound getBounds() {
        Coordinate upperLeft = point2Coord(0, 0);
        Coordinate lowerRight = point2Coord(getWidth(), getHeight());
        return new Bound(upperLeft.getLatitude(), lowerRight.getLongitude(), lowerRight.getLatitude(), upperLeft.getLongitude());
    }

    @Override public SimpleObjectProperty<Coordinate> centerProperty() {
        return center;
    }

    @Override public void moveTo(double longitude, double latitude) {
        centerProperty().setValue(new Coordinate(longitude, latitude));
    }

    @Override public void moveTo(Coordinate coordinate) {
        centerProperty().setValue(coordinate);
    }

    @Override public void zoomIn() {
        getNavigationControl().zoomIn();
        getControls().stream().filter(c -> c.isActive()).forEach(c -> c.zoomIn());
        renderLayers("zoomIn");
//        setZoom(zoomProperty().getValue() + getZoomStep());
    }

    @Override public void zoomOut() {
        getNavigationControl().zoomOut();
        getControls().stream().filter(c -> c.isActive()).forEach(c -> c.zoomOut());
        renderLayers("zoomOut");
//        setZoom(zoomProperty().getValue() - getZoomStep());
    }

    @Override public void setZoom(double zoom) {
        Coordinate oldCenter = getCenter();
        getNavigationControl().onZoom(zoom);
        setCenter(oldCenter);
        getControls().stream().filter((Control t) -> t.isActive()).forEach(c -> c.onZoom(zoom));
        renderLayers("");
    }

    @Override public double getZoom() {
        return zoomProperty().getValue();
    }

    @Override public DoubleProperty zoomProperty() {
        return viewport.zoomProperty();
    }

    @Override public DoubleProperty zoomStepProperty() {
        return viewport.zoomStepProperty();
    }

    @Override public double getZoomStep() {
        return viewport.getZoomStep();
    }

    @Override public void setZoomStep(double zoomStep) {
        this.viewport.setZoomStep(zoomStep);
    }

    ///// Layer ////////////////////////////////////////////////////////////////
    @Override public void addLayer(Layer layer) {
        layerList.add(layer);
        layer.renderRequest();
    }

    @Override public void addLayer(int index, Layer layer) {
        layerList.add(index, layer);
        layer.renderRequest();
    }

    @Override public void addLayer(Layer... layers) {
        asList(layers).forEach(layer -> this.addLayer(layerList));
    }

    @Override public void addLayer(Collection<Layer> layers) {
        layers.forEach(layer -> this.addLayer(layer));
    }

    @Override public Layer getLayer(int index) {
        return layerList.get(index);
    }

    @Override public ObservableList<Layer> getLayers() {
        return layerList;
    }

    @Override public ObjectProperty<TileLayer> baseLayerProperty() {
        return baseLayerProperty;
    }

    @Override public TileLayer getBaseLayer() {
        return baseLayerProperty.getValue();
    }

    private boolean hasBaseLayer() {
        return this.baseLayerProperty != null;
    }

    @Override public void setBaseLayer(TileLayer layer) {
        this.baseLayerProperty.setValue(layer);
    }

    @Override public void removeLayer(int index) {
        layerList.remove(index);
    }

    @Override public void removeLayer(Layer layer) {
        layerList.remove(layer);
    }

    @Override public void removeLayer(Layer... layers) {
        layerList.removeAll(layers);
    }

    @Override public void removeLayer(Collection<Layer> layers) {
        layerList.removeAll(layers);
    }

    @Override public void setLayerIndex(Layer layer, int index) {
        layerList.remove(layer);
        layerList.add(index, layer);
    }

    @Override public Bound getMapPoint(Bound bound) {
        Point2D leftTop = getMapPoint(bound.getLeft(), bound.getTop());
        Point2D rightBottom = getMapPoint(bound.getRight(), bound.getBottom());
        return new Bound(leftTop.getY(), rightBottom.getX(), rightBottom.getY(), leftTop.getX());
    }

    @Override public Point2D getMapPoint(Coordinate coordinate) {
        return getMapPoint(coordinate.getLongitude(), coordinate.getLatitude());
    }

    @Override public Point2D getMapPoint(double lon, double lat) {
        return getMapPoint(zoomProperty().getValue(), lon, lat);
    }

    @Override public Point2D getMapPoint(double zoom, double lon, double lat) {
        return CoordinateUtils.coord2Point(viewport, lon, lat);
//        double n = pow(2, zoom);
//        double latRad = PI * lat / 180;
//        double xD = n / 360.0 * (180.0 + lon);
//        double yD = n * (1.0 - (log(tan(latRad) + 1 / cos(latRad)) / PI)) / 2;
//        double mex = xD * 256.0;
//        double mey = yD * 256.0;
//        double x = getBaseLayer().getTranslateX() + mex;
//        double y = getBaseLayer().getTranslateY() + mey;
//        return new Point2D(x, y);
    }

    @Override public Coordinate point2Coord(double x, double y) {
        return CoordinateUtils.point2Coord(viewport, x, y);
//        if (getScene() == null) {
//            return new Coordinate(0, 0);
//        }
//
//        Point2D localToScene = localToScene(0, 0);
//        x = x - getBaseLayer().getTranslateX() - localToScene.getX();
//        y = y - getBaseLayer().getTranslateY() - localToScene.getY();
//        double z = zoomProperty().getValue();
//        double latrad = PI - (2.0 * PI * y) / (pow(2, z) * 256.);
//        double mlat = toDegrees(atan(sinh(latrad)));
//        double mlon = x / (256 * pow(2, z)) * 360 - 180;
//        return new Coordinate(mlon, mlat);
    }

    /**
     * zur test zwecken public
     *
     */
    private void calculateCenterCoordinate(Layer layer) {
        if (layer == null) {
            return;
        }
        double z = zoomProperty().getValue();
        double x = getWidth() / 2 - layer.getView().getTranslateX();
        double y = getHeight() / 2 - layer.getView().getTranslateY();
        double latRad = PI - (2.0 * PI * y) / (pow(2, z) * 256.0);
        double mLat = toDegrees(atan(sinh(latRad)));
        double mLon = x / (256 * pow(2, z)) * 360 - 180;
        centerProperty().setValue(new Coordinate(mLon, mLat));
    }

    ///// Control //////////////////////////////////////////////////////////////
    public SimpleObjectProperty<NavigationControl> navigationCtrlProperty() {
        return navigationControl;
    }

    @Override public NavigationControl getNavigationControl() {
        return this.navigationControl.getValue();
    }

    @Override public void setNavigationControl(NavigationControl navigationControl) {
        this.navigationControl.setValue(navigationControl);
    }

    @Override public void addControl(Control control) {
        control.setMapView(this);
        controlList.add(control);
    }

    @Override public void addControl(Control... controls) {
        for (Control control : controls) {
            control.setMapView(this);
        }
        controlList.addAll(controls);
    }

    @Override public void addControl(Collection<Control> controls) {
        controls.stream().forEach((control) -> {
            control.setMapView(this);
        });
        controlList.addAll(controls);
    }

    @Override public Control getControl(int index) {
        return controlList.get(index);
    }

    @Override public ObservableList<Control> getControls() {
        return controlList;
    }

    @Override public void removeControl(int index) {
        controlList.remove(index);
    }

    @Override public void removeControl(Control control) {
        controlList.remove(control);
    }

    @Override public void removeControl(Control... controls) {
        controlList.removeAll(controls);
    }

    @Override public void removeControl(Collection<Control> controls) {
        controlList.removeAll(controls);
    }

}
