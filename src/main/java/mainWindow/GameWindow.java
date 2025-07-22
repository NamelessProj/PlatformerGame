package mainWindow;

import javax.swing.JFrame;

import utils.LoadSave;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;

public class GameWindow extends JFrame {
    /**
     * Constructor for the GameWindow class.
     * @param gamePanel the {@link GamePanel} instance that will be added to this window
     */
    public GameWindow(GamePanel gamePanel) {
        this.setTitle("A Pirate's Adventure");
        this.setIconImage(getIcon());
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

    /**
     * Returns the icon image for the game window.
     * @return the {@link BufferedImage} representing the icon
     */
    private BufferedImage getIcon() {
        return LoadSave.GetSpriteAtlas(LoadSave.Images.ICON);
    }
}