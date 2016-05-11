package de.knaubmaxim.mapfx.layer.vector;

import de.knaubmaxim.mapfx.MapView;
import de.knaubmaxim.mapfx.layer.Layer;
import de.knaubmaxim.mapfx.layer.vector.features.Drawable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import static javafx.scene.paint.Color.RED;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author maka
 */
@Slf4j
public class VectorLayer extends Layer {

    private final Pane layerPane;
    private final LinkedList<Drawable> featureList = new LinkedList<>();
    private final Canvas canvas;
    private ImageView imageView;
    private double imageWidth, imageHeight;
    private boolean isDirty = true;
    private final Timeline renderRequestTimeline = new Timeline(new KeyFrame(Duration.millis(250), (ActionEvent event) -> renderRequest()));
    private final InvalidationListener listener = (Observable observable) -> {
        renderRequestTimeline.playFromStart();
    };

    public VectorLayer(MapView mapView) {
        super(mapView);
        layerPane = new Pane();

        canvas = new Canvas();
        canvas.widthProperty().bind(mapView.widthProperty());
        canvas.heightProperty().bind(mapView.heightProperty());

        layerPane.getChildren().add(canvas);
//        layerPane.setOnMouseMoved(e -> {
//            double sceneX = e.getSceneX(), sceneY = e.getSceneY();
//            Coordinate coord = getMapView().point2Coord(sceneX, sceneY);
//            Double distance = Double.POSITIVE_INFINITY;
//            try {
//                Point get = dirtyDrawables.stream().map((t) -> (Point) t).filter((t) -> t.isMouseHover(e)).findFirst().get();
//                Color fillColor = get.getFillColor();
//                get.setFillColor(Color.GREEN);
//                get.draw(gc);
//                get.setFillColor(fillColor);
//            } catch (Exception ex) {
//            }
//        });
    }

    @Override public void move(double x, double y) {
        moveX(x);
        moveY(y);
    }

    @Override public void moveX(double x) {
        layerPane.setTranslateX(layerPane.getTranslateX() - x);
    }

    @Override public void moveY(double y) {
        layerPane.setTranslateY(layerPane.getTranslateY() - y);
    }

    @Override public void renderRequest() {
        layerPane.setTranslateX(0);
        layerPane.setTranslateY(0);
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        if (isDirty) {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.setFill(RED);
            isDirty = false;
        }
        final List<Drawable> dirtyDrawables = new ArrayList<>();
        dirtyDrawables.clear();
        featureList.stream().filter(drawable -> drawable.isOnScreen(getMapView().getBounds()) && drawable.isEnabled()).forEach(drawable -> {
            dirtyDrawables.add(drawable);
        });
        if (dirtyDrawables.isEmpty()) {
            return;
        }
        dirtyDrawables.forEach((drawable) -> {
            drawable.calculate(getMapView());
        });
        dirtyDrawables.forEach((drawable) -> {
            drawable.draw(gc);
        });
        isDirty = true;
    }

    @Override public Node getView() {
        return layerPane;
    }

    public void addFeature(Drawable drawable) {
        drawable.setMapView(getMapView());
        featureList.add(drawable);
        drawable.enabledProperty().addListener(listener);
//        drawable.draw(gc); // TODO: diesen aufruf checken vlt. ist es so besser anstelle den kompletten layer zu rendern
        renderRequest();
    }

    public void addAllFeature(Collection<? extends Drawable> drawables) {
        drawables.stream().forEach(e -> {
            e.setMapView(getMapView());
            e.enabledProperty().addListener(listener);
        });

        featureList.addAll(drawables);
        renderRequest();
    }

    public void clearFeatures() {
        featureList.forEach(e -> e.enabledProperty().removeListener(listener));
        featureList.clear();
        renderRequest();
    }

    public void removeFeature(Drawable drawable) {
        featureList.remove(drawable);
        drawable.enabledProperty().removeListener(listener);
    }

    public void removeAllFeatures(Collection<? extends Drawable> drawables) {
        drawables.stream().forEach(e -> {
            e.enabledProperty().removeListener(listener);
        });

        featureList.removeAll(drawables);
        renderRequest();
    }

    @Override public double getTranslateX() {
        return layerPane.getTranslateX();
    }

    @Override public double getTranslateY() {
        return layerPane.getTranslateY();
    }

    public LinkedList<Drawable> getFeatureList() {
        return featureList;
    }
}
