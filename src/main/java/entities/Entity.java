package entities;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitbox;
    protected int animationTick, animationIndex;

    /**
     * Constructor for the Entity class.
     *
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
     *
     * @param x      The x-coordinate of the hitbox.
     * @param y      The y-coordinate of the hitbox.
     * @param width  The width of the hitbox.
     * @param height The height of the hitbox.
     */
    protected void initHitbox(float x, float y, int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, width, height);
    }

    /**
     * For debugging purposes, draws the hitbox of the entity.
     */
    protected void drawHitbox(Graphics g, int xLvlOffset) {
        g.setColor(Color.PINK);
        g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    /*protected void updateHitbox() {
        hitbox.x = (int) x;
        hitbox.y = (int) y;
    }*/

    /**
     * Get the hitbox of the entity.
     *
     * @return The hitbox of the entity as a Rectangle2D.Float object.
     */
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }
}