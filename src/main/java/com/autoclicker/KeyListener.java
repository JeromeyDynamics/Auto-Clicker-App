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

    /**
     * Registers a global key listener to listen for native key events.
     * The listener uses Swing's event dispatch thread to dispatch events.
     * If the native hook registration fails, the program will exit with status code
     * 1.
     * 
     * @throws NativeHookException if the native hook registration fails
     */
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

    /**
     * Checks if the auto-clicker should be activated based on the user's settings.
     * If double-key activation is not required, this method will return true.
     * If double-key activation is required, this method will return true if and
     * only if the primary and secondary keys are both pressed.
     * 
     * @param settings  the user's settings
     * @param primary   the primary key to check
     * @param secondary the secondary key to check
     * @return true if the auto-clicker should be activated, false otherwise
     */
    private boolean activationOK(UserSettings settings, String primary, String secondary) {
        if (!settings.isRequireDoubleKey()) {
            return true;
        } else {
            // When double-key activation is required, both the primary and secondary keys
            // must be pressed.
            return pressedKeys.contains(primary) && pressedKeys.contains(secondary);
        }
    }

    /**
     * Listens for native key press events and controls the auto-clicker based on
     * the user's settings. If auto-clicking is disabled, this method will not do
     * anything. If the settings window is focused, this method will not do
     * anything except for the stop key, which is always processed regardless of
     * focus. The method will also not do anything if the activation conditions
     * are not met (i.e. the primary and secondary keys are not both pressed when
     * double-key activation is required).
     * 
     * @param e The native key event that triggered this method.
     */
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

    /**
     * When a key is released, stop clicking if the mode is "hold" and the released
     * key is the hold key.
     * 
     * @param e The event describing the released key.
     */
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

    /**
     * This method is called when a key is typed (pressed and released).
     * Currently, no action is needed for key typed events, so this method
     * does not perform any operations.
     *
     * @param e The native key event that triggered this method.
     */
    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        // No action needed on key typed.
    }
}
