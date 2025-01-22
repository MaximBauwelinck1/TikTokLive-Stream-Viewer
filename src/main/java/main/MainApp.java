package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

import domein.DomeinController;
import gui.StartschermController;


public class MainApp extends Application {
    private static Stage stage;

    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) throws IOException {
        StartschermController welkomScherm = new StartschermController(DomeinController.getInstance());
        welkomScherm.setId("pane");
        primaryStage.setTitle("Tiktok");
        //primaryStage.setMaximized(true);
        
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(625); // bounds.getWidth()
        primaryStage.setHeight(420);//bounds.getHeight()
        
        Scene scene = new Scene(welkomScherm);
        scene.setFill(Color.RED);
        primaryStage.setOnCloseRequest(event -> {
        Platform.exit(); 
        System.exit(0);  // om alle treads te stoppen
        });
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }

    static void setRoot(String fxml) throws IOException {
        setRoot(fxml,stage.getTitle());
    }

    static void setRoot(String fxml, String title) throws IOException {
        Scene scene = new Scene(loadFXML(fxml));
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/fxml/"+fxml + ".fxml"));
        return fxmlLoader.load();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
