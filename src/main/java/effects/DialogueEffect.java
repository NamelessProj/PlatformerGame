package effects;

import static utils.Constants.Dialogue.GetSpriteAmount;
import static utils.Constants.GameConstants.ANIMATION_SPEED;

public class DialogueEffect {
    private int x, y, type;
    private int animationIndex, animationTick;
    private boolean active = true;

    /**
     * Creates a new DialogueEffect instance.
     * @param x The x-coordinate of the effect.
     * @param y The y-coordinate of the effect.
     * @param type The type of the effect, which determines the sprite used.
     */
    public DialogueEffect(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    /**
     * Updates the animation state of the effect.
     */
    public void update() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= GetSpriteAmount(type)) {
                active = false;
                animationIndex = 0;
            }
        }
    }

    /**
     * Deactivates the effect, marking it as no longer active.
     */
    public void deactivate() {
        active = false;
    }

    /**
     * Resets the effect to a new position and reactivates it.
     * @param x The new x-coordinate of the effect.
     * @param y The new y-coordinate of the effect.
     */
    public void reset(int x, int y) {
        this.x = x;
        this.y = y;
        active = true;
    }

    /**
     * Gets the current animation index of the effect.
     * @return The current animation index.
     */
    public int getAnimationIndex() {
        return animationIndex;
    }

    /**
     * Gets the x-coordinate of the effect.
     * @return The x-coordinate of the effect.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the effect.
     * @return The y-coordinate of the effect.
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the type of the effect.
     * @return The type of the effect, which determines the sprite used.
     */
    public int getType() {
        return type;
    }

    /**
     * Checks if the effect is currently active.
     * @return true if the effect is active, false otherwise.
     */
    public boolean isActive() {
        return active;
    }
}