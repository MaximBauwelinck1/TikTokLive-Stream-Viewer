package gui;

import java.io.File;
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
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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
    @FXML
    Button btnKiesFoto;
    private StartschermController startschermController;
    public SettingsController(DomeinController dc, StartschermController startschermController){
      this.dc = dc;
        this.startschermController = startschermController;
       loadFxmlScreen("/gui/SettingsScreen.fxml");
       initializeLanguage();
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
        this.getStylesheets().add(getClass().getResource("/css/SettingsPage.css").toExternalForm());

        choiselanguage.getItems().addAll("Nederlands", "English");
        radioBackgroundColor.setOnAction(this::handleClickRadioColor);
        radioBackgroundImage.setOnAction(this::handleClickRadioImage);
        btnKiesFoto.setOnAction(this::handlekiesFoto);
        colorPicker.setOnAction(this::handleColorChange);
        if(dc.getBackgroundType() == BackgroundType.COLOR){
            radioBackgroundColor.setSelected(true);
            btnKiesFoto.setDisable(true);
            Platform.runLater(() -> {
                initializeColor();
                });
        } else if(dc.getBackgroundType() == BackgroundType.IMAGE){
            radioBackgroundImage.setSelected(true);
            colorPicker.setDisable(true);
            File file  = new File(dc.getBackgroundImagePath());
            btnKiesFoto.setText(file.getName());
            Platform.runLater(() -> {
                intializeBackgroundPhoto();
                });
        }
        choiselanguage.setOnAction(this::handleLanguegeChange);
    }

    public void initializeLanguage(){
        lblChooseBackgroundOption.setText(dc.vertaalStrings("SettingsschermKiesBackgroundOptie"));
        lblChooselanguage.setText(dc.vertaalStrings("SettingsschermLanguagelabel"));
        radioBackgroundColor.setText(dc.vertaalStrings("SettingsschermBackgroundColorlabel"));
        radioBackgroundImage.setText(dc.vertaalStrings("SettingsschermBackgroundImagelabel"));
        btnKiesFoto.setText(dc.vertaalStrings("SettingsSchermButtonKiesFoto"));
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

    public void handlekiesFoto(ActionEvent e){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(dc.vertaalStrings("SettingsSchermKiesFoto"));
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter(dc.vertaalStrings("SettingsSchermToegestaneFiles"), "*.jpg","*.png"));
        File img = fileChooser.showOpenDialog((Stage) this.getScene().getWindow()).getAbsoluteFile();
        if(img == null) return ;
        btnKiesFoto.setText(img.getName());
        dc.setBackgroundImage(img.getAbsolutePath());
        setBackgroundPhoto(img);
    }
    public void handleBackClick(ActionEvent e){
        StartschermController ssc = new StartschermController( this.dc);
    	Stage stage = (Stage) this.getScene().getWindow();
    	stage.setScene(new Scene(ssc));
    	stage.show();
    }

    public void handleClickRadioColor(ActionEvent e){
       colorPicker.setDisable(false);
       btnKiesFoto.setDisable(true);
       dc.setBackgroundType(BackgroundType.COLOR);
    }
    public void intializeBackgroundPhoto(){
        File file = new File(dc.getBackgroundImagePath());
       setBackgroundPhoto(file);
    }
    public void initializeColor(){
        setColors(dc.getBackgroundColor(), dc.getTextColor());
        colorPicker.setValue(dc.getBackgroundColor());
    }

    public void setBackgroundPhoto(File file){
        Image image = new Image(file.toURI().toString());
          BackgroundImage backgroundImage = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT, 
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
                Platform.runLater(() -> {
        this.setBackground(new Background(backgroundImage));
                });
        System.out.println(dc.getTextColorForImage());
        setFontColor(dc.getTextColorForImage());
    }
    public void setColors(Color background, Color text){ 
        Platform.runLater(() -> {
        this.setBackground(new Background(new BackgroundFill(
           background,  
            CornerRadii.EMPTY, 
            null               
        )));
        });
        setFontColor(text);
    }

    public void setFontColor(Color c){
        String hexColor = String.format("#%02x%02x%02x", 
        (int) (c.getRed() * 255),
        (int) (c.getGreen() * 255),
        (int) (c.getBlue() * 255));
        Platform.runLater(() -> {
        this.getChildren().stream().filter(el -> el instanceof Labeled && !el.getId().equals("btnBack")) // check if element has an label
        .forEach(el -> el.setStyle("-fx-text-fill:" + hexColor+ ";"));
        });
    }
    public void handleColorChange( ActionEvent e){
        dc.setBackgroundColor(colorPicker.getValue());
        dc.setBackgroundType(BackgroundType.COLOR);
        setColors(dc.getBackgroundColor(), dc.getTextColor());
    }
    public void handleClickRadioImage(ActionEvent e){
        colorPicker.setDisable(true);
        btnKiesFoto.setDisable(false);
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
