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
