package de.mknaub.mapfx.events;

import javafx.scene.input.MouseEvent;

/**
 *
 * @author maka
 */
public interface DragEvent {

    void handle(MouseEvent event, double initDragX, double initDragY);
}
