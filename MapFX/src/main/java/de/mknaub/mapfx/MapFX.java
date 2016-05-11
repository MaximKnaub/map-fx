package de.mknaub.mapfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author MaKa
 */
public class MapFX extends Application {

    @Override public void start(Stage primaryStage) throws Exception {
        System.out.println("test");
        primaryStage.setScene(new Scene(new StackPane(new MapViewFX()), 1024, 728));
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
