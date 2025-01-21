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
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SettingsController extends AnchorPane{
    private DomeinController dc;

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
        btnBack.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        btnBack.setStyle("-fx-background-color: transparent; " +  // No background color
        "-fx-border-color: transparent; " +     // No border color
        "-fx-padding: 0;"); 
        btnBack.setOnAction(this::handleBackClick);
        this.getStylesheets().add(getClass().getResource("/css/Settingspage.css").toExternalForm());

        choiselanguage.getItems().addAll("Nederlands", "English");
        radioBackgroundColor.setOnAction(this::handleClickRadioColor);
        radioBackgroundImage.setOnAction(this::handleClickRadioImage);

        radioBackgroundColor.setSelected(true);
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
    }
    public void handleClickRadioImage(ActionEvent e){
        colorPicker.setDisable(true);
    }
}
