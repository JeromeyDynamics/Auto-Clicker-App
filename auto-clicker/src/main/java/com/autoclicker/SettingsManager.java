package com.autoclicker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;

public class SettingsManager {
    private final Gson gson;
    private final File configFile;

    // Constructor initializes Gson instance and config file path
    public SettingsManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.configFile = new File(System.getProperty("user.home"), ".autoclicker_config.json");
    }

    // Loads settings from the configuration file
    public UserSettings loadSettings() {
        if (!configFile.exists()) {
            return new UserSettings(); // Return default settings if config file doesn't exist
        }
        try (Reader reader = new FileReader(configFile)) {
            return gson.fromJson(reader, UserSettings.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new UserSettings(); // Return default settings in case of an error
        }
    }

    // Saves the provided settings to the configuration file
    public void saveSettings(UserSettings settings) {
        try (Writer writer = new FileWriter(configFile)) {
            gson.toJson(settings, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
