package de.knaubmaxim.mapfx.control;

import de.knaubmaxim.mapfx.MapView;
import de.knaubmaxim.mapfx.coord.Coordinate;
import de.knaubmaxim.mapfx.coord.CoordinateUtils;
import de.knaubmaxim.mapfx.layer.node.NodeLayer;
import java.util.Objects;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import static javafx.geometry.Pos.BOTTOM_LEFT;
import static javafx.geometry.Pos.CENTER;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 *
 * @author MaKa
 */
public class ScaleLine extends Control {

    private NodeLayer nodeLayer;
    private final VBox box = new VBox();
    private final Label label = new Label();
    private final IntegerProperty length = new SimpleIntegerProperty(500);
    private final StringProperty unit = new SimpleStringProperty("km");

    private final double maxWidth = 256;
    private Timeline timeline = new Timeline(new KeyFrame(Duration.millis(125), new KeyValue(box.maxWidthProperty(), maxWidth)));

    public ScaleLine() {
        initListener();
        activate();
    }

    private void initListener() {
        label.textProperty().bind(length.asString().concat(" ").concat(unit));

        mapViewProperty().addListener((ov, oldVal, newVal) -> {
            if (Objects.nonNull(oldVal)) {
                oldVal.removeLayer(nodeLayer);
            }
            if (Objects.nonNull(newVal)) {
                newVal.addLayer(nodeLayer = initLayer(newVal));
            }
        });
    }

    private NodeLayer initLayer(MapView mapView) {
        NodeLayer layer = new NodeLayer(mapView);
        ((StackPane) layer.getView()).setAlignment(BOTTOM_LEFT);
        ((StackPane) layer.getView()).setPadding(new Insets(10));
        ((StackPane) layer.getView()).setOpacity(.75);

        box.getChildren().add(label);
        box.setAlignment(CENTER);
        box.setPadding(new Insets(5));
        box.maxHeightProperty().bind(label.heightProperty().add(5));
        box.setMaxWidth(128);
        box.getStyleClass().add("map-fx-scale-line");

        layer.addNode(box);
        return layer;
    }

    @Override public void onZoom(double delta, double x, double y) {
        update();
    }

    @Override public void onMouseDrag(MouseEvent event) {
        update();
    }

    private void update() {
        Coordinate center = getMapView().getCenter();
        Point2D centerPoint = CoordinateUtils.coord2Point(getMapView().getViewport(), center);
        Coordinate left = CoordinateUtils.point2Coord(getMapView().getViewport(), new Point2D(centerPoint.getX() - maxWidth / 2, centerPoint.getY()));
        Coordinate right = CoordinateUtils.point2Coord(getMapView().getViewport(), new Point2D(centerPoint.getX() + maxWidth / 2, centerPoint.getY()));

        double distanceM = left.distanceTo(right);
        double distance = distanceM;

        if (distance < 2000) {
            setUnit("m");
        } else {
            setUnit("km");
            distance /= 1000;
        }

        length.setValue(roundDistance(distance));

        Point2D sourcePoint = CoordinateUtils.coord2Point(getMapView().getViewport(), left);
        Point2D destinationPoint = CoordinateUtils.coord2Point(getMapView().getViewport(), left.destinationPoint(90, getUnit().equals("km") ? length.doubleValue() * 1000 : length.doubleValue()));
        updateScaleLineWidth(destinationPoint.getX() - sourcePoint.getX());
    }

    private Integer roundDistance(Double distance) {
        Integer result;
        if (distance > 2500) {
            result = 2500;
        } else if (distance > 2000) {
            result = 2000;
        } else if (distance > 1500) {
            result = 1500;
        } else if (distance > 1000) {
            result = 1000;
        } else if (distance > 500) {
            result = 500;
        } else if (distance > 200) {
            result = 200;
        } else if (distance > 100) {
            result = 100;
        } else if (distance > 50) {
            result = 50;
        } else if (distance > 20) {
            result = 20;
        } else if (distance > 5) {
            result = 5;
        } else if (distance > 2) {
            result = 2;
        } else {
            result = 1000;
        }
        return result;
    }

    private void updateScaleLineWidth(double width) {
//        timeline.stop();
//        timeline = new Timeline(new KeyFrame(Duration.millis(50), new KeyValue(box.maxWidthProperty(), width)));
//        timeline.play();
        box.setMaxWidth(width);
    }

    //<editor-fold defaultstate="collapsed" desc="BOILERPLATE_CODE">
    public int getLength() {
        return length.get();
    }

    public void setLength(int value) {
        length.set(value);
    }

    public IntegerProperty lengthProperty() {
        return length;
    }

    public String getUnit() {
        return unit.get();
    }

    public void setUnit(String value) {
        unit.set(value);
    }

    public StringProperty unitProperty() {
        return unit;
    }
    //</editor-fold>
}
