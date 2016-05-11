package de.mknaub.mapfx.events;

import de.mknaub.mapfx.MapView;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 *
 * @author maka
 */
public class MapFXEventDispatcher implements EventDispatcher {

    private final EventDispatcher originalDispatcher;
    private final MapView mapView;

    public MapFXEventDispatcher(MapView mapView, EventDispatcher originalDispatcher) {
        this.mapView = mapView;
        this.originalDispatcher = originalDispatcher;
    }

    @Override
    public Event dispatchEvent(Event event, EventDispatchChain tail) {
        if (event instanceof MouseEvent) {

        }
        if (event instanceof ScrollEvent) {
        }
        return originalDispatcher.dispatchEvent(event, tail);
    }

}
