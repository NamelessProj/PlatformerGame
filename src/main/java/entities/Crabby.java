package entities;

import gamestates.Playing;

import static utils.Constants.Dialogue.EXCLAMATION;
import static utils.Constants.Directions.*;
import static utils.Constants.EnemyConstants.*;
import static utils.HelpMethods.IsFloor;

public class Crabby extends Enemy {

    /**
     * Crabby enemy constructor.
     * @param x The x-coordinate of the Crabby's position.
     * @param y The y-coordinate of the Crabby's position.
    */
    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(22, 19);
        initAttackBox(82, 19, 30);
    }

    /**
     * Updates the Crabby's state and behavior based on the level data and player position.
     * @param lvlData The level data containing information about the environment.
     * @param playing The Playing instance that manages the game state and player interactions.
     */
    protected void update(int[][] lvlData, Playing playing) {
        updateBehavior(lvlData, playing);
        updateAnimationTick();
        updateAttackBox();
    }

    /**
     * Updates the Crabby's behavior based on its current state and the player's position.
     * @param lvlData The level data containing information about the environment.
     * @param playing The {@link Playing} instance that manages the game state and player interactions.
     */
    private void updateBehavior(int[][] lvlData, Playing playing) {
        if (firstUpdate)
            firstUpdateCheck(lvlData);

        if (inAir)
            inAirChecks(lvlData, playing);
        else {
            switch (state) {
                case IDLE -> {
                    if (IsFloor(hitbox, lvlData))
                        newState(RUNNING);
                    else
                        inAir = true;
                }
                case RUNNING -> {
                    if (canSeePlayer(lvlData, playing.getPlayer())) {
                        turnTowardsPlayer(playing.getPlayer());
                        if (isPlayerCloseForAttack(playing.getPlayer()))
                            newState(ATTACK);
                    }

                    move(lvlData);

                    if (inAir)
                        playing.addDialogue((int) hitbox.x, (int) hitbox.y, EXCLAMATION);
                }
                case ATTACK -> {
                    if (animationIndex == 0)
                        attackChecked = false;

                    if (animationIndex == 3 && !attackChecked)
                        checkEnemyHit(attackBox, playing.getPlayer());
                }
                case HIT -> {
                    if (animationIndex <= GetSpriteAmount(enemyType, state) - 2)
                        pushBack(pushBackDirection, 2f, lvlData);
                    updatePushBackDrawOffset();
                }
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