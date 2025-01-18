package gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import DTO.DonatieDTO;
import domein.DomeinController;
import domein.DonationObersver;
import domein.ImageConverter;
import domein.Subject;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class StreamviewerController extends BorderPane implements PropertyChangeListener, DonationObersver{
    private DomeinController dc;
    
    @FXML
    private Label lblAantalViewwers ;

    @FXML
    private Label lblAantalLikes;

    @FXML
    private Label lblRoomId;
    @FXML
    private Label lblDonatie;

    @FXML
    private ImageView imageDonatieUser;
    @FXML
    private TextArea txtLeaderboard;

    private Boolean toontDonatie = false;
    public StreamviewerController(DomeinController dc){

        loadFxmlScreen("StreamViewer.fxml", dc);
        this.dc.addPropertyChangeListener(this);
        dc.addDonationObserver(this);
    }
       private void loadFxmlScreen(String name, DomeinController dc) {
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
        txtLeaderboard.setStyle(null);
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
}
