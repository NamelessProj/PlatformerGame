package objects;

import static utils.Constants.GameConstants.SCALE;
import static utils.Constants.ObjectConstants.POTION_HOVER_DELTA;

public class Potion extends GameObject {
    private static class Direction {
        public static final int DOWN = 1;
        public static final int UP = -1;
    }

    private float hoverOffset, hoverSpeed = 0.075f * SCALE;
    private int maxHoverOffset, hoverDirection = Direction.DOWN;
    float hoverTime = 0f, hoverDuration = 1.5f;
    private float pauseTimer = 0f;
    private float pauseDuration = 0.3f;

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
        if (pauseTimer > 0) {
            pauseTimer -= POTION_HOVER_DELTA;
            if (pauseTimer <= 0) {
                hoverDirection *= -1;
                hoverTime = 0f;
            }
            return;
        }

        hoverTime += POTION_HOVER_DELTA / hoverDuration;
        if (hoverTime > 1f) {
            hoverTime = 1;
            pauseTimer = pauseDuration;
        }

        // Ease-in
        float t = (float) Math.sin(hoverTime * Math.PI / 2);
        if (hoverDirection == Direction.DOWN)
            hoverOffset = t * maxHoverOffset;
        else
            hoverOffset = (1 - t) * maxHoverOffset;

        hitbox.y = y + hoverOffset;
    }
}