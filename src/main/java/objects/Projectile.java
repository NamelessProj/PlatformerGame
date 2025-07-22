package objects;

import java.awt.geom.Rectangle2D;

import static utils.Constants.GameConstants.SCALE;
import static utils.Constants.Projectiles.*;

public class Projectile {
    private Rectangle2D.Float hitbox;
    private int direction;
    private boolean active = true;

    /**
     * Constructor for the Projectile class.
     * @param x the x-coordinate of the projectile
     * @param y the y-coordinate of the projectile
     * @param direction the direction of the projectile ({@code 1} for right, {@code -1} for left)
     */
    public Projectile(int x, int y, int direction) {
        int xOffset = (int) (-3 * SCALE);
        int yOffset = (int) (5 * SCALE);

        if (direction == 1)
            xOffset = (int) (29 * SCALE);

        hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT);
        this.direction = direction;
    }

    /**
     * Updates the position of the projectile based on its direction.
     */
    public void updatePosition() {
        hitbox.x += direction * SPEED;
    }

    /**
     * Sets the position of the projectile's hitbox.
     * @param x the x-coordinate of the projectile
     * @param y the y-coordinate of the projectile
     */
    public void setPosition(int x, int y) {
        hitbox.x = x;
        hitbox.y = y;
    }

    /**
     * Returns the hitbox of the projectile.
     * @return the hitbox {@link Rectangle2D.Float} of the projectile
     */
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    /**
     * Sets the active state of the projectile.
     * @param active {@code true} to activate the projectile, {@code false} to deactivate it
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Checks if the projectile is active.
     * @return {@code true} if the projectile is active, {@code false} otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Returns the direction of the projectile.
     * @return the direction of the projectile ({@code 1} for right, {@code -1} for left)
     */
    public int getDirection() {
        return direction;
    }
}