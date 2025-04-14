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
        // Initialize global key listener using current settings
        new KeyListener();
    }

    /**
     * Sets up the system tray icon and its associated context menu.
     * If system tray is not supported, this method does nothing.
     * The system tray icon is set to the image located at
     * src/main/res/img/icon.png.
     * The context menu has one item: "Exit", which exits the application
     * and unregisters the native hook.
     */
    private void setupTray() {
        if (!SystemTray.isSupported()) {
            System.err.println("System tray is not supported.");
            return;
        }

        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().getImage("src\\main\\res\\img\\icon.png");
        trayIcon = new TrayIcon(image, "Auto Clicker");
        trayIcon.setImageAutoSize(true);

        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Left-click to open the settings window
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
