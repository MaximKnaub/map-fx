package de.mknaub.mapfx.layer.tile;

import de.mknaub.mapfx.layer.TileLayer.TileMetaData;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import lombok.Getter;

/**
 *
 * @author MaKa
 */
public class CanvasTile extends Tile {

    @Getter protected final Canvas canvas;

    public CanvasTile(TileMetaData metaData) {
        super(metaData);
        canvas = new Canvas(256, 256);
        init();
    }

    private void init() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFont(new Font(10));
        getChildren().add(canvas);
    }
}
