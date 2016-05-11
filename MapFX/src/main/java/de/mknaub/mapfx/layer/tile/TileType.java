package de.knaubmaxim.mapfx.layer.tile;

import javafx.beans.property.ReadOnlyStringProperty;

/**
 *
 * @author maka
 */
public interface TileType {

    public String getTypeName();

    public ReadOnlyStringProperty typeNameProperty();

    public String getBaseURL();

    public String getURLPostFix();

    public void setURLPostFix(String postFix);

    public String getAttributionNotice();
}
