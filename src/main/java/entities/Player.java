package entities;

import utils.Constants;

import javax.imageio.ImageIO;

import static utils.Constants.PlayerConstants.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Player extends Entity {
    private BufferedImage[][] animations;
    private int animationTick, animationIndex, animationSpeed = 15;
    private int playerAction = IDLE;
    private boolean moving = false;
    private boolean left, up, right, down;
    private float playerSpeed = 2.0f;

    public Player(float x, float y) {
        super(x, y);
        loadAnimations();
    }

    public void update() {
        updatePosition();
        updateAnimationTick();
        setAnimation();
    }

    public void render(Graphics g) {
        g.drawImage(animations[playerAction][animationIndex], (int) x, (int) y, Constants.GameConstants.IMAGE_WIDTH, Constants.GameConstants.IMAGE_HEIGHT, null);
    }

    private void updateAnimationTick() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= GetSpriteAmount(playerAction))
                animationIndex = 0;
        }
    }

    private void setAnimation() {
        if (moving)
            playerAction = RUNNING;
        else
            playerAction = IDLE;
    }

    private void updatePosition() {
        moving = false;

        if (left && !right) {
            x -= playerSpeed;
            moving = true;
        } else if (right && !left) {
            x += playerSpeed;
            moving = true;
        }

        if (up && !down) {
            y -= playerSpeed;
            moving = true;
        } else if (down && !up) {
            y += playerSpeed;
            moving = true;
        }
    }

    private void loadAnimations() {
        InputStream is = getClass().getResourceAsStream("/images/player_sprites.png");
        try {
            BufferedImage playerSprites = ImageIO.read(is);

            animations = new BufferedImage[NUM_ANIMATIONS][MAX_NUM_SPRITES];
            for (int j = 0; j < NUM_ANIMATIONS; j++)
                for (int i = 0; i < MAX_NUM_SPRITES; i++)
                    animations[j][i] = playerSprites.getSubimage(i * IMAGE_WIDTH, j * IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);
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

    public void resetDirBooleans() {
        left = false;
        up = false;
        right = false;
        down = false;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }
}