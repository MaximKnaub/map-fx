package de.mknaub.mapfx.control;

import de.mknaub.mapfx.MapView;
import de.mknaub.mapfx.coord.Coordinate;
import de.mknaub.mapfx.layer.node.NodeLayer;
import java.text.DecimalFormat;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import static javafx.geometry.Pos.BOTTOM_RIGHT;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author maka
 */
@Slf4j
public class MousePositionConrol extends Control {

    private NodeLayer nodeLayer;
    private Label positionLabel;
    private final SimpleObjectProperty<Pos> position = new SimpleObjectProperty<>(BOTTOM_RIGHT);
    private final DecimalFormat df = new DecimalFormat("0.0000000");

    public MousePositionConrol() {
        initListener();
        activate();
    }

    private NodeLayer initLayer(MapView mapView) {
        positionLabel = new Label("0,0");
        Coordinate c = getMapView().getCenter();
        positionLabel.setText(df.format(c.getLongitude()) + ", " + df.format(c.getLatitude()) + " ( " + c.getWGS84Longitude() + " " + c.getWGS84Latitude() + " )");
        nodeLayer = new NodeLayer(mapView);
        ((Pane) nodeLayer.getView()).getStyleClass().add("mouse-position");
        ((StackPane) nodeLayer.getView()).setAlignment(position.getValue());
        nodeLayer.addNode(positionLabel);
        return nodeLayer;
    }

    private void initListener() {
        position.addListener(e -> {
            if (getMapView() != null) {
                ((StackPane) nodeLayer.getView()).setAlignment(position.getValue());
            }
        });

        mapViewProperty().addListener((ov, oldVal, newVal) -> {
            if (oldVal != null) {
                oldVal.removeLayer(nodeLayer);
            }
            if (newVal != null) {
                nodeLayer = initLayer(newVal);
                newVal.addLayer(nodeLayer);
            }
        });
    }

    public SimpleObjectProperty<Pos> positionProperty() {
        return position;
    }

    public Pos getPosition() {
        return position.getValue();
    }

    public void setPosition(Pos position) {
        this.position.setValue(position);
    }

    @Override public void onMouseMoved(MouseEvent event) {
        if (getMapView() != null) {
            Point2D l = getMapView().getMapPane().sceneToLocal(event.getSceneX(), event.getSceneY());
            double x = l.getX(), y =l.getY();
            Coordinate c = getMapView().point2Coord(x, y);
            positionLabel.setText(df.format(c.getLongitude()) + ", " + df.format(c.getLatitude()) + " ( " + c.getWGS84Longitude() + " " + c.getWGS84Latitude() + ", " + getMapView().getZoom() + " )");
        }
    }
}
