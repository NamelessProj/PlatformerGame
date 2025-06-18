package objects;

import static utils.Constants.GameConstants.SCALE;
import static utils.Constants.GameConstants.TILES_SIZE;

public class Cannon extends GameObject {
    private int tileY;
    
    public Cannon(int x, int y, int objectType) {
        super(x, y, objectType);
        tileY = y / TILES_SIZE;
        this.initHitbox(40, 26);
        this.hitbox.x -= (int) (4 * SCALE);
        this.hitbox.y += (int) (6 * SCALE);
    }

    public void update() {
        if (doAnimation)
            updateAnimationTick();
    }

    public int getTileY() {
        return tileY;
    }
}