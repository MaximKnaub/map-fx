package de.mknaub.mapfx.control.draw;

import de.mknaub.mapfx.layer.vector.features.BasicFeature;

/**
 *
 * @author maka
 * @param <T>
 */
public interface OnDrawFinish<T extends BasicFeature> {

    void handle(T feature);
}
