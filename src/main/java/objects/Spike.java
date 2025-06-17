package objects;

import static utils.Constants.GameConstants.SCALE;

public class Spike extends GameObject {
    public Spike(int x, int y, int objectType) {
        super(x, y, objectType);
        initHitbox(32, 16);
        this.xDrawOffset = 0;
        this.yDrawOffset = (int) (16 * SCALE);
        this.hitbox.y += yDrawOffset;
    }
}