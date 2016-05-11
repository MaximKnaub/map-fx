package de.mknaub.mapfx.layer.node;

import de.mknaub.mapfx.MapView;
import de.mknaub.mapfx.layer.Layer;
import static javafx.geometry.Pos.BOTTOM_RIGHT;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author maka
 */
public class NodeLayer extends Layer {

    private final Pane layerPane;

    public NodeLayer(MapView mapView) {
        super(mapView);
        layerPane = new StackPane();
        ((StackPane) layerPane).setAlignment(BOTTOM_RIGHT);
    }
    
    public NodeLayer(MapView mapView, Pane pane){
        super(mapView);
        layerPane = pane;
    }

    @Override public void renderRequest() {
    }

    @Override public String getName() {
        return "Nodelayer";
    }

    @Override
    public Node getView() {
        return layerPane;
    }

    public void addNode(Node node) {
        layerPane.getChildren().add(node);
    }

    @Override public double getTranslateX() {
        return layerPane.getTranslateX();
    }

    @Override public double getTranslateY() {
        return layerPane.getTranslateY();
    }

}
