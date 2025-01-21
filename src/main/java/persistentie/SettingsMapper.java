package persistentie;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SettingsMapper {
    private static final String FILE_NAME = "settings.properties";
    private Properties properties;

    public SettingsMapper() {
        properties = new Properties();
        loadSettings();
    }

    public void setSetting(String key, String value) {
        properties.setProperty(key, value);
    }

    public String getSetting(String key) {
        return properties.getProperty(key);
    }

    public void saveSettings() {
        try (FileOutputStream fos = new FileOutputStream(FILE_NAME)) {
            properties.store(fos, "Application Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSettings() {
        try (FileInputStream fis = new FileInputStream(FILE_NAME)) {
            properties.load(fis);
        } catch (IOException e) {
            System.out.println("Settings file not found. Creating a new one.");
        }
    }
}
