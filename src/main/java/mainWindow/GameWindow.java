package mainWindow;

import javax.swing.JFrame;

public class GameWindow extends JFrame {
    public GameWindow(GamePanel gamePanel) {
        this.setSize(400, 400);
        this.setTitle("Game Window");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(gamePanel);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}