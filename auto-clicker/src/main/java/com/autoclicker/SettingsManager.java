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

    public void saveSettings(UserSettings settings) {
        try (Writer writer = new FileWriter(configFile)) {
            gson.toJson(settings, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
