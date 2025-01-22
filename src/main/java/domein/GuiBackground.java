package domein;

import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import utils.BackgroundType;

public class GuiBackground {
    private Color backgroundColor;
    private Color textColor ;
    private Color textColorForImage ;
    private String backgroundImagePath;
    private BackgroundType backgroundType;

    public void setBackgroundColor(Color c){
        this.backgroundColor = c;
        this.textColor = Color.web(GuiBackground.calculateFontColor(c.getRed(),c.getGreen(), c.getBlue()));
    }
    public void setBackgroundImage(String img){
        this.backgroundImagePath = img;
        if(img == null) return ;
        this.textColorForImage = Color.web(GuiBackground.calcultaeFontColorForBackImg(new File(img)));
    }

    public void setBackgroundType(BackgroundType type){
        this.backgroundType = type;
    }

    public Color getTextColor() {
        return textColor;
    }
    public Color getTextColorForImage() {
        return textColorForImage;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public String getBackgroundImage(){
        return backgroundImagePath;
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

    private static String calcultaeFontColorForBackImg(File file){
          Image image = new Image(file.toURI().toString());
        
        PixelReader pixelReader = image.getPixelReader();

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        
        Color pixelColor = pixelReader.getColor(width / 2, height / 2);

        // Calculate the luminance of the pixel
       return calculateFontColor(pixelColor.getRed(),pixelColor.getGreen(),pixelColor.getBlue());

    }
}
