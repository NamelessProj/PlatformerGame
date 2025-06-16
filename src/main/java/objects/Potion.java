package objects;

import static utils.Constants.GameConstants.SCALE;

public class Potion extends GameObject {
    private static class Direction {
        public static final int DOWN = 1;
        public static final int UP = -1;
    }

    private float hoverOffset, hoverSpeed = 0.075f * SCALE;
    private int maxHoverOffset, hoverDirection = Direction.DOWN;

    public Potion(int x, int y, int objectType) {
        super(x, y, objectType);
        this.doAnimation = true;
        this.initHitbox(7, 14);
        this.xDrawOffset = (int) (3 * SCALE);
        this.yDrawOffset = (int) (2 * SCALE);

        maxHoverOffset = (int) (8 * SCALE);
    }

    public void update() {
        updateAnimationTick();
        updateHover();
    }

    private void updateHover() {
        hoverOffset += hoverSpeed * hoverDirection;

        if (hoverOffset >= maxHoverOffset)
            hoverDirection = Direction.UP;
        else if (hoverOffset < 0)
            hoverDirection = Direction.DOWN;

        hitbox.y = y + hoverOffset;
    }
}