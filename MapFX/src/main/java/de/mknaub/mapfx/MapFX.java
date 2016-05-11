package de.knaubmaxim.mapfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author MaKa
 */
public class MapFX extends Application {

    @Override public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(new StackPane(new Button("MapFX")), 512, 250));
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
