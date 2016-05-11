package de.mknaub.mapfx.layer.tile;

import de.mknaub.mapfx.events.MapEvent;
import de.mknaub.mapfx.events.tile.TileEvent;
import de.mknaub.mapfx.layer.TileLayer.TileMetaData;
import de.mknaub.mapfx.layer.tileprovider.TileProvider;
import static java.lang.String.format;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author MaKa
 */
public class ProviderTile extends Tile {

    private final Image image;
    private final BooleanProperty loading = new SimpleBooleanProperty();
    // events
    private MapEvent<TileEvent> onTileLoadedEvent;

    public ProviderTile(TileMetaData metaData) {
        super(metaData);
        
        image = new Image(format("%s/%s/%s/%s%s",
                ((TileProvider)metaData.getTileLayer()).getBaseUrl(),
                metaData.getZ(),
                metaData.getX(),
                metaData.getY(),
                ".png" + ((TileProvider)metaData.getTileLayer()).getURLPostFix()), true);

        ChangeListener<Number> listener = new ChangeListener<Number>() {
            ChangeListener<Number> instance = this;

            @Override public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal) {
                loading.setValue(newVal.doubleValue() < 1.);
                if (newVal.doubleValue() >= 1.) {
                    image.progressProperty().removeListener(instance);
                    if (parentTile != null) {
                        removeCovering(parentTile);
                    }
                    MapEvent<TileEvent> onTileLoaded = getOnTileLoadedEvent();
                    if (onTileLoaded != null) {
                        onTileLoaded.handle(new TileEvent(ProviderTile.this));
                    }
                }
            }
        };
        image.progressProperty().addListener(listener);

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(256);
        imageView.setFitHeight(256);
        getChildren().addAll(imageView);

    }

    public boolean isLoading() {
        return loading.getValue();
    }

    //<editor-fold defaultstate="collapsed" desc="EVENT_HANDLING">
    public void setOnTileLoaded(MapEvent<TileEvent> tileEvent) {
        this.onTileLoadedEvent = tileEvent;
    }

    public MapEvent<TileEvent> getOnTileLoadedEvent() {
        return this.onTileLoadedEvent;
    }
    //</editor-fold>
}
