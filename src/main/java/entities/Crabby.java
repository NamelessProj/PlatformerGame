package entities;

import static utils.Constants.Directions.LEFT;
import static utils.Constants.EnemyConstants.*;
import static utils.Constants.GameConstants.SCALE;
import static utils.HelpMethods.*;

public class Crabby extends Enemy {
    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(x, y, (int) (22 * SCALE), (int) (19 * SCALE));
    }

    protected void update(int[][] lvlData) {
        updateMove(lvlData);
        updateAnimationTick();
    }

    private void updateMove(int[][] lvlData) {
        if (firstUpdate)
            firstUpdateCheck(lvlData);

        if (inAir)
            updateInAir(lvlData);
        else {
            switch (enemyState) {
                case IDLE -> newState(RUNNING);
                case RUNNING -> move(lvlData);
            }
        }
    }
}