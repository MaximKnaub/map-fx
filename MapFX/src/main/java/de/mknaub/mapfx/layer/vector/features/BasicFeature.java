package de.knaubmaxim.mapfx.layer.vector.features;

import de.knaubmaxim.mapfx.MapView;
import de.knaubmaxim.mapfx.events.MapViewEvent;
import de.knaubmaxim.mapfx.layer.vector.VectorLayer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.DODGERBLUE;
import static javafx.scene.paint.Color.rgb;

/**
 *
 * @author maka
 */
public abstract class BasicFeature implements Drawable, MapViewEvent {

    protected MapView mapView;
    protected VectorLayer vectorLayer;
    protected double opacity = 1.0;
    protected Color fillColor = DODGERBLUE;
    protected Color hoverColor = DODGERBLUE;
    protected Color strokeColor = BLACK;
    protected double strokeWidth = 1;
    protected double offsetX, offsetY;
    protected DoubleProperty translateX = new SimpleDoubleProperty(0);
    protected DoubleProperty translateY = new SimpleDoubleProperty(0);

    protected SimpleBooleanProperty enabled = new SimpleBooleanProperty(true);

    @Override public MapView getMapView() {
        return mapView;
    }

    @Override public void setMapView(MapView mapView) {
        this.mapView = mapView;
    }

    @Override public VectorLayer getLayer() {
        return vectorLayer;
    }

    @Override public void setLayer(VectorLayer vectorLayer) {
        this.vectorLayer = vectorLayer;
    }

    @Override public Color getFillColor() {
        return this.fillColor;
    }

    @Override public void setFillColor(Color color) {
        this.fillColor = color;
//        this.setOpacity(opacity);
    }

    @Override public void setFillColor(int r, int g, int b) {
        this.fillColor = rgb(r, g, b);
    }

    @Override public void setFillColor(int r, int g, int b, double a) {
        this.fillColor = rgb(r, g, b, a);
    }

    @Override public Color getStrokeColor() {
        return this.strokeColor;
    }

    @Override public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }

    @Override public void setStrokeColor(int r, int g, int b) {
        this.strokeColor = rgb(r, g, b);
    }

    @Override public void setStrokeColor(int r, int g, int b, double a) {
        this.strokeColor = rgb(r, g, b, a);
    }

    @Override public double getStrokeWidth() {
        return strokeWidth;
    }

    @Override public void setStrokeWidth(double width) {
        this.strokeWidth = width;
    }

    @Override public void setOpacity(double opacity) {
        this.fillColor = new Color(getFillColor().getRed(), getFillColor().getGreen(), getFillColor().getBlue(), opacity);
        this.opacity = opacity;
    }

    @Override public double getOpacity() {
        return this.opacity;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }

    @Override public SimpleBooleanProperty enabledProperty() {
        return enabled;
    }

    @Override public boolean isEnabled() {
        return enabled.getValue();
    }

    @Override public void setEnabled(boolean enabled) {
        this.enabled.setValue(enabled);
    }

    public DoubleProperty translateXProperty() {
        return translateX;
    }

    public double getTranslateX() {
        return this.translateX.getValue();
    }

    public void setTranslateX(double translateX) {
        this.translateX.setValue(translateX);
    }

    public DoubleProperty translateYProperty() {
        return translateY;
    }

    public double getTranslateY() {
        return this.translateY.getValue();
    }

    public void setTranslateY(double translateY) {
        this.translateY.setValue(translateY);
    }

    ///// EventHandling ////////////////////////////////////////////////////////
    @Override
    public void onMouseDragEntered(MouseDragEvent mouseEvent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMouseDragExited(MouseDragEvent mouseEvent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMouseDragOver(MouseDragEvent mouseEvent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMouseDragReleased(MouseDragEvent mouseEvent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMouseClicked(MouseEvent mouseEvent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMouseDragged(MouseEvent mouseEvent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMouseEntered(MouseEvent mouseEvent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMouseExited(MouseEvent mouseEvent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMouseMoved(MouseEvent mouseEvent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMousePressed(MouseEvent mouseEvent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMouseReleased(MouseEvent mouseEvent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
