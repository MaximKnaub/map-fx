package de.mknaub.mapfx.events;

/**
 *
 * @author Maxim
 */
public interface MapEvent<T> {

    void handle(T event);

}
