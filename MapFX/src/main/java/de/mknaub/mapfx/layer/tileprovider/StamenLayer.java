package de.mknaub.mapfx.layer.tileprovider;

import de.mknaub.mapfx.MapView;
import de.mknaub.mapfx.layer.tile.MapTileType;

/**
 *
 * @author maka
 */
public class StamenLayer extends TileProvider {

    public StamenLayer(MapView mapView) {
        super(mapView);
    }

    @Override
    public String getName() {
        return "Stamen";
    }
    @Override protected void initTileTypes() {
        getSupportedTileTypes().add(new MapTileType("Watercolor", "http://tile.stamen.com/watercolor", "Map tiles by Stamen Design, under CC BY 3.0. Data by OpenStreetMap, under CC BY SA."));
        getSupportedTileTypes().add(new MapTileType("Toner", "http://tile.stamen.com/toner", "Map tiles by Stamen Design, under CC BY 3.0. Data by OpenStreetMap, under CC BY SA."));
        getSupportedTileTypes().add(new MapTileType("Terrain", "http://tile.stamen.com/terrain", "Map tiles by Stamen Design, under CC BY 3.0. Data by OpenStreetMap, under CC BY SA."));
    }
}
