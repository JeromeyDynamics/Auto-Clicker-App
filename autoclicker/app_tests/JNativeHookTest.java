import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import java.util.HashSet;
import java.util.Set;

public class JNativeHookTest implements NativeKeyListener {
    private static final Set<Integer> pressedKeys = new HashSet<>();

    public static void main(String[] args) {
        try {
            GlobalScreen.registerNativeHook();
        } catch (Exception e) {
            e.printStackTrace();
        }

        GlobalScreen.addNativeKeyListener(new KeyListenerExample());
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        pressedKeys.add(e.getKeyCode()); // Add key to the set

        //detect if 'A' is pressed
        if (pressedKeys.contains(NativeKeyEvent.VC_A)) {
            System.out.println("A is being held down!");
        }

        //detect if 'F3' is pressed
        if (pressedKeys.contains(NativeKeyEvent.VC_F3)) {
            System.out.println("F3 is being held down!");
        }

        //detect if BOTH 'A' and 'F3' are pressed together
        if (pressedKeys.contains(NativeKeyEvent.VC_A) && pressedKeys.contains(NativeKeyEvent.VC_F3)) {
            System.out.println("A and F3 are both pressed!");
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }
}