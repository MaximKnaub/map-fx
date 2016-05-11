package de.mknaub.mapfx.layer.tile;

import de.mknaub.mapfx.coord.Bound;
import de.mknaub.mapfx.coord.Coordinate;
import static de.mknaub.mapfx.coord.CoordinateUtils.point2Coord;
import de.mknaub.mapfx.layer.TileLayer.TileMetaData;
import static java.lang.Math.floor;
import static java.lang.Math.pow;
import java.util.LinkedList;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.scene.layout.Region;
import javafx.scene.transform.Scale;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author maka
 */
@Slf4j
public class Tile extends Region {

    protected final TileMetaData metaData;
    protected final List<Tile> covering = new LinkedList<>();
    private final Scale scale;
    private final InvalidationListener zl;
    protected Tile parentTile;
    @Getter private final Bound bound;

    public Tile(TileMetaData metaData) {
        this.metaData = metaData;
        this.scale = new Scale();
        scale.setPivotX(0);
        scale.setPivotY(0);
        getTransforms().add(scale);
        Coordinate topLeft = point2Coord(getZ(), getX() * 256, getY() * 256);
        Coordinate bottomRight = point2Coord(getZ(), getX() * 256 + 256, getY() * 256 + 256);
        this.bound = new Bound(topLeft.getLatitude(), bottomRight.getLongitude(), bottomRight.getLatitude(), topLeft.getLongitude());

        parentTile = metaData.getTileLayer().findCovering(getZ(), getX(), getY());
        if (parentTile != null) {
            parentTile.addCovering(Tile.this);
        }

        zl = o -> calculatePosition();

        metaData.getTileLayer().getMapView().zoomProperty().addListener(new WeakInvalidationListener(zl));
        calculatePosition();
    }

    public final int getZ() {
        return this.metaData.getZ();
    }

    public final long getX() {
        return this.metaData.getX();
    }

    public final long getY() {
        return this.metaData.getY();
    }

    public void addCovering(Tile tile) {
        covering.add(tile);
        setVisible(true);
    }

    public <T extends Tile> void removeCovering(T tile) {
        covering.remove(tile);
        calculatePosition();
    }

    public Tile getCoveringTile() {
        return parentTile;
    }

    public boolean isCovering() {
        return covering.size() > 0;
    }

    @Override public String toString() {
        return "BaseLayerTile{" + "zoom=" + getZ() + ", x=" + getX() + ", y=" + getY() + '}';
    }

    private void calculatePosition() {
        double currentZoom = metaData.getTileLayer().getMapView().zoomProperty().getValue();
        int visibileWindow = (int) floor(currentZoom + metaData.getTileLayer().getMapView().getZoomStep());
        int zoom = metaData.getZ();
        if ((visibileWindow == zoom)
                || isCovering()
                || (visibileWindow >= metaData.getTileLayer().getMaxZoom()
                && zoom == metaData.getTileLayer().getMaxZoom() - 1)) {
            this.setVisible(true);
        } else {
            this.setVisible(false);
        }
        double sf = pow(2, currentZoom - zoom);
        scale.setX(sf);
        scale.setY(sf);
        setTranslateX(256 * metaData.getX() * sf);
        setTranslateY(256 * metaData.getY() * sf);
    }
}
