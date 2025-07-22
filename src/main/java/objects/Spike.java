package objects;

import static utils.Constants.GameConstants.SCALE;

public class Spike extends GameObject {
    /**
     * Constructor for the Spike class.
     * @param x the x-coordinate of the spike
     * @param y the y-coordinate of the spike
     * @param objectType the type of the object ({@link utils.Constants.ObjectConstants#SPIKE})
     */
    public Spike(int x, int y, int objectType) {
        super(x, y, objectType);
        initHitbox(32, 16);
        this.xDrawOffset = 0;
        this.yDrawOffset = (int) (16 * SCALE);
        this.hitbox.y += yDrawOffset;
    }
}