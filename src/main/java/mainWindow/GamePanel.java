package mainWindow;

import static utils.Constants.*;

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
    private BufferedImage playerSprites;
    private BufferedImage[][] animations;
    private int animationTick, animationIndex, animationSpeed = 15;
    private int playerAction = PlayerConstants.IDLE;
    private int playerDirection = -1;
    private boolean moving = false;

    public GamePanel() {
        mouseInputs = new MouseInputs(this);

        importImages();
        loadAnimations();
        setPanelSize();

        this.addKeyListener(new KeyboardInputs(this));
        this.addMouseListener(mouseInputs);
        this.addMouseMotionListener(mouseInputs);
    }

    private void importImages() {
        InputStream is = getClass().getResourceAsStream("/images/player_sprites.png");
        try {
            playerSprites = ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadAnimations() {
        animations = new BufferedImage[PlayerConstants.NUM_ANIMATIONS][PlayerConstants.MAX_NUM_SPRITES];

        for (int j = 0; j < PlayerConstants.NUM_ANIMATIONS; j++)
            for (int i = 0; i < PlayerConstants.MAX_NUM_SPRITES; i++)
                animations[j][i] = playerSprites.getSubimage(i * PlayerConstants.IMAGE_WIDTH, j * PlayerConstants.IMAGE_HEIGHT, PlayerConstants.IMAGE_WIDTH, PlayerConstants.IMAGE_HEIGHT);
    }

    private void setPanelSize() {
        Dimension size = new Dimension(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
        this.setMinimumSize(size);
        this.setPreferredSize(size);
        this.setMaximumSize(size);
    }

    public void setDirection(int direction) {
        this.playerDirection = direction;
        moving = true;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    private void updateAnimationTick() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= PlayerConstants.GetSpriteAmount(playerAction))
                animationIndex = 0;
        }
    }

    private void setAnimation() {
        if (moving)
            playerAction = PlayerConstants.RUNNING;
        else
            playerAction = PlayerConstants.IDLE;
    }

    private void updatePosition() {
        if (moving) {
            switch (playerDirection) {
                case Directions.LEFT -> xDelta -= 5;
                case Directions.UP -> yDelta -= 5;
                case Directions.RIGHT -> xDelta += 5;
                case Directions.DOWN -> yDelta += 5;
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateAnimationTick();
        setAnimation();
        updatePosition();
        g.drawImage(animations[playerAction][animationIndex], (int) xDelta, (int) yDelta, GameConstants.IMAGE_WIDTH, GameConstants.IMAGE_HEIGHT, null);
    }
}