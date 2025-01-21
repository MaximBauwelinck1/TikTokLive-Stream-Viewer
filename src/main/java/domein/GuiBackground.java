package domein;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import utils.BackgroundType;

public class GuiBackground {
    private Color backgroundColor;
    private Color textColor ;
    private Image backgroundImage;
    private BackgroundType backgroundType;

    public void setBackgroundColor(Color c){
        this.backgroundColor = c;
        this.textColor = Color.web(GuiBackground.calculateFontColor(c.getRed(),c.getGreen(), c.getBlue()));
    }
    public void setBackgroundImage(Image img){
        this.backgroundImage = img;
    }

    public void setBackgroundType(BackgroundType type){
        this.backgroundType = type;
    }

    public Color getTextColor() {
        return textColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Image getBackgroundImage(){
        return backgroundImage;
    }
    public BackgroundType getBackgroundType(){
        return backgroundType;
    }

    private static String calculateFontColor(double rNorm, double gNorm, double bNorm) {

        // Calculate the luminance
        double luminance = 0.2126 * rNorm + 0.7152 * gNorm + 0.0722 * bNorm;
        // Return white or black based on the luminance
        return (luminance > 0.5) ? "#000000" : "#FFFFFF";
    }
}
