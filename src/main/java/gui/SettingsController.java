package gui;

import java.io.IOException;


import domein.DomeinController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.BackgroundType;

public class SettingsController extends AnchorPane{
    private DomeinController dc;

    @FXML
    Label lblChooseBackgroundOption;
    @FXML
    Label lblChooselanguage;
    @FXML
    Button btnBack;
    @FXML
    RadioButton radioBackgroundColor;
    @FXML
    RadioButton radioBackgroundImage;
    @FXML
    ChoiceBox<String> choiselanguage;
    @FXML
    ColorPicker colorPicker;
    public SettingsController(DomeinController dc){
      this.dc = dc;
    
       loadFxmlScreen("SettingsScreen.fxml");
       Image img = new Image(getClass().getResource("/icons/backArrow-white.png").toExternalForm());
      ImageView view = new ImageView(img);
      view.setFitHeight(80);
      view.setPreserveRatio(true);
      view.setFitWidth(30);
      btnBack.setText(null);
      btnBack.setGraphic(view);
      btnBack.setId("btnBack");
        btnBack.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        btnBack.setStyle("-fx-background-color: transparent; " +  // No background color
        "-fx-border-color: transparent; " +     // No border color
        "-fx-padding: 0;"); 
        btnBack.setOnAction(this::handleBackClick);
        this.getStylesheets().add(getClass().getResource("/css/Settingspage.css").toExternalForm());

        choiselanguage.getItems().addAll("Nederlands", "English");
        radioBackgroundColor.setOnAction(this::handleClickRadioColor);
        radioBackgroundImage.setOnAction(this::handleClickRadioImage);
        colorPicker.setOnAction(this::handleColorChange);

        radioBackgroundColor.setSelected(true);
        choiselanguage.setOnAction(this::handleLanguegeChange);
        initializeLanguage();
        if(dc.getBackgroundType() == BackgroundType.COLOR){
            Platform.runLater(() -> {
                initializeColor();
                });
        }
    }

    public void initializeLanguage(){
        lblChooseBackgroundOption.setText(dc.vertaalStrings("SettingsschermKiesBackgroundOptie"));
        lblChooselanguage.setText(dc.vertaalStrings("SettingsschermLanguagelabel"));
        radioBackgroundColor.setText(dc.vertaalStrings("SettingsschermBackgroundColorlabel"));
        radioBackgroundImage.setText(dc.vertaalStrings("SettingsschermBackgroundImagelabel"));

        if(dc.getTaal().getKey().equals("nl")){
            choiselanguage.setValue("Nederlands");
        } else if(dc.getTaal().getKey().equals("en")){
            choiselanguage.setValue("English");
        }
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

    public void handleBackClick(ActionEvent e){
        StartschermController ssc = new StartschermController( this.dc);
    	Stage stage = (Stage) this.getScene().getWindow();
    	stage.setScene(new Scene(ssc));
    	stage.show();
    }

    public void handleClickRadioColor(ActionEvent e){
       colorPicker.setDisable(false);
       dc.setBackgroundType(BackgroundType.COLOR);
    }
    public void initializeColor(){
        handleColorUpdate(dc.getBackgroundColor(), dc.getTextColor());
    }
    public void handleColorUpdate(Color background, Color text){ 
        this.setBackground(new Background(new BackgroundFill(
           background,  
            CornerRadii.EMPTY, 
            null               
        )));
        String hexColor = String.format("#%02x%02x%02x", 
        (int) (text.getRed() * 255),
        (int) (text.getGreen() * 255),
        (int) (text.getBlue() * 255)
    );
        this.getChildren().stream().filter(el -> el instanceof Labeled && !el.getId().equals("btnBack")) // check if element has an label
        .forEach(el -> el.setStyle("-fx-text-fill:" + hexColor+ ";"));

    }
    public void handleColorChange( ActionEvent e){
        dc.setBackgroundColor(colorPicker.getValue());
        dc.setBackgroundType(BackgroundType.COLOR);
        handleColorUpdate(dc.getBackgroundColor(), dc.getTextColor());
    }
    public void handleClickRadioImage(ActionEvent e){
        colorPicker.setDisable(true);
        dc.setBackgroundType(BackgroundType.IMAGE);
    }
    public void handleLanguegeChange(ActionEvent e){
       if(choiselanguage.getValue() == "English"){
            dc.setTaal("en","US");
       } else if(choiselanguage.getValue() == "Nederlands"){
            dc.setTaal("nl","BE");
       }
       initializeLanguage();
    }
}
