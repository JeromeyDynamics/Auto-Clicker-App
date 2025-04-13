package com.autoclicker;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.dispatcher.SwingDispatchService;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyListener implements NativeKeyListener {
    public KeyListener() {
        registerGlobalKeyListener();
    }

    public void registerGlobalKeyListener() {
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
        SettingsManager sm = new SettingsManager();
        UserSettings settings = sm.loadSettings();

        // If auto clicking is disabled, ignore key events.
        if (settings.isAutoClickerDisabled()) {
            return;
        }

        String keyPressed = NativeKeyEvent.getKeyText(e.getKeyCode());
        String mode = settings.getMode().toLowerCase();

        if (mode.equals("toggle")) {
            if (settings.getStartKey().equalsIgnoreCase(settings.getStopKey())) {
                if (keyPressed.equalsIgnoreCase(settings.getStartKey())) {
                    if (Clicker.isClicking()) {
                        Clicker.stopAutoClicker();
                    } else {
                        Clicker.startAutoClicker();
                    }
                }
            } else {
                if (keyPressed.equalsIgnoreCase(settings.getStartKey())) {
                    Clicker.startAutoClicker();
                }
                if (keyPressed.equalsIgnoreCase(settings.getStopKey())) {
                    Clicker.stopAutoClicker();
                }
            }
        } else if (mode.equals("tap")) {
            if (keyPressed.equalsIgnoreCase(settings.getTapKey())) {
                if (!Clicker.isClicking()) {
                    Clicker.startAutoClicker();
                }
            }
        } else { // hold mode.
            if (keyPressed.equalsIgnoreCase(settings.getHoldKey())) {
                Clicker.startAutoClicker();
            }
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        SettingsManager sm = new SettingsManager();
        UserSettings settings = sm.loadSettings();
        String keyReleased = NativeKeyEvent.getKeyText(e.getKeyCode());
        String mode = settings.getMode().toLowerCase();

        if (mode.equals("hold")) {
            if (keyReleased.equalsIgnoreCase(settings.getHoldKey())) {
                Clicker.stopAutoClicker();
            }
        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        // No action required.
    }
}
