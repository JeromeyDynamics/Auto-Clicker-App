package test;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

public class ClickerTest {
    private static volatile boolean beClicking = false; // Use volatile for thread safety
    private static Thread clickThread;

    public static boolean isClicking() {
        return beClicking;
    }

    public static void setClicking(boolean clicking) {
        if (clicking && !beClicking) {
            System.out.println("Clicking!");
            beClicking = true;
            startClicking();
        } else if (!clicking && beClicking) {
            beClicking = false; // Stop clicking
            if (clickThread != null) {
                try {
                    clickThread.join(); // Wait for the thread to finish
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void startClicking() {
        clickThread = new Thread(() -> {
            try {
                Robot robot = new Robot();
                while (beClicking) {
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    Thread.sleep(5); // Adjust the delay as needed
                }
            } catch (AWTException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        clickThread.start();
    }

    public static void main(String[] args) {
        System.out.println("Clicks for 5 seconds");

        setClicking(true); // Start clicking

        try {
            Thread.sleep(5000); // Click for 10 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setClicking(false); // Stop clicking
    }
}
