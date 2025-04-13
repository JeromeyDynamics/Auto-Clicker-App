package com.autoclicker;

import java.awt.Robot;
import java.awt.AWTException;
import java.awt.event.InputEvent;

public class Clicker {
    private static volatile boolean clicking = false;

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

    public static synchronized void stopAutoClicker() {
        clicking = false;
    }

    public static boolean isClicking() {
        return clicking;
    }
}
