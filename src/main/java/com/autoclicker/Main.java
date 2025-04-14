package com.autoclicker;

import javax.swing.SwingUtilities;

public class Main {
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