package gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import DTO.DonatieDTO;
import domein.DomeinController;
import domein.DonationObersver;
import domein.ImageConverter;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import main.MainApp;
import utils.BackgroundType;

public class StreamviewerController extends BorderPane implements PropertyChangeListener, DonationObersver{
    private DomeinController dc;
    
    @FXML
     Label lblAantalViewwers ;

    @FXML
     Label lblAantalLikes;

    @FXML
     Label lblRoomId;
    @FXML
     Label lblDonatie;

    @FXML
     ImageView imageDonatieUser;
    @FXML
     TextArea txtLeaderboard;

    private Boolean toontDonatie = false;
    public StreamviewerController(DomeinController dc){

        loadFxmlScreen("StreamViewer.fxml", dc);
        this.dc.addPropertyChangeListener(this);
        dc.addDonationObserver(this);
        this.getStylesheets().add(getClass().getResource("/css/Streamviewer.css").toExternalForm());

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
       private void loadFxmlScreen(String name, DomeinController dc) {
       //Courier_Prime_Bold.ttf
       //UbuntuMono-B.ttf
       //FantasqueSansMono-Bold.ttf
       Font font1 = Font.loadFont(getClass().getResource("/fonts/NotCourierSans-Bold.otf").toExternalForm(), 12);
    	this.dc = dc;
		FXMLLoader loader = new FXMLLoader(getClass().getResource(name));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		lblAantalViewwers.setText("aantal kijkers: 0");
        lblAantalLikes.setText( "aantal likes: 0");
        lblRoomId.setText("room ID: 0");
        lblDonatie.setText("");
        lblDonatie.setWrapText(true);
        txtLeaderboard.setText("");
        txtLeaderboard.setStyle("-fx-focus-color: transparent; -fx-text-box-border: transparent;");
        txtLeaderboard.setFont(font1);
        
    }
 

     @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("aantalViewers")){
             Platform.runLater(() -> {
            lblAantalViewwers.setText(String.format("aantal kijkers: %s", evt.getNewValue()));
        });
        } else if(evt.getPropertyName().equals("aantalLikes")){
            Platform.runLater(() -> {
           lblAantalLikes.setText(String.format("aantal likes: %s", evt.getNewValue()));
       });
       }else if(evt.getPropertyName().equals("roomId")){
            Platform.runLater(() -> {
            lblRoomId.setText(String.format("room ID: %s", evt.getNewValue()));
        });
        }
    }

   public void showEerstDonatie() {
    DonatieDTO donatie = dc.getDonatie();

    // Update label and image
    lblDonatie.setText(String.format("%s heeft een %s gedoneerd!", donatie.fromUser(), donatie.nameGift()));
    imageDonatieUser.setImage(ImageConverter.convertToFxImage(donatie.userPicture()));

    // Create fade-in transition for label
    FadeTransition fadeInLabel = new FadeTransition(Duration.seconds(5), lblDonatie);
    fadeInLabel.setFromValue(0.0); // Start invisible
    fadeInLabel.setToValue(1.0);   // Fully visible

    // Create fade-in transition for image
    FadeTransition fadeInImage = new FadeTransition(Duration.seconds(5), imageDonatieUser);
    fadeInImage.setFromValue(0.0); // Start invisible
    fadeInImage.setToValue(1.0);   // Fully visible

    // Set actions after fade-in
    fadeInLabel.setOnFinished(_ -> {
        // Create fade-out transition for label
        FadeTransition fadeOutLabel = new FadeTransition(Duration.seconds(5), lblDonatie);
        fadeOutLabel.setFromValue(1.0); // Fully visible
        fadeOutLabel.setToValue(0.0);   // Fully invisible

        // Create fade-out transition for image
        FadeTransition fadeOutImage = new FadeTransition(Duration.seconds(5), imageDonatieUser);
        fadeOutImage.setFromValue(1.0); // Fully visible
        fadeOutImage.setToValue(0.0);   // Fully invisible

        // Play fade-out transitions
        fadeOutLabel.play();
        fadeOutImage.play();

        // After fade-out, show the next donation or stop
        fadeOutLabel.setOnFinished(_ -> {
            if (!dc.isDonatiesEmpty()) {
                showEerstDonatie(); // Show the next donation
            } else {
                toontDonatie = false; // No more donations to show
            }
        });
    });

    // Play fade-in transitions simultaneously using ParallelTransition
    ParallelTransition fadeIn = new ParallelTransition(fadeInLabel, fadeInImage);
    fadeIn.play();
}
    @Override
    public void handleDonate() {
        txtLeaderboard.setText(dc.getTopTienVanLeaderboard());
       if(!dc.isDonatiesEmpty()&& !toontDonatie){
        toontDonatie= true;
        Platform.runLater(() -> {
            showEerstDonatie();
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
    public void setFontColor(Color c){
        String hexColor = String.format("#%02x%02x%02x", 
        (int) (c.getRed() * 255),
        (int) (c.getGreen() * 255),
        (int) (c.getBlue() * 255)
    );
        applyFontColorRecursively(this, hexColor);
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

    private void applyFontColorRecursively(Parent parent, String hexColor) {
        for (Node child : parent.getChildrenUnmodifiable()) {
            if (child instanceof Labeled) {
                // If it's a labeled node, apply the style
                ((Labeled) child).setStyle("-fx-text-fill:" + hexColor + ";");
            } else if (child instanceof Parent) {
                // If it's a container, recursively process its children
                applyFontColorRecursively((Parent) child, hexColor);
            }
        }
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
