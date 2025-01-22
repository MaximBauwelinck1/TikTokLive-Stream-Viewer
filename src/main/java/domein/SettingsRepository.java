package domein;

import java.util.Arrays;

import javafx.scene.paint.Color;
import javafx.util.Pair;
import persistentie.SettingsMapper;
import utils.BackgroundType;

public class SettingsRepository {

    private final SettingsMapper mapper;

    public SettingsRepository() 
    {
        mapper = new SettingsMapper();
    }
    
    public Pair<String,String> getLanguage(){
       String taal = mapper.getSetting("language");
       if(taal == null) return null;
       String[] locale = taal.split(",");
       return new Pair<String,String>(locale[0], locale[1]);
    }

    public void saveLanguage(String language, String country){
        mapper.setSetting("language", language +","+country);
        saveSettings();
    }

    public void saveBackgroundColor(Color background, Color textColor){
        mapper.setSetting("backgroundColor",String.format("%f|%f|%f", background.getRed(),background.getGreen(),background.getBlue()));
        mapper.setSetting("textColor",String.format("%f|%f|%f", textColor.getRed(),textColor.getGreen(),textColor.getBlue()));
        saveSettings();
    }

    public void saveBackgroundType(BackgroundType type){
        mapper.setSetting("backgroundType",type.toString());
        saveSettings();
    }
    public Color getBackgroundColor(){
        String colortext = mapper.getSetting("backgroundColor");
        if(colortext == null) return null;
        String[] f= colortext.split("\\|");
        f = Arrays.stream(f).map(el -> el.replace(',', '.')).toArray(String[]::new);
        return new Color(Double.parseDouble(f[0]), Double.parseDouble(f[1]), Double.parseDouble(f[2]), 1);
    }

    public Color gettextColor(){
        String colortext = mapper.getSetting("textColor");
        if(colortext == null) return null;
        String[] f= colortext.split("\\|");
        f = Arrays.stream(f).map(el -> el.replace(',', '.')).toArray(String[]::new);
        return new Color(Double.parseDouble(f[0]), Double.parseDouble(f[1]), Double.parseDouble(f[2]), 0);
    }

    public BackgroundType getBackgroundType(){
        String type = mapper.getSetting("backgroundType");
        if(type == null) return null;
        return BackgroundType.valueOf(type);
    }

    public void setBackgroundImage(String path){
        mapper.setSetting("backgroundImagePath", path);
        saveSettings();
    }
    public String getBackgroundImagePath(){
        return mapper.getSetting("backgroundImagePath");
    }
    
    private void saveSettings(){
        mapper.saveSettings();
    }
}
