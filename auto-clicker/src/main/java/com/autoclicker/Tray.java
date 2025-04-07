package com.autoclicker;

import java.awt.*;
import java.awt.event.*;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

public class Tray {
    private TrayIcon trayIcon;
    private App app;
    private SettingsManager settingsManager;

    public Tray() {
        setupTray();
        app = new App();
        settingsManager = new SettingsManager();

        // Load the user settings and pass them to KeyListener
        UserSettings settings = settingsManager.loadSettings();
        KeyListener keyListener = new KeyListener();
    }

    private void setupTray() {
        // Check if the system supports a system tray
        if (!SystemTray.isSupported()) {
            System.err.println("System tray is not supported.");
            return;
        }

        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().getImage("auto-clicker\\src\\main\\res\\img\\pixil-frame-0.png");
        trayIcon = new TrayIcon(image, "Auto Clicker");
        trayIcon.setImageAutoSize(true);

        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    app.showSettingsWindow();
                }
            }
        });

        PopupMenu popup = new PopupMenu();
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(e -> {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException ex) {
                ex.printStackTrace();
            }
            System.exit(0);
        });
        popup.add(exitItem);
        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println("TrayIcon could not be added.");
            e.printStackTrace();
        }
    }
}
