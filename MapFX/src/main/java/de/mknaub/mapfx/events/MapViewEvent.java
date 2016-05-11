package de.mknaub.mapfx.events;

import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Maxim
 */
public interface MapViewEvent {

    void onMouseDragEntered(MouseDragEvent mouseEvent);

    void onMouseDragExited(MouseDragEvent mouseEvent);

    void onMouseDragOver(MouseDragEvent mouseEvent);

    void onMouseDragReleased(MouseDragEvent mouseEvent);

    void onMouseClicked(MouseEvent mouseEvent);

    void onMouseDragged(MouseEvent mouseEvent);

    void onMouseEntered(MouseEvent mouseEvent);

    void onMouseExited(MouseEvent mouseEvent);

    void onMouseMoved(MouseEvent mouseEvent);

    void onMousePressed(MouseEvent mouseEvent);

    void onMouseReleased(MouseEvent mouseEvent);

}
