package objects;

import static utils.Constants.GameConstants.SCALE;
import static utils.Constants.ObjectConstants.*;

public class GameContainer  extends GameObject{
    public GameContainer(int x, int y, int objectType) {
        super(x, y, objectType);
        createHitbox();
    }

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
    }

    public void update() {
        if (doAnimation)
            updateAnimationTick();
    }
}