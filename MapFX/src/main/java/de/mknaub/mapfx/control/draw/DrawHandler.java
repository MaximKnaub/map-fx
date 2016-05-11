package de.mknaub.mapfx.control.draw;

import de.mknaub.mapfx.layer.vector.features.BasicFeature;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author maka
 */
public interface DrawHandler {

    void setDrawControl(DrawControl drawControl);

    void onMouseClicked(MouseEvent event);

    void onMousePressed(MouseEvent event);

    void onMouseReleased(MouseEvent event);

    void onMouseMoved(MouseEvent event);

    void onMouseDrag(MouseEvent event);

    BasicFeature getFeature();
}
