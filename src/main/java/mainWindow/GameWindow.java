package mainWindow;

import javax.swing.JFrame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameWindow extends JFrame {
    /**
     * Constructor for the GameWindow class.
     * @param gamePanel the GamePanel instance that will be added to this window
     */
    public GameWindow(GamePanel gamePanel) {
        this.setTitle("Game Window");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(gamePanel);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.getGame().windowFocusLost();
            }

            @Override
            public void windowGainedFocus(WindowEvent e) {}
        });
    }
}