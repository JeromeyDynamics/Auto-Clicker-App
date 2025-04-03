package com.autoclicker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.dispatcher.SwingDispatchService;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class App implements NativeKeyListener {

    private int clickDuration = 3; // Default duration in seconds
    private TrayIcon trayIcon;
    private JFrame settingsFrame;
    private JSpinner durationSpinner;

    public App() {
        setupTray();
        registerGlobalKeyListener();
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
                    showSettingsWindow();
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

    private void showSettingsWindow() {
        if (settingsFrame == null) {
            settingsFrame = new JFrame("Auto Clicker Settings");
            settingsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            settingsFrame.setSize(300, 150);
            settingsFrame.setLayout(new FlowLayout());

            JLabel durationLabel = new JLabel("Click duration (seconds):");
            durationSpinner = new JSpinner(new SpinnerNumberModel(clickDuration, 1, 60, 1));
            JButton saveButton = new JButton("Save");

            saveButton.addActionListener(e -> {
                clickDuration = (int) durationSpinner.getValue();
                JOptionPane.showMessageDialog(settingsFrame, "Duration set to " + clickDuration + " seconds.");
            });

            settingsFrame.add(durationLabel);
            settingsFrame.add(durationSpinner);
            settingsFrame.add(saveButton);
        }
        settingsFrame.setVisible(true);
    }

    private void registerGlobalKeyListener() {
        try {
            GlobalScreen.setEventDispatcher(new SwingDispatchService());
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("Error registering native hook.");
            ex.printStackTrace();
            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(this);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_A) {
            Clicker.startAutoClicker();
        }
        if (e.getKeyCode() == NativeKeyEvent.VC_S) {
            Clicker.stopAutoClicker();
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
    }
}
