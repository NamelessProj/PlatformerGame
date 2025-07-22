package objects;

import static utils.Constants.GameConstants.SCALE;
import static utils.Constants.ObjectConstants.*;

public class GameContainer  extends GameObject{
    /**
     * Constructor for the GameContainer class.
     * @param x the x-coordinate of the game container
     * @param y the y-coordinate of the game container
     * @param objectType the type of the object (e.g., {@link utils.Constants.ObjectConstants#BOX} or {@link utils.Constants.ObjectConstants#BARREL})
     */
    public GameContainer(int x, int y, int objectType) {
        super(x, y, objectType);
        createHitbox();
    }

    /**
     * Initializes the hitbox for the game container based on its type.
     */
    private void createHitbox() {
        if (objectType == BOX) {
            initHitbox(25, 18);
            xDrawOffset = (int) (7 * SCALE);
            yDrawOffset = (int) (12 * SCALE);
        } else {
            initHitbox(23, 25);
            xDrawOffset = (int) (8 * SCALE);
            yDrawOffset = (int) (5 * SCALE);
        }

        hitbox.y += yDrawOffset + (int) (2 * SCALE);
        hitbox.x += (float) xDrawOffset / 2;
    }

    /**
     * Updates the game container's animation tick if animations are enabled.
     */
    public void update() {
        if (doAnimation)
            updateAnimationTick();
    }
}