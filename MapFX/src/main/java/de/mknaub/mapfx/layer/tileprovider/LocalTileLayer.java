package de.mknaub.mapfx.layer.tileprovider;

import de.mknaub.mapfx.MapView;
import de.mknaub.mapfx.layer.tile.MapTileType;

/**
 *
 * @author maka
 */
public class LocalTileLayer extends TileProvider {

    public LocalTileLayer(MapView mapView) {
        super(mapView);
    }

    @Override public String getName() {
        return "Local Tiles";
    }

    @Override protected void initTileTypes() {
        getSupportedTileTypes().add(new MapTileType("Map", "file:///D:/tiles/osm", ""));
    }
}
