package com.autoclicker;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Main {
    /**
     * Loads an icon from the given path and returns it as an Image.
     * The path is relative to the class's resource directory.
     * If the icon is not found, null is returned and an error message is printed to
     * stderr.
     * 
     * @param path the path to the icon
     * @return the icon or null if not found
     */
    public static Image loadIcon(String path) {
        URL imageUrl = Main.class.getResource(path);
        if (imageUrl != null) {
            return Toolkit.getDefaultToolkit().getImage(imageUrl);
        } else {
            System.err.println("Icon not found:" + path);
            return null;
        }
    }

    /**
     * Main entry point of the application.
     *
     * This method creates a new instance of the Tray class, which sets up the
     * system tray and the settings window.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Tray());
    }
}