package objects;

import static utils.Constants.GameConstants.SCALE;

public class Potion extends GameObject {
    public Potion(int x, int y, int objectType) {
        super(x, y, objectType);
        this.doAnimation = true;
        this.initHitbox(7, 14);
        this.xDrawOffset = (int) (3 * SCALE);
        this.yDrawOffset = (int) (2 * SCALE);
    }

    public void update() {
        updateAnimationTick();
    }
}