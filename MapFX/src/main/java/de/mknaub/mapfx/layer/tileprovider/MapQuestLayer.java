package de.mknaub.mapfx.layer.tileprovider;

import de.mknaub.mapfx.MapView;
import de.mknaub.mapfx.layer.tile.MapTileType;

/**
 *
 * @author Maxim
 */
public class MapQuestLayer extends TileProvider {

    public MapQuestLayer(MapView mapView) {
        super(mapView);
    }

    @Override public String getName() {
        return "Map Quest";
    }
    @Override protected void initTileTypes() {
        getSupportedTileTypes().add(new MapTileType("Map", "http://otile1.mqcdn.com/tiles/1.0.0/map", "© OpenStreetMap contributors"));
        getSupportedTileTypes().add(new MapTileType("Satellite", "http://otile1.mqcdn.com/tiles/1.0.0/sat", "Portions Courtesy NASA/JPL-Caltech and U.S. Depart. of Agriculture, Farm Service Agency"));
    }
}
