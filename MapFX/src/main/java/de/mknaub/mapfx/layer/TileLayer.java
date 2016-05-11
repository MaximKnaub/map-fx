package de.mknaub.mapfx.layer;

import de.mknaub.mapfx.MapView;
import de.mknaub.mapfx.coord.Bound;
import de.mknaub.mapfx.coord.Coordinate;
import de.mknaub.mapfx.layer.tile.Tile;
import static java.lang.Double.MAX_VALUE;
import static java.lang.Math.PI;
import static java.lang.Math.ceil;
import static java.lang.Math.cos;
import static java.lang.Math.floor;
import static java.lang.Math.log;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.tan;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author maka
 */
@Slf4j
public abstract class TileLayer extends Layer {

    protected final Pane layerPane;
    protected final Map<Long, SoftReference<Tile>>[] tiles = new HashMap[(int) getMaxZoom()];
    private final Rectangle area;
    private final ObjectProperty<Callback<TileMetaData, Optional<? extends Tile>>> tileFactory
            = new SimpleObjectProperty<>((Callback<TileMetaData, Optional<? extends Tile>>) (metaData) -> Optional.of(new Tile(metaData)));
    private final ObjectProperty<Bound> layerBound = new ReadOnlyObjectWrapper<>(new Bound(90, 180, -90, -180));

    public TileLayer(MapView mapView) {
        super(mapView);
        layerPane = new Pane();

        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = new HashMap<>();
        }

        this.area = new Rectangle(0, 0, MAX_VALUE, MAX_VALUE);
        this.area.setVisible(false);
        area.translateXProperty().bind(layerPane.translateXProperty().multiply(-1));
        area.translateYProperty().bind(layerPane.translateYProperty().multiply(-1));
        area.widthProperty().bind(layerPane.widthProperty());
        area.heightProperty().bind(layerPane.heightProperty());

