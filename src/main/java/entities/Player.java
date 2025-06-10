package entities;

import utils.LoadSave;

import static utils.Constants.GameConstants.SCALE;
import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.CanMoveHere;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    private BufferedImage[][] animations;
    private int animationTick, animationIndex, animationSpeed = 25;
    private int playerAction = IDLE;
    private boolean moving = false, attacking = false;
    private boolean left, up, right, down;
    private float playerSpeed = 2.0f;
    private int[][] lvlData;
    private float xDrawOffset = 21 * SCALE;
    private float yDrawOffset = 4 * SCALE;

    /**
     * Constructor for the Player class.
     *
     * @param x      The x-coordinate of the player.
     * @param y      The y-coordinate of the player.
     * @param width  The width of the player.
     * @param height The height of the player.
     */
    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        initHitbox(x, y, PLAYER_WIDTH * SCALE, PLAYER_HEIGHT * SCALE);
    }

    /**
     * Updates the player's position and animation state.
     */
    public void update() {
        updatePosition();
        updateAnimationTick();
        setAnimation();
    }

    /**
     * Draws the player on the screen.
     *
     * @param g The Graphics object used for drawing.
     */
    public void render(Graphics g) {
        g.drawImage(animations[playerAction][animationIndex], (int) (hitbox.x - xDrawOffset), (int) (hitbox.y - yDrawOffset), width, height, null);
        drawHitbox(g);
    }

    /**
     * Updates the animation tick and handles the animation index.
     */
    private void updateAnimationTick() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= GetSpriteAmount(playerAction)) {
                animationIndex = 0;
                attacking = false;
            }
        }
    }

    /**
     * Sets the current animation based on the player's action.
     */
    private void setAnimation() {
        int startAnimation = playerAction;

        if (moving)
            playerAction = RUNNING;
        else
            playerAction = IDLE;

        if (attacking)
            playerAction = ATTACK_1;

        if (startAnimation != playerAction)
            resetAnimationTick();
    }

    /**
     * Resets the animation tick and index to start the animation from the beginning.
     */
    private void resetAnimationTick() {
        animationTick = 0;
        animationIndex = 0;
    }

    /**
     * Updates the player's position based on the movement keys pressed.
     */
    private void updatePosition() {
        moving = false;

        if (!left && !right && !up && !down)
            return;

        float xSpeed = 0, ySpeed = 0;

        if (left && !right)
            xSpeed = -playerSpeed;
        else if (right && !left)
            xSpeed = playerSpeed;

        if (up && !down)
            ySpeed = -playerSpeed;
        else if (down && !up)
            ySpeed = playerSpeed;

        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y + ySpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
            hitbox.y += ySpeed;
            moving = true;
        }
    }

    /**
     * Loads the player animations from the sprite atlas.
     */
    private void loadAnimations() {
        BufferedImage playerSprites = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

        animations = new BufferedImage[NUM_ANIMATIONS][MAX_NUM_SPRITES];
        for (int j = 0; j < NUM_ANIMATIONS; j++)
            for (int i = 0; i < MAX_NUM_SPRITES; i++)
                animations[j][i] = playerSprites.getSubimage(i * IMAGE_WIDTH, j * IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    /**
     * Loads the level data for collision detection.
     *
     * @param lvlData The 2D array representing the level data.
     */
    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
    }

    /**
     * Resets the player's direction booleans to false.
     */
    public void resetDirBooleans() {
        left = false;
        up = false;
        right = false;
        down = false;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
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