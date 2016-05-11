package de.knaubmaxim.mapfx.events.tile;

import de.knaubmaxim.mapfx.layer.tile.Tile;

/**
 *
 * @author Maxim
 */
public class TileEvent {

    private final Tile tile;

    public TileEvent(Tile tile) {
        this.tile = tile;
    }

    public Tile getTile() {
        return tile;
    }
}
