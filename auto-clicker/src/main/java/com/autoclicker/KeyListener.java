package com.autoclicker;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.dispatcher.SwingDispatchService;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.util.HashSet;
import java.util.Set;

public class KeyListener implements NativeKeyListener {
    // Maintain a set of currently pressed key texts.
    private static final Set<String> pressedKeys = new HashSet<>();

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

    // Helper method to verify that activation conditions are met.
    private boolean activationOK(UserSettings settings, String primary, String secondary) {
        if (!settings.isRequireDoubleKey()) {
            return true;
        } else {
            // When double-key activation is required, both the primary and secondary keys
            // must be pressed.
            return pressedKeys.contains(primary) && pressedKeys.contains(secondary);
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        String keyText = NativeKeyEvent.getKeyText(e.getKeyCode());
        pressedKeys.add(keyText);

        SettingsManager sm = new SettingsManager();
        UserSettings settings = sm.loadSettings();

        // If auto clicking is disabled, ignore all key events.
        if (settings.isAutoClickerDisabled()) {
            return;
        }

        // Determine if the settings window is currently active.
        Window activeWindow = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
        boolean appFocused = (activeWindow == App.getSettingsFrame());

        // Stop key is always processed regardless of focus.
        if (keyText.equalsIgnoreCase(settings.getStopKey()) && Clicker.isClicking()) {
            Clicker.stopAutoClicker();
            return;
        }

        // Prevent processing activation keys when the settings window is focused.
        if (appFocused) {
            return;
        }

        String mode = settings.getMode().toLowerCase();
        if (mode.equals("toggle")) {
            // If start and stop keys are the same, toggle clicking on key press.
            if (settings.getStartKey().equalsIgnoreCase(settings.getStopKey())) {
                if (keyText.equalsIgnoreCase(settings.getStartKey()) &&
                        activationOK(settings, settings.getStartKey(), settings.getSecondaryStartKey())) {
                    if (Clicker.isClicking()) {
                        Clicker.stopAutoClicker();
                    } else {
                        Clicker.startAutoClicker();
                    }
                }
            } else {
                if (keyText.equalsIgnoreCase(settings.getStartKey()) &&
                        activationOK(settings, settings.getStartKey(), settings.getSecondaryStartKey())) {
                    Clicker.startAutoClicker();
                }
            }
        } else if (mode.equals("hold")) {
            if (keyText.equalsIgnoreCase(settings.getHoldKey()) &&
                    activationOK(settings, settings.getHoldKey(), settings.getSecondaryHoldKey())) {
                Clicker.startAutoClicker();
            }
        } else if (mode.equals("tap")) {
            if (keyText.equalsIgnoreCase(settings.getTapKey()) &&
                    activationOK(settings, settings.getTapKey(), settings.getSecondaryTapKey())) {
                if (!Clicker.isClicking()) {
                    Clicker.startAutoClicker();
                }
            }
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        String keyText = NativeKeyEvent.getKeyText(e.getKeyCode());
        pressedKeys.remove(keyText);

        SettingsManager sm = new SettingsManager();
        UserSettings settings = sm.loadSettings();
        String mode = settings.getMode().toLowerCase();

        if (mode.equals("hold") && keyText.equalsIgnoreCase(settings.getHoldKey())) {
            Clicker.stopAutoClicker();
        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        // No action needed on key typed.
    }
}
