import java.awt.Dimension;
import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        //new JFrame with the title Auto Clicker
        JFrame frame = new JFrame("Auto Clicker");
        //JFrames hold JPanels, JLabels, JButtons, etc
        frame.getContentPane();

        //creates App object app
        App app = new App();

        Dimension size = new Dimension(900, 900);
        app.setPreferredSize(size);
        app.setMinimumSize(size);
        app.setMaximumSize(size);

        //adds app object to frame
        frame.add(app);

        //frame set up
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //adding default settings
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}
