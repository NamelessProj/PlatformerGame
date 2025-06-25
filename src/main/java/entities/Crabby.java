package entities;

import java.awt.geom.Rectangle2D;

import static utils.Constants.Directions.*;
import static utils.Constants.EnemyConstants.*;
import static utils.Constants.GameConstants.SCALE;

public class Crabby extends Enemy {

    /**
     * Crabby enemy constructor.
     * @param x The x-coordinate of the Crabby's position.
     * @param y The y-coordinate of the Crabby's position.
    */
    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(22, 19);
        initAttackBox();
    }

    /**
     * Initializes the hitbox for the Crabby enemy.
     */
    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (82 * SCALE), (int) (19 * SCALE));
        attackBoxOffsetX = (int) (30 * SCALE);
    }

    /**
     * Updates the Crabby's state and behavior based on the level data and player position.
     * @param lvlData The level data containing information about the environment.
     * @param player The player entity that the Crabby interacts with.
     */
    protected void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    /**
     * Updates the Crabby's behavior based on its current state and the player's position.
     * @param lvlData The level data containing information about the environment.
     * @param player The player entity that the Crabby interacts with.
     */
    private void updateBehavior(int[][] lvlData, Player player) {
        if (firstUpdate)
            firstUpdateCheck(lvlData);

        if (inAir)
            updateInAir(lvlData);
        else {
            switch (state) {
                case IDLE -> newState(RUNNING);
                case RUNNING -> {
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                        if (isPlayerCloseForAttack(player))
                            newState(ATTACK);
                    }

                    move(lvlData);
                }
                case ATTACK -> {
                    if (animationIndex == 0)
                        attackChecked = false;

                    if (animationIndex == 3 && !attackChecked)
                        checkEnemyHit(attackBox, player);
                }
                case HIT -> {}
            }
        }
    }

    /**
     * Flips the Crabby's hitbox based on its walking direction.
     * @return The x-coordinate offset for flipping the hitbox.
     */
    public int flipX() {
        if (walkDir == RIGHT)
            return width;
        else
            return 0;
    }

    /**
     * Flips the Crabby's walking direction.
     * @return The direction multiplier for flipping the walking direction.
     */
    public int flipW() {
        if (walkDir == RIGHT)
            return -1;
        else
            return 1;
    }
}