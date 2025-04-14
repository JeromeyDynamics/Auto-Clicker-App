package com.autoclicker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;

public class SettingsManager {
    private final Gson gson;
    private final File configFile;

    public SettingsManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.configFile = new File(System.getProperty("user.home"), ".autoclicker_config.json");
    }

    /**
     * Loads the user's settings from the configuration file.
     * If the file does not exist, the default settings are returned.
     * If the file exists but there is an error reading it, the default settings are
     * returned.
     * 
     * @return the user's settings
     */
    public UserSettings loadSettings() {
        if (!configFile.exists()) {
            return new UserSettings();
        }
        try (Reader reader = new FileReader(configFile)) {
            return gson.fromJson(reader, UserSettings.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new UserSettings();
        }
    }

    /**
     * Saves the given settings to the configuration file.
     * If there is an error saving the file, the error is printed to the console.
     * 
     * @param settings the settings to save
     */
    public void saveSettings(UserSettings settings) {
        try (Writer writer = new FileWriter(configFile)) {
            gson.toJson(settings, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
