package de.knaubmaxim.mapfx.layer.tileprovider;

import de.knaubmaxim.mapfx.MapView;
import de.knaubmaxim.mapfx.layer.tile.MapTileType;

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
