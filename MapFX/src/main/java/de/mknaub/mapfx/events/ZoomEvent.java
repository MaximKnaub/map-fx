package de.mknaub.mapfx.events;

import lombok.Getter;

/**
 *
 * @author maka
 */
public class ZoomEvent {

    @Getter private final double oldValue, newValue;

    public ZoomEvent(double oldValue, double newValue) {
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

}
