package gui;

import java.io.File;
import java.io.IOException;
import domein.DomeinController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.image.Image;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.BackgroundType;

public class StartschermController extends AnchorPane {
    private DomeinController dc;
    @FXML
    Label lblMainTitle;
    @FXML
    Label lblSelect;
    @FXML
    Button btnStart;
    @FXML
    Button btnSettings;
    @FXML
    ComboBox<String> comboBox;

    public StartschermController(DomeinController dc){
      this.dc = dc;
    
       loadFxmlScreen("StartupScreen.fxml");
        btnStart.setOnAction(this::handleStart);
        btnSettings.setOnAction(this::goToSettingsPage);
        comboBox.getItems().addAll("--this will later be customizable--");
        comboBox.setEditable(true);
        initializelanguage();

        if(dc.getBackgroundType() == BackgroundType.COLOR){
            Platform.runLater(() -> {
                initializeColor();
                });
        } else if(dc.getBackgroundType() == BackgroundType.IMAGE){
            Platform.runLater(() -> {
                intializeBackgroundPhoto();
                });
        }
    }

    public void intializeBackgroundPhoto(){
        File file = new File(dc.getBackgroundImagePath());
       setBackgroundPhoto(file);
    }
    public void initializeColor(){
        setColors(dc.getBackgroundColor(), dc.getTextColor());
    }
    public void initializelanguage()
    {
        comboBox.setPromptText(dc.vertaalStrings("StartschermComboboxPlaceholder"));
        lblMainTitle.setText(dc.vertaalStrings("StartschermTitel"));
        lblSelect.setText(dc.vertaalStrings("StartschermStreamerLabel"));
        btnStart.setText(dc.vertaalStrings("StartschermStartKnop"));
        btnSettings.setText(dc.vertaalStrings("StartschermSettingsKnop"));
    }
     private void loadFxmlScreen(String name) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(name));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

    }

    public void handleStart(ActionEvent action){
        try {
            dc.setStreamerId(comboBox.getValue());
            dc.startStreamWachting();
            startStream();
        } catch (Exception ex) {
           Alert a = new Alert(AlertType.ERROR);
           a.setHeaderText("Er is een fout opgetreden.");
           a.setContentText(ex.getMessage());
            a.showAndWait();
        }
    }


    public void afsluiten(ActionEvent event){
        System.exit(0);
    }

    public void startStream(){
    	StreamviewerController ssc = new StreamviewerController( this.dc);
    	Stage stage = (Stage) this.getScene().getWindow();
    	stage.setScene(new Scene(ssc));
        stage.setWidth(1200); // bounds.getWidth()
        stage.setHeight(850);
    	stage.show();
    }

    public void goToSettingsPage(ActionEvent e){
    	SettingsController ssc = new SettingsController( this.dc);
    	Stage stage = (Stage) this.getScene().getWindow();
    	stage.setScene(new Scene(ssc));
        stage.setWidth(625); 
        stage.setHeight(420);
    	stage.show();
    }

    public void setFontColor(Color c){
        String hexColor = String.format("#%02x%02x%02x", 
        (int) (c.getRed() * 255),
        (int) (c.getGreen() * 255),
        (int) (c.getBlue() * 255)
    );
        this.getChildren().stream().filter(el -> el instanceof Labeled && !el.getId().equals("btnBack")) // check if element has an label
        .forEach(el -> el.setStyle("-fx-text-fill:" + hexColor+ ";"));
    }

    public void setBackgroundPhoto(File file){
        Image image = new Image(file.toURI().toString());
          BackgroundImage backgroundImage = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT, 
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        this.setBackground(new Background(backgroundImage));
        System.out.println(dc.getTextColorForImage());
        setFontColor(dc.getTextColorForImage());
    }

    public void setColors(Color background, Color text){ 
        this.setBackground(new Background(new BackgroundFill(
           background,  
            CornerRadii.EMPTY, 
            null               
        )));
        this.getChildren().stream().filter(child -> child instanceof Button)
        .forEach(child -> {
            Button button = (Button) child;
            button.setBackground(new Background(new BackgroundFill(
                background.brighter().brighter(),  
                 new CornerRadii(5), 
                 null               
             )));
        });
        setFontColor(text);
    }
}