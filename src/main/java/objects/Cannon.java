package objects;

import static utils.Constants.GameConstants.SCALE;
import static utils.Constants.GameConstants.TILES_SIZE;

public class Cannon extends GameObject {
    private int tileY;
    
    /**
     * Constructor for the Cannon class.
     * @param x the x-coordinate of the cannon
     * @param y the y-coordinate of the cannon
     * @param objectType the type of the object ({@link utils.Constants.ObjectConstants#CANNON_LEFT} or {@link utils.Constants.ObjectConstants#CANNON_RIGHT})
     */
    public Cannon(int x, int y, int objectType) {
        super(x, y, objectType);
        tileY = y / TILES_SIZE;
        this.initHitbox(40, 26);
        this.hitbox.x -= (int) (4 * SCALE);
        this.hitbox.y += (int) (6 * SCALE);
    }

    /**
     * Updates the animation tick for the cannon.
     */
    public void update() {
        if (doAnimation)
            updateAnimationTick();
    }

    /**
     * Returns the tile Y coordinate of the cannon.
     * @return the tile Y coordinate
     */
    public int getTileY() {
        return tileY;
    }
}