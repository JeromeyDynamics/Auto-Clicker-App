package com.autoclicker;

import java.awt.Robot;
import java.awt.AWTException;
import java.awt.event.InputEvent;

public class Clicker {
    private static volatile boolean clicking = false;

    public static synchronized void startAutoClicker() {
        // Load settings and check disable flag.
        SettingsManager sm = new SettingsManager();
        UserSettings settings = sm.loadSettings();
        if (settings.isAutoClickerDisabled()) {
            return; // Do not start clicking if disabled.
        }

        if (clicking) {
            return; // Prevent duplicate threads.
        }
        clicking = true;
        new Thread(() -> {
            try {
                Robot robot = new Robot();
                String mode = settings.getMode().toLowerCase();

                if (mode.equals("toggle")) {
                    if (settings.isToggleUnlimited()) {
                        while (clicking) {
                            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                            Thread.sleep(50);
                        }
                    } else {
                        long endTime = System.currentTimeMillis() + (settings.getToggleDuration() * 1000);
                        while (clicking && System.currentTimeMillis() < endTime) {
                            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                            Thread.sleep(50);
                        }
                        clicking = false;
                    }
                } else if (mode.equals("tap")) {
                    long endTime = System.currentTimeMillis() + (settings.getTapDuration() * 1000);
                    while (clicking && System.currentTimeMillis() < endTime) {
                        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                        Thread.sleep(50);
                    }
                    clicking = false;
                } else { // hold mode.
                    while (clicking) {
                        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                        Thread.sleep(50);
                    }
                }
            } catch (AWTException | InterruptedException ex) {
                ex.printStackTrace();
            } finally {
                clicking = false;
            }
        }).start();
    }

    public static synchronized void stopAutoClicker() {
        clicking = false;
    }

    public static boolean isClicking() {
        return clicking;
    }
}
