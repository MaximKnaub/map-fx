package de.mknaub.mapfx.layer.tile;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author maka
 */
public class MapTileType implements TileType {

    private final StringProperty name = new SimpleStringProperty("");
    private final String baseURL;
    private final String attributionNotice;
    private String urlPostFix = "";

    public MapTileType(String name, String baseURL, String attributionNotice) {
        this.name.setValue(name);
        this.baseURL = baseURL;
        this.attributionNotice = attributionNotice;
    }

    @Override public String getTypeName() {
        return name.getValue();
    }

    @Override public ReadOnlyStringProperty typeNameProperty() {
        return name;
    }

    @Override public String getBaseURL() {
        return baseURL;
    }

    @Override public void setURLPostFix(String postFix) {
        this.urlPostFix = postFix;
    }

    @Override public String getURLPostFix() {
        return urlPostFix;
    }

    @Override
    public String getAttributionNotice() {
        return attributionNotice;
    }
}
