package de.mknaub.mapfx.control;

import de.mknaub.mapfx.MapView;
import de.mknaub.mapfx.layer.node.NodeLayer;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author maka
 */
public class LayerSwitcherControl extends Control {

    private NodeLayer nodeLayer;
    private final SimpleObjectProperty<Pos> position = new SimpleObjectProperty<>(Pos.TOP_RIGHT);
    
    public LayerSwitcherControl() {
        init();
    }
    
    private void init(){
        initListener();
    }

    private void initListener() {
        mapViewProperty().addListener((ov, oldVal, newVal) -> {
            if(nodeLayer!=null){
                oldVal.removeLayer(nodeLayer);
            }
            setMapView(newVal);
            nodeLayer = initLayer(newVal);
            newVal.addLayer(nodeLayer);
            VBox box = new VBox(5);
            newVal.getLayers().forEach(layer->{
                box.getChildren().add(new CheckBox(layer.getName()));
            });
            nodeLayer.addNode(box);
        });
    }

    private NodeLayer initLayer(MapView mapView) {
        nodeLayer = new NodeLayer(mapView);
        nodeLayer.setName(this.getClass().getSimpleName());
        ((StackPane)nodeLayer.getView()).setAlignment(position.getValue());
        return nodeLayer;
    }

}
