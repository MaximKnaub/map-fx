package de.mknaub.mapfx.events.tile;

import de.mknaub.mapfx.layer.tile.Tile;

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
