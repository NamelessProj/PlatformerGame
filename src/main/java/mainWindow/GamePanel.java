package mainWindow;

import constants.GameConstants;
import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class GamePanel extends JPanel {
    private MouseInputs mouseInputs;
    private float xDelta = 100, yDelta = 100;
    private BufferedImage img, subImg;

    public GamePanel() {
        mouseInputs = new MouseInputs(this);
        importImages();
        setPanelSize();
        this.addKeyListener(new KeyboardInputs(this));
        this.addMouseListener(mouseInputs);
        this.addMouseMotionListener(mouseInputs);
    }

    private void importImages() {
        InputStream is = getClass().getResourceAsStream("/images/player_sprites.png");
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setPanelSize() {
        Dimension size = new Dimension(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
        this.setMinimumSize(size);
        this.setPreferredSize(size);
        this.setMaximumSize(size);
    }

    public void changeXDelta(int val) {
        this.xDelta += val;
    }

    public void changeYDelta(int val) {
        this.yDelta += val;
    }

    public void setRectPos(int x, int y) {
        this.xDelta = x;
        this.yDelta = y;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        subImg = img.getSubimage(GameConstants.PLAYER_IMAGE_WIDTH, 8 * GameConstants.PLAYER_IMAGE_HEIGHT, GameConstants.PLAYER_IMAGE_WIDTH, GameConstants.PLAYER_IMAGE_HEIGHT);
        g.drawImage(subImg, (int) xDelta, (int) yDelta, GameConstants.IMAGE_WIDTH, GameConstants.IMAGE_HEIGHT, null);
    }
}