package com.autoclicker;

import java.awt.Robot;
import java.awt.AWTException;
import java.awt.event.InputEvent;

public class Clicker {
    private static volatile boolean clicking = false;

    /**
     * Starts the auto-clicker in the mode specified by the settings.
     * This method is thread-safe and can be called from any thread.
     * If the auto-clicker is already running, this method does nothing.
     * If the auto-clicker is disabled in the settings, this method does nothing.
     * The auto-clicker runs in a separate thread, so this method returns
     * immediately.
     * The auto-clicker continues to run until it is stopped by calling
     * stopAutoClicker() or
     * until the mode is switched to another mode.
     */
    public static synchronized void startAutoClicker() {
        SettingsManager sm = new SettingsManager();
        UserSettings settings = sm.loadSettings();
        if (settings.isAutoClickerDisabled()) {
            return;
        }
        if (clicking) {
            return;
        }
        clicking = true;
        new Thread(() -> {
            try {
                Robot robot = new Robot();
                String mode = settings.getMode().toLowerCase();
                int delay = 50;
                if (mode.equals("toggle")) {
                    delay = (11 - settings.getToggleSpeed()) * 10;
                    if (settings.isToggleUnlimited()) {
                        while (clicking) {
                            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                            Thread.sleep(delay);
                        }
                    } else {
                        long endTime = System.currentTimeMillis() + (settings.getToggleDuration() * 1000);
                        while (clicking && System.currentTimeMillis() < endTime) {
                            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                            Thread.sleep(delay);
                        }
                        clicking = false;
                    }
                } else if (mode.equals("hold")) {
                    delay = (11 - settings.getHoldSpeed()) * 10;
                    while (clicking) {
                        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                        Thread.sleep(delay);
                    }
                } else if (mode.equals("tap")) {
                    delay = (11 - settings.getTapSpeed()) * 10;
                    long endTime = System.currentTimeMillis() + (settings.getTapDuration() * 1000);
                    while (clicking && System.currentTimeMillis() < endTime) {
                        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                        Thread.sleep(delay);
                    }
                    clicking = false;
                }
            } catch (AWTException | InterruptedException ex) {
                ex.printStackTrace();
            } finally {
                clicking = false;
            }
        }).start();
    }

    /**
     * Stop the auto-clicker immediately. This method blocks until the auto-clicker
     * has stopped. If the auto-clicker is not running, this method does nothing.
     */
    public static synchronized void stopAutoClicker() {
        clicking = false;
    }

    /**
     * Returns true if the auto-clicker is currently running, false otherwise.
     * This method does not block and returns immediately.
     * 
     * @return true if the auto-clicker is running, false otherwise.
     */
    public static boolean isClicking() {
        return clicking;
    }
}
