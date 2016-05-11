package de.knaubmaxim.mapfx.events;

/**
 *
 * @author Maxim
 */
public interface MapEvent<T> {

    void handle(T event);

}
