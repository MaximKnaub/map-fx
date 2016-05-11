package de.knaubmaxim.mapfx.layer.vector.features;

import de.knaubmaxim.mapfx.MapView;
import de.knaubmaxim.mapfx.coord.Bound;
import de.knaubmaxim.mapfx.layer.vector.VectorLayer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author maka
 */
public interface Drawable {

    MapView getMapView();

    VectorLayer getLayer();

    void setLayer(VectorLayer vectorLayer);

    void setMapView(MapView mapView);

    void calculate(MapView mapView);

    void draw(GraphicsContext context);

    Color getFillColor();

    void setFillColor(Color color);

    void setFillColor(int r, int g, int b);

    void setFillColor(int r, int g, int b, double a);

    Color getStrokeColor();

    void setStrokeColor(Color color);

    void setStrokeColor(int r, int g, int b);

    void setStrokeColor(int r, int g, int b, double a);

    double getStrokeWidth();

    void setStrokeWidth(double width);

    void setOpacity(double opacity);

    double getOpacity();

    boolean isOnScreen(Bound bound);

    SimpleBooleanProperty enabledProperty();

    boolean isEnabled();

    void setEnabled(boolean enabled);

}
