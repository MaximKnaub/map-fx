package de.knaubmaxim.mapfx.layer.tileprovider;

import de.knaubmaxim.mapfx.MapView;
import de.knaubmaxim.mapfx.layer.tile.MapTileType;

/**
 *
 * @author maka
 */
public class OpenStreetMapLayer extends TileProvider {

    public OpenStreetMapLayer(MapView mapView) {
        super(mapView);
    }

    @Override public String getName() {
        return "OpenStreetMap";
    }

    @Override protected void initTileTypes() {
        getSupportedTileTypes().add(new MapTileType("Map", "http://tile.openstreetmap.org", "© OpenStreetMap contributors"));
//        MapTileType m = new MapTileType("Map", "https://b.tiles.mapbox.com/v4/pinterest.md5kx1or", "© OpenStreetMap contributors");
//        m.setURLPostFix("?access_token=pk.eyJ1IjoicGludGVyZXN0IiwiYSI6ImdQSE45XzgifQ.4HhVEoWGNG2ZXxTC1x56kQ");
//        getSupportedTileTypes().add(m);
    }
}
