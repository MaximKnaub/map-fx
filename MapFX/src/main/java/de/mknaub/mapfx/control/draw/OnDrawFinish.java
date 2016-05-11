package de.knaubmaxim.mapfx.control.draw;

import de.knaubmaxim.mapfx.layer.vector.features.BasicFeature;

/**
 *
 * @author maka
 * @param <T>
 */
public interface OnDrawFinish<T extends BasicFeature> {

    void handle(T feature);
}
