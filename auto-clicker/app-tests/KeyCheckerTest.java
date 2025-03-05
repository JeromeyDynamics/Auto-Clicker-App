package test;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

public class KeyCheckerTest extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            System.out.println("The special key is being pressed!");
        } else {
            System.out.println("Key " + e.getKeyChar() + " is being pressed!");
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Key Checker");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(new KeyCheckerTest());
        frame.setVisible(true);
    }
}
