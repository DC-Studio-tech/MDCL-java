package org.mcdcl;

import org.mcdcl.ui.MainView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("MDCL - Minecraft启动器");
        
        MainView mainView = new MainView();
        Scene scene = new Scene(mainView, 1280, 800);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(768);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}