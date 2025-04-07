package org.mcdcl.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.nio.file.*;

public class SettingsManager {
    private static final String SETTINGS_FILE = "settings.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static Settings loadSettings() throws IOException {
        Path settingsPath = Paths.get(SETTINGS_FILE);
        if (!Files.exists(settingsPath)) {
            return new Settings();
        }

        try (Reader reader = Files.newBufferedReader(settingsPath)) {
            return gson.fromJson(reader, Settings.class);
        }
    }

    public static void saveSettings(Settings settings) throws IOException {
        try (Writer writer = Files.newBufferedWriter(Paths.get(SETTINGS_FILE))) {
            gson.toJson(settings, writer);
        }
    }
}