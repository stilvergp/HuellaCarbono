package com.github.stilvergp;

import com.github.stilvergp.controller.AppController;
import com.github.stilvergp.view.Scenes;
import com.github.stilvergp.view.View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    public static Scene scene;
    public static Stage stage;
    public static AppController currentController;

    @Override
    public void start(Stage stage) throws IOException {
        View view = AppController.loadFXML(Scenes.ROOT);
        scene = new Scene(view.scene, 800, 650);
        currentController = (AppController) view.controller;
        currentController.onOpen(null);
        stage.setTitle("Carbon Footprint");
        stage.setMinHeight(scene.getHeight());
        stage.setMinWidth(scene.getWidth());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}