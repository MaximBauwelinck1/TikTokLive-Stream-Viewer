package gui;

import java.io.IOException;
import domein.DomeinController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
}