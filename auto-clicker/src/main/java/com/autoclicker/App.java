package com.autoclicker;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class App implements NativeKeyListener {

    // Counter for key 'A' presses
    private int aCounter = 0;
    private TrayIcon trayIcon;

    public static void main(String[] args) {
        // Create an instance and set up tray and key listener
        App app = new App();
        app.setupTray();
        app.registerGlobalKeyListener();
    }

    /**
     * Sets up the system tray icon along with a simple popup menu.
     */
    private void setupTray() {
        if (!SystemTray.isSupported()) {
            System.err.println("System tray is not supported on this system.");
            return;
        }
        SystemTray tray = SystemTray.getSystemTray();
        // Create an initial image showing count 0
        Image image = createTrayImage(aCounter);

        // Create a popup menu with an exit option.
        PopupMenu popup = new PopupMenu();
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    GlobalScreen.unregisterNativeHook();
                } catch (NativeHookException ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        });
        popup.add(exitItem);

        // Create and configure the TrayIcon.
        trayIcon = new TrayIcon(image, "Key count: " + aCounter, popup);
        trayIcon.setImageAutoSize(true);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println("TrayIcon could not be added.");
            e.printStackTrace();
        }
    }

    /**
     * Creates an image for the tray icon showing the current count.
     *
     * @param count The number to display.
     * @return An Image object with the count drawn on it.
     */
    private Image createTrayImage(int count) {
        int width = 16, height = 16;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        // Clear image with transparency.
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, width, height);
        g.setComposite(AlphaComposite.SrcOver);

        // Draw a blue circle as background.
        g.setColor(Color.BLUE);
        g.fillOval(0, 0, width, height);

        // Draw the counter in white.
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        String text = String.valueOf(count);
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        int x = (width - textWidth) / 2;
        int y = (height + textHeight) / 2 - 2;
        g.drawString(text, x, y);

        g.dispose();
        return image;
    }

    /**
     * Updates the tray icon image and tooltip with the current key count.
     */
    private void updateTrayIcon() {
        trayIcon.setToolTip("Key count: " + aCounter);
        trayIcon.setImage(createTrayImage(aCounter));
    }

    /**
     * Registers the global key listener using JNativeHook.
     */
    private void registerGlobalKeyListener() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            ex.printStackTrace();
            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(this);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        // Check if key 'A' is pressed (case-insensitive)
        if (e.getKeyCode() == NativeKeyEvent.VC_A) {
            aCounter++;
            updateTrayIcon();
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        // No action on key release.
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        // No action on key typed.
    }
}
