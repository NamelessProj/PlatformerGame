package entities;

import static utils.Constants.EnemyConstants.*;
import static utils.Constants.GameConstants.SCALE;

public class Crabby extends Enemy {
    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(x, y, (int) (22 * SCALE), (int) (19 * SCALE));
    }
}