        initListener();
    }

    private void initListener() {
        layerPane.sceneProperty().addListener(i -> {
            if (layerPane.getScene() != null) {
                layerPane.getScene().windowProperty().addListener(e -> {
                    layerPane.getScene().getWindow().showingProperty().addListener(s -> {
                        setCenter(getMapView().getCenter());
                        loadTiles();
                    });
                });
            }
        });
    }

    public ObjectProperty<Callback<TileMetaData, Optional<? extends Tile>>> tileFactoryProperty() {
        return tileFactory;
    }

    public Callback<TileMetaData, Optional<? extends Tile>> getTileFactory() {
        return tileFactory.getValue();
    }

    public void setTileFactory(Callback<TileMetaData, Optional<? extends Tile>> tileFactory) {
        this.tileFactory.setValue(tileFactory);
    }

    public void setCenter(Coordinate coord) {
        setCenter(coord.getLongitude(), coord.getLatitude());
    }

    protected void setCenter(double longitude, double latitude) {
        double activeZoom = zoomProperty().getValue();
        double n = pow(2, activeZoom);
        double lat_rad = PI * latitude / 180;
        double id = n / 360.0 * (180.0 + longitude);
        double jd = n * (1 - (log(tan(lat_rad) + 1 / cos(lat_rad)) / PI)) / 2;
        double mex = (double) id * 256;
        double mey = (double) jd * 256;
        double ttx = mex - mapView.getValue().getWidth() / 2;
        double tty = mey - mapView.getValue().getHeight() / 2;
        layerPane.setTranslateX(-1 * ttx);
        layerPane.setTranslateY(-1 * tty);
    }

    protected Coordinate getCenter() {
        return getMapView().getCenter();
    }

    @Override public void move(double x, double y) {
        moveX(x);
        moveY(y);
    }

    @Override public void moveX(double x) {
        layerPane.setTranslateX(layerPane.getTranslateX() - x);
    }

    @Override public void moveY(double y) {
        layerPane.setTranslateY(layerPane.getTranslateY() - y);
    }

    public DoubleProperty translateXProperty() {
        return layerPane.translateXProperty();
    }

    public DoubleProperty translateYProperty() {
        return layerPane.translateYProperty();
    }

    @Override public double getTranslateX() {
        return translateXProperty().getValue();
    }

    @Override public double getTranslateY() {
        return translateYProperty().getValue();
    }

    @Override public Node getView() {
        return layerPane;
    }

    @Override public void renderRequest() {
        loadTiles();
    }

    protected DoubleProperty zoomProperty() {
        return getMapView().zoomProperty();
    }

    @Override public void setZoom(double zoom) {
        double currentZoom = getMapView().getZoom();
        double index = currentZoom - zoom;
        int zoomDirection = zoom < currentZoom ? -1 : 1;
        index *= index < 0 ? -1 : 1;
        for (; index >= 0; index -= getMapView().getZoomStep()) {
            zoom(zoomDirection, 0, 0);
        }
    }

    @Override public void zoom(double delta, double pivotX, double pivotY) {
        double zOffset = getMapView().getZoomStep();
        double dz = delta > 0 ? zOffset : zOffset * -1;
        double zp = zoomProperty().getValue();

        double txOld = layerPane.getTranslateX();
        double t1x = pivotX - txOld;
        double t2x = 1.0 - pow(2, dz);
        double toTx = t1x * t2x;
        double tx = txOld + toTx;

        double tyOld = layerPane.getTranslateY();
        double t1y = pivotY - tyOld;
        double t2y = 1.0 - pow(2, dz);
        double toTy = t1y * t2y;
        double ty = tyOld + toTy;

        if (delta > 0) {
            if (zp < getMaxZoom() + zOffset) {
                layerPane.setTranslateX(tx);
                layerPane.setTranslateY(ty);
            }
        } else if (zp > getMinZoom() - zOffset) {
            double nz = zp - zOffset;
            layerPane.setTranslateX(tx);
            layerPane.setTranslateY(ty);
        }
        loadTiles();
    }

    private void loadTiles() {
        double activeZoom = zoomProperty().getValue();
        int nearestZoom = (min((int) floor(getMapView().getZoom() + getMapView().getZoomStep()), (int) getMapView().getViewport().getMaxZoom() - 1));
        double deltaZ = nearestZoom - activeZoom;
        long i_max = 1 << nearestZoom;
        long j_max = 1 << nearestZoom;
        double tX = layerPane.getTranslateX();
        double tY = layerPane.getTranslateY();
        double width = getMapView().getWidth();
        double height = getMapView().getHeight();
        long iMin = max(0, (long) (-tX * pow(2, deltaZ) / 256) - 1);
        long jMin = max(0, (long) (-tY * pow(2, deltaZ) / 256));
        long iMax = min(i_max, iMin + (long) (width * pow(2, deltaZ) / 256) + 3);
        long jMax = min(j_max, jMin + (long) (height * pow(2, deltaZ) / 256) + 3);
        for (long tileX = iMin; tileX < iMax; tileX++) {
            for (long tileY = jMin; tileY < jMax; tileY++) {
                Long key = tileX * i_max + tileY;
                SoftReference<Tile> ref = tiles[nearestZoom].get(key);
                if ((ref == null) || (ref.get() == null)) {
                    getTileFactory().call(new TileMetaData(this, nearestZoom, tileX, tileY)).ifPresent(tile -> {
                        if (getLayerBound().isInside(tile.getBound()) || getLayerBound().intersects(tile.getBound())) {
                            tiles[nearestZoom].put(key, new SoftReference<>(tile));
                            Tile covering = tile.getCoveringTile();
                            if (covering != null) {
                                if (layerPane.getChildren().contains(covering) == false) {
                                    layerPane.getChildren().add(covering);
                                }
                            }
                            layerPane.getChildren().add(tile);
                        }
                    });
                } else {
                    Tile tile = ref.get();
                    if (layerPane.getChildren().contains(tile) == false) {
                        layerPane.getChildren().add(tile);
                    }
                }
            }
        }
        cleanupTiles();
    }

    public Tile findCovering(int zoom, long x, long y) {
        while (zoom > 0) {
            zoom--;
            x = x / 2;
            y = y / 2;
            Tile tile = findTile(zoom, x, y);
            if ((tile != null)) {
                return tile;
            }
        }
        return null;
    }

    private Tile findTile(int zoom, long x, long y) {
        Long key = x * (1 << zoom) + y;
        SoftReference<Tile> tile = tiles[zoom].get(key);
        return tile == null ? null : tile.get();
    }

    private void cleanupTiles() {
        double zp = zoomProperty().getValue();
        List<Tile> toRemove = new LinkedList<>();
        ObservableList<Node> children = layerPane.getChildren();
        children.filtered((node) -> node instanceof Tile).forEach(child -> {
            Tile tile = (Tile) child;
            boolean intersects = tile.getBoundsInParent().intersects(area.getBoundsInParent());
            if (intersects == false) {
                toRemove.add(tile);
            } else if (tile.getZ() > ceil(zp)) {
                toRemove.add(tile);
            } else if (tile.getZ() < floor(zp + getMapView().getZoomStep())
                    && tile.isCovering() == false
                    && ceil(zp) >= getMaxZoom() == false) {
                toRemove.add(tile);
            }
        });
        layerPane.getChildren().removeAll(toRemove);
    }

    protected void clearTiles() {
        List<Node> toRemove = new ArrayList<>();
        layerPane.getChildren().stream().filter(e -> e instanceof Tile).forEach(child -> {
            toRemove.add(child);
        });
        layerPane.getChildren().removeAll(toRemove);

        for (Map<Long, SoftReference<Tile>> tile : tiles) {
            tile.clear();
        }
    }

    public double getMaxZoom() {
        return getMapView().getViewport().getMaxZoom();
    }

    public double getMinZoom() {
        return getMapView().getViewport().getMinZoom();
    }

    // other functions
    public void setEffect(Effect effect) {
        layerPane.setEffect(effect);
    }

    public void effectProperty(Effect effect) {
        layerPane.effectProperty();
    }

    public Bound getLayerBound() {
        return layerBound.get();
    }

    public void setLayerBound(Bound value) {
        layerBound.set(value);
    }

    public ObjectProperty layerBoundProperty() {
        return layerBound;
    }

    @Data public static class TileMetaData {

        private TileLayer tileLayer;
        private int z;
        private long x, y;

        public TileMetaData() {
        }

        public TileMetaData(TileLayer tileLayer, int z, long x, long y) {
            this.tileLayer = tileLayer;
            this.z = z;
            this.x = x;
            this.y = y;
        }

        @Override public String toString() {
            return "TileMetaData{" + "tileLayer=" + tileLayer.getName() + ", z=" + z + ", x=" + x + ", y=" + y + '}';
        }
    }
}
