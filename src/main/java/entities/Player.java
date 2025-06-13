package entities;

import utils.LoadSave;

import static utils.Constants.GameConstants.SCALE;
import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    private BufferedImage[][] animations;
    private int animationTick, animationIndex, animationSpeed = 25;
    private int playerAction = IDLE;
    private boolean moving = false, attacking = false;
    private boolean left, right, jump;
    private float playerSpeed = 1.0f * SCALE;
    private int[][] lvlData;
    private float xDrawOffset = 21 * SCALE;
    private float yDrawOffset = 4 * SCALE;

    // Jumping / Gravity
    private float airSpeed = 0f;
    private float gravity = 0.04f * SCALE;
    private float jumpSpeed = -2.25f * SCALE;
    private float fallSpeedAfterCollision = 0.5f * SCALE;
    private boolean inAir = false;

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
        initHitbox(x, y, (int) (PLAYER_WIDTH * SCALE), (int) (PLAYER_HEIGHT * SCALE));
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
    public void render(Graphics g, int xLvlOffset) {
        g.drawImage(animations[playerAction][animationIndex], (int) (hitbox.x - xDrawOffset) - xLvlOffset, (int) (hitbox.y - yDrawOffset), width, height, null);
        //drawHitbox(g);
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

        if (inAir) {
            if (airSpeed < 0)
                playerAction = JUMP;
            else
                playerAction = FALLING;
        }

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

        if (jump)
            jump();

        if (!inAir)
            if ((!left && !right) || (left && right))
                return;

        float xSpeed = 0;

        if (left)
            xSpeed -= playerSpeed;

        if (right)
            xSpeed += playerSpeed;

        if (!inAir && !IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
        
        if (inAir) {
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
            } else {
                hitbox.y = GetEntityYPositionUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
            }
        }

        updateXPosition(xSpeed);
        moving = true;
    }

    private void jump() {
        if (inAir)
            return;

        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPosition(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            hitbox.x += xSpeed;
        else {
            hitbox.x = GetEntityXPositionNextToWall(hitbox, xSpeed);
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
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    /**
     * Resets the player's direction booleans to false.
     */
    public void resetDirBooleans() {
        left = false;
        right = false;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }
}