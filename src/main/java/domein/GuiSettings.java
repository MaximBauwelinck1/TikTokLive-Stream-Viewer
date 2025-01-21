package domein;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import utils.BackgroundType;

public class GuiSettings {
    private GuiBackground guiBackground;

    public GuiSettings(){
        guiBackground = new GuiBackground();
    }

    public void  setBackgroundColor(Color c){
        guiBackground.setBackgroundColor(c);
    }
    public void setBackgroundType(BackgroundType type){
        guiBackground.setBackgroundType(type);
    }
    public void setBackgroundImage(Image img){
        guiBackground.setBackgroundImage(img);
    }

    public BackgroundType getBackgroundType(){
        return guiBackground.getBackgroundType();
    }

    public Color getBackgroundColor(){
        return guiBackground.getBackgroundColor();
    }
    public Color getTextColor(){
        return guiBackground.getTextColor();
    }

    public Image getBackgroundImage(){
        return guiBackground.getBackgroundImage();
    }
}
