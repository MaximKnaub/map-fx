package de.knaubmaxim.mapfx.layer.tileprovider;

import de.knaubmaxim.mapfx.MapView;
import de.knaubmaxim.mapfx.layer.TileLayer;
import de.knaubmaxim.mapfx.layer.tile.ProviderTile;
import de.knaubmaxim.mapfx.layer.tile.TileType;
import java.util.Optional;
import javafx.beans.property.SimpleObjectProperty;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;

/**
 *
 * @author MaKa
 */
public abstract class TileProvider extends TileLayer {

    private final SimpleObjectProperty<TileType> tileType = new SimpleObjectProperty<>();
    private final ObservableList<TileType> supportedTileTypes = observableArrayList();

    public TileProvider(MapView mapView) {
        super(mapView);
        setUpMap();
        tileType.addListener(e -> clearTiles());
        setTileFactory((TileMetaData param) -> Optional.of(new ProviderTile(param)));
    }

    private void setUpMap() {
        initTileTypes();
        if (supportedTileTypes.isEmpty() == false) {
            tileType.setValue(supportedTileTypes.get(0));
        }
    }

    abstract protected void initTileTypes();

    public TileType getTileType() {
        return this.tileType.getValue();
    }

    public void selectTileType(TileType tileType) {
        this.tileType.setValue(tileType);
    }

    public ObservableList<TileType> getSupportedTileTypes() {
        return supportedTileTypes;
    }

    public String getBaseUrl() {
        return getTileType().getBaseURL();
    }

    public String getURLPostFix() {
        return getTileType().getURLPostFix();
    }

}
