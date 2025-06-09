package entities;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Color;

public abstract class Entity {
    protected float x, y;
    protected int width, height;
    protected Rectangle hitbox;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        initHitbox();
    }

    private void initHitbox() {
        hitbox = new Rectangle((int) x, (int) y, width, height);
    }

    /**
     * For debugging purposes, draws the hitbox of the entity.
     */
    protected void drawHitbox(Graphics g) {
        g.setColor(Color.PINK);
        g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }

    protected void updateHitbox() {
        hitbox.x = (int) x;
        hitbox.y = (int) y;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}