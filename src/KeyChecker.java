import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyChecker extends KeyAdapter {
    private static int enableKey;
    private static int disableKey;
    private static int pushKey;
    private static boolean isOnPushMode = false;

    public static void onPushMode(boolean isOnPushMode) {
        System.out.println("Push button set to " + isOnPushMode);
        KeyChecker.isOnPushMode = isOnPushMode;
    }

    public static void setPushKey(String key) {
        System.out.println("Push key set to " + key);
        pushKey = Keys.getKeyEvent(key);
    }

    public static void setEnableKey(String key) {
        System.out.println("Enable key set to " + key);
        enableKey = Keys.getKeyEvent(key);
    }

    public static void setDisableKey(String key) {
        System.out.println("Disable key set to " + key);
        disableKey = Keys.getKeyEvent(key);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Key " + e.getKeyChar() + " is being pressed!");
        if (isOnPushMode) {
            if (e.getKeyCode() == pushKey) {
                System.out.println("Clicking Started!");
                Clicker.setClicking(true); // Start clicking
            }
        } else {
            if (e.getKeyCode() == enableKey) {
                System.out.println("Clicking Started!");
                Clicker.setClicking(true); // Start clicking
            } else if (e.getKeyCode() == disableKey) {
                System.out.println("Clicking Stopped!");
                Clicker.setClicking(false); // Stop clicking
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (isOnPushMode && e.getKeyCode() == pushKey) {
            System.out.println("Clicking Stopped!");
            Clicker.setClicking(false); // Stop clicking when key is released
        }
    }
}
