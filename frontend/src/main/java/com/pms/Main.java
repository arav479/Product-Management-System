package com.pms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entry point of the Product Management System JavaFX frontend.
 *
 * This class only loads the FXML layout and shows the window.
 * Database / JDBC code should not be added here.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/product-view.fxml"));
        Scene scene = new Scene(root, 980, 720);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.setTitle("Product Management System");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(860);
        primaryStage.setMinHeight(640);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
