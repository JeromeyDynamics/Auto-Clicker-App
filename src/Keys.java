import java.awt.event.KeyEvent;

public class Keys {
    public static int getKeyEvent(String keyName) {
        switch (keyName) {
            case "a":
                return KeyEvent.VK_A;
            case "b":
                return KeyEvent.VK_B;
            case "c":
                return KeyEvent.VK_C;
            case "d":
                return KeyEvent.VK_D;
            case "e":
                return KeyEvent.VK_E;
            case "f":
                return KeyEvent.VK_F;
            case "g":
                return KeyEvent.VK_G;
            case "h":
                return KeyEvent.VK_H;
            case "i":
                return KeyEvent.VK_I;
            case "j":
                return KeyEvent.VK_J;
            case "k":
                return KeyEvent.VK_K;
            case "l":
                return KeyEvent.VK_L;
            case "m":
                return KeyEvent.VK_M;
            case "n":
                return KeyEvent.VK_N;
            case "o":
                return KeyEvent.VK_O;
            case "p":
                return KeyEvent.VK_P;
            case "q":
                return KeyEvent.VK_Q;
            case "r":
                return KeyEvent.VK_R;
            case "s":
                return KeyEvent.VK_S;
            case "t":
                return KeyEvent.VK_T;
            case "u":
                return KeyEvent.VK_U;
            case "v":
                return KeyEvent.VK_V;
            case "w":
                return KeyEvent.VK_W;
            case "x":
                return KeyEvent.VK_X;
            case "y":
                return KeyEvent.VK_Y;
            case "z":
                return KeyEvent.VK_Z;
            case "F1":
                return KeyEvent.VK_F1;
            case "F2":
                return KeyEvent.VK_F2;
            case "F3":
                return KeyEvent.VK_F3;
            case "F4":
                return KeyEvent.VK_F4;
            case "F5":
                return KeyEvent.VK_F5;
            case "F6":
                return KeyEvent.VK_F6;
            case "F7":
                return KeyEvent.VK_F7;
            case "F8":
                return KeyEvent.VK_F8;
            case "F9":
                return KeyEvent.VK_F9;
            case "F10":
                return KeyEvent.VK_F10;
            case "F11":
                return KeyEvent.VK_F11;
            case "F12":
                return KeyEvent.VK_F12;
            default:
                throw new IllegalArgumentException("Invalid key name: " + keyName);
        }
    }
}
