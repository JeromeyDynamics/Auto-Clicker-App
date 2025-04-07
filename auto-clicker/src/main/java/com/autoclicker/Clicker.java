package com.autoclicker;

import java.awt.Robot;
import java.awt.AWTException;
import java.awt.event.InputEvent;

public class Clicker {
    private static volatile boolean clicking = false;
    private static UserSettings settings;

    public Clicker() {
        settings = new UserSettings();
    }

    public static synchronized void startAutoClicker() {
        if (clicking) {
            return; // Auto-clicker is already running
        }
        clicking = true;
        new Thread(() -> {
            try {
                Robot robot = new Robot();
                long endTime = System.currentTimeMillis() + (settings.getClickDuration() * 1000);
                while (clicking && System.currentTimeMillis() < endTime) {
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    Thread.sleep(50); // Adjust the sleep time to control click speed
                }
                clicking = false; // Ensure clicking stops after duration
            } catch (AWTException | InterruptedException ex) {
                ex.printStackTrace();
                clicking = false; // Ensure clicking stops in case of exception
            }
        }).start();
    }

    public static synchronized void stopAutoClicker() {
        clicking = false;
    }
}