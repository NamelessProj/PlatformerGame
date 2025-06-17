package objects;

import static utils.Constants.GameConstants.SCALE;
import static utils.Constants.ObjectConstants.POTION_HOVER_DELTA;

public class Potion extends GameObject {
    private float hoverOffset, hoverSpeed = 0.075f * SCALE;
    private int maxHoverOffset, hoverDirection = 1;
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

        // Ease-in function for smooth hovering
        float t = easeInOutBack(hoverTime);
        if (hoverDirection == 1)
            hoverOffset = t * maxHoverOffset;
        else
            hoverOffset = (1 - t) * maxHoverOffset;

        hitbox.y = y + hoverOffset;
    }

    private float easeInOutBack(float x) {
        final float c1 = 1.70158f;
        final float c2 = c1 * 1.525f;

        if (x < 0.5f)
            return (float) ((Math.pow(2 * x, 2) * ((c2 + 1) * 2 * x - c2)) / 2);
        else
            return (float) ((Math.pow(2 * x - 2, 2) * ((c2 + 1) * (2 * x - 2) + c2) + 2) / 2);
    }
}