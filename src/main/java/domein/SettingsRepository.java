package domein;

import javafx.util.Pair;
import persistentie.SettingsMapper;

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

    private void saveSettings(){
        mapper.saveSettings();
    }
}
