package de.mknaub.mapfx;

import static java.lang.Math.pow;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;

/**
 *
 * @author MaKa
 */
public class Viewport {

    private final MapView mapView;

    private final DoubleProperty longitude = new SimpleDoubleProperty(0d);
    private final DoubleProperty latitude = new SimpleDoubleProperty(0d);
    private final Extent extent = new Extent(0, 0, 748, 655);
    private final DoubleProperty zoom = new SimpleDoubleProperty(0);
    private final DoubleProperty zoomStep = new SimpleDoubleProperty(0.1);
    private final DoubleProperty minZoom = new SimpleDoubleProperty(0);
    private final DoubleProperty maxZoom = new SimpleDoubleProperty(21);

    public Viewport(MapView mapView) {
        this.mapView = mapView;

        init();
    }
    
    private void init(){
        ChangeListener<Number> cl = (observable, oldVal, newVal) -> {
            calcCulateExtent();
        };
        this.mapView.widthProperty().addListener(cl);
        this.mapView.heightProperty().addListener(cl);
    }
    private void calcCulateExtent(){
        double w = mapView.getWidth(), h = mapView.getHeight();
        double z = mapView.getZoom();
        double s =(pow(2, z) * 256.);
//        CoordinateUtils.coord2Point(this, null)
    }

    public Extent getExtent() {
        return extent;
    }

    public double getZoom() {
        return zoom.get();
    }

    public void setZoom(double value) {
        zoom.set(value);
    }

    public DoubleProperty zoomProperty() {
        return zoom;
    }

    public double getZoomStep() {
        return zoomStep.get();
    }

    public void setZoomStep(double value) {
        zoomStep.set(value);
    }

    public DoubleProperty zoomStepProperty() {
        return zoomStep;
    }

    public double getMinZoom() {
        return minZoom.get();
    }

    public void setMinZoom(double value) {
        minZoom.set(value);
    }

    public DoubleProperty minZoomProperty() {
        return minZoom;
    }

    public double getMaxZoom() {
        return maxZoom.get();
    }

    public void setMaxZoom(double value) {
        maxZoom.set(value);
    }

    public DoubleProperty maxZoomProperty() {
        return maxZoom;
    }

}
