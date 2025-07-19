package entities;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

import static utils.Constants.Directions.*;
import static utils.Constants.GameConstants.SCALE;
import static utils.Constants.Saves.DATA_SEPARATOR;
import static utils.HelpMethods.CanMoveHere;

public abstract class Entity {
    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitbox;
    protected int animationTick, animationIndex;
    protected int state;
    protected float airSpeed;
    protected boolean inAir = false;
    protected int maxHealth;
    protected int currentHealth;
    protected Rectangle2D.Float attackBox;
    protected float walkSpeed;

    protected int pushBackDirection;
    protected float pushDrawOffset;
    protected int pushBackOffsetDirection = UP;

    /**
     * Constructor for the Entity class.
     * @param x      The x-coordinate of the entity.
     * @param y      The y-coordinate of the entity.
     * @param width  The width of the entity.
     * @param height The height of the entity.
     */
    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Initializes the hitbox of the entity.
     * @param width  The width of the hitbox.
     * @param height The height of the hitbox.
     */
    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int) (width * SCALE), (int) (height * SCALE));
    }

    /**
     * Updates the entity's animation tick and index based on the current state.
     */
    protected void updatePushBackDrawOffset() {
        float speed = 0.95f;
        float limit = -30f;

        if (pushBackOffsetDirection == UP) {
            pushDrawOffset -= speed;
            if (pushDrawOffset <= limit)
                pushBackOffsetDirection = DOWN;
        } else {
            pushDrawOffset += speed;
            if (pushDrawOffset >= 0)
                pushDrawOffset = 0;
        }
    }

    /**
     * Pushes the entity back in the specified direction with a speed multiplier.
     * @param pushBackDirection The direction to push back the entity (LEFT or RIGHT).
     * @param speedMulti The multiplier for the speed of the push back.
     * @param lvlData The level data containing information about the environment.
     */
    protected void pushBack(int pushBackDirection, float speedMulti, int[][] lvlData) {
        float xSpeed = walkSpeed;
        if (pushBackDirection == LEFT)
            xSpeed = -walkSpeed;

        if (CanMoveHere(hitbox.x + xSpeed * speedMulti, hitbox.y, hitbox.width, hitbox.height, lvlData))
            hitbox.x += xSpeed * speedMulti;
    }

    /**
     * For debugging purposes, draws the hitbox of the entity.
     * @param g The Graphics object used for drawing.
     * @param xLvlOffset The x-coordinate offset for the level, used to adjust the
     */
    protected void drawHitbox(Graphics g, int xLvlOffset) {
        g.setColor(Color.PINK);
        g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    protected void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.PINK);
        g.drawRect((int) attackBox.x - xLvlOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    /**
     * Get the hitbox of the entity.
     * @return The hitbox of the entity as a Rectangle2D.Float object.
     */
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    /**
     * Get the current state of the entity.
     * @return The current state of the entity as an integer.
     */
    protected int getState() {
        return state;
    }

    /**
     * Get the current animation index of the entity.
     * @return The current animation index of the entity as an integer.
     */
    protected int getAnimationIndex() {
        return animationIndex;
    }

    /**
     * Get the current health of the entity.
     * @return The current health of the entity as an integer.
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Sets the current health of the entity.
     * @param state The new state to set for the entity.
     */
    protected void newState(int state) {
        this.state = state;
        animationTick = 0;
        animationIndex = 0;
    }

    /**
     * Sets the push back direction for the entity.
     * @param pushBackDirection The direction to push back the entity (LEFT or RIGHT).
     */
    public void setPushBackDirection(int pushBackDirection) {
        this.pushBackDirection = pushBackDirection;
    }

    /**
     * Converts an array of integers to a string representation.
     * @param arr The array of integers to convert.
     * @return A string representation of the array, with elements separated by DATA_SEPARATOR.
     * @see utils.Constants.Saves#DATA_SEPARATOR
     */
    protected String toString(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i : arr)
            sb.append(i).append(DATA_SEPARATOR);

        if (sb.length() > 0) // Remove the last separator
            sb.setLength(sb.length() - 1);

        return sb.toString();
    }
}