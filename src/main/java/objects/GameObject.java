package objects;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utils.Constants.GameConstants.ANIMATION_SPEED;
import static utils.Constants.GameConstants.SCALE;
import static utils.Constants.ObjectConstants.*;

public class GameObject {
    protected int x, y, objectType;
    protected Rectangle2D.Float hitbox;
    protected boolean doAnimation, active = true;
    protected int animationTick, animationIndex;
    protected int xDrawOffset, yDrawOffset;

    /**
     * Constructor for the GameObject class.
     * @param x the x-coordinate of the game object
     * @param y the y-coordinate of the game object
     * @param objectType the type of the object (e.g., BARREL, BOX, CANNON_LEFT, CANNON_RIGHT)
     */
    public GameObject(int x, int y, int objectType) {
        this.x = x;
        this.y = y;
        this.objectType = objectType;
    }

    /**
     * Updates the animation tick for the game object.
     */
    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= GetSpriteAmount(objectType)) {
                animationIndex = 0;
                if (objectType == BARREL || objectType == BOX) {
                    doAnimation = false;
                    active = false;
                } else if (objectType == CANNON_LEFT || objectType == CANNON_RIGHT)
                    doAnimation = false;
            }
        }
    }

    /**
     * Resets the game object to its initial state.
     */
    public void reset() {
        active = true;
        animationTick = 0;
        animationIndex = 0;

        doAnimation = objectType != BARREL && objectType != BOX && objectType != CANNON_LEFT && objectType != CANNON_RIGHT;
    }

    /**
     * Initializes the hitbox for the game object based on its dimensions.
     * @param width the width of the hitbox
     * @param height the height of the hitbox
     */
    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int) (width * SCALE), (int) (height * SCALE));
    }

    /**
     * Draws the hitbox of the game object for debugging purposes.
     * @param g the Graphics object used for drawing
     * @param xLvlOffset the x-level offset for drawing the hitbox
     */
    public void drawHitbox(Graphics g, int xLvlOffset) {
        g.setColor(Color.PINK);
        g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    /**
     * Returns the y-draw offset for the game object.
     * @return the y-draw offset
     */
    public int getYDrawOffset() {
        return yDrawOffset;
    }

    /**
     * Returns the x-draw offset for the game object.
     * @return the x-draw offset
     */
    public int getXDrawOffset() {
        return xDrawOffset;
    }

    /**
     * Returns the type of the game object.
     * @return the type of the object
     */
    public int getObjectType() {
        return objectType;
    }

    /**
     * Returns the hitbox of the game object.
     * @return the hitbox as a Rectangle2D.Float object
     */
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    /**
     * Sets the active state of the game object.
     * @param active true if the object is active, false otherwise
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Checks if the game object is active.
     * @return true if the object is active, false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Returns the index of the current animation frame.
     * @return the animation index
     */
    public int getAnimationIndex() {
        return animationIndex;
    }

    /**
     * Returns the current animation tick count.
     * @return the animation tick count
     */
    public int getAnimationTick() {
        return animationTick;
    }

    /**
     * Sets whether the game object should animate.
     * @param doAnimation true to enable animation, false to disable
     */
    public void setDoAnimation(boolean doAnimation) {
        this.doAnimation = doAnimation;
    }
}