package gui;

import java.util.Optional;

import domein.DomeinController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class StartschermController extends BorderPane {
    private DomeinController dc;
    Label lbl = new Label("Tiktok Stream viewer");
    Label lblSelect = new Label("Kies een streamer of voer een id in : ");
    Button btnStart = new Button("Start");
    ComboBox<String> comboBox = new ComboBox<>();

    public StartschermController(DomeinController dc){
      this.dc = dc;
        GridPane midden = new GridPane();

        midden.setAlignment(Pos.TOP_CENTER);
        midden.setHgap(0);
        midden.setVgap(20);

        lbl.setFont(Font.font("Tahoma",FontWeight.NORMAL,20));
        lbl.setTextFill(Color.BLACK);
        lbl.setAlignment(Pos.CENTER);

       
        lblSelect.setFont(Font.font("Tahoma",FontWeight.NORMAL,20));
        lblSelect.setTextFill(Color.BLACK);
        lblSelect.setAlignment(Pos.CENTER);

        btnStart.setFont(Font.font("Tahoma",FontWeight.NORMAL,20));
        btnStart.setTextFill(Color.BLACK);
        btnStart.setAlignment(Pos.CENTER);
        btnStart.setOnAction(this::handleStart);
        comboBox.getItems().addAll("king.alasow");
        comboBox.setEditable(true);

        midden.add(lbl,0,1);
        midden.add(lblSelect, 0, 2);
        midden.add(comboBox, 1, 2);
        midden.add(btnStart, 0, 5);
        GridPane.setHalignment(lbl, HPos.CENTER);

        this.setCenter(midden);

       
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
    	stage.show();
    }
}