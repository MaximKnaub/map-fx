package de.mknaub.mapfx.layer;

import javafx.scene.Node;

/**
 *
 * @author maka
 */
public interface ILayer {

    Node getView();

    String getName();

    void setName(String name);

    boolean isBaseLayer();

    void setBaseLayer(boolean isBaseLayer);

    void setVisible(boolean visible);

    double getTranslateX();

    double getTranslateY();

}
