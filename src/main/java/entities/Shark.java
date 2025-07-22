package entities;

import static utils.Constants.Dialogue.EXCLAMATION;
import static utils.Constants.Directions.LEFT;
import static utils.Constants.EnemyConstants.*;
import static utils.HelpMethods.*;

import gamestates.Playing;

public class Shark extends Enemy {

    /**
     * Constructor for the Shark enemy.
     * @param x The x-coordinate of the Shark's position.
     * @param y The y-coordinate of the Shark's position.
     */
    public Shark(float x, float y) {
        super(x, y, SHARK_WIDTH, SHARK_HEIGHT, SHARK);
        initHitbox(18, 22);
        initAttackBox(20, 18, 20);
    }

    /**
     * Updates the Shark's state and behavior.
     * @param lvlData The level data containing information about the environment.
     * @param playing The {@link Playing} instance that manages the game state and player interactions.
     */
    public void update(int[][] lvlData, Playing playing) {
        updateBehavior(lvlData, playing);
        updateAnimationTick();
        updateAttackBoxFlip();
    }

    /**
     * Updates the Shark's behavior based on its current state and the level data.
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
                }
                case ATTACK -> {
                    if (animationIndex == 0)
                        attackChecked = false;
                    else if (animationIndex == 3) {
                        if (!attackChecked)
                            checkEnemyHit(attackBox, playing.getPlayer());
                        attackMove(lvlData, playing);
                    }
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
     * Moves the Shark during its attack phase.
     * @param lvlData The level data containing information about the environment.
     * @param playing The {@link Playing} instance that manages the game state and player interactions.
     */
    protected void attackMove(int[][] lvlData, Playing playing) {
        float xSpeed = walkSpeed;
        int multiplier = 4;

        if (walkDir == LEFT)
            xSpeed = -walkSpeed;

        if (CanMoveHere(hitbox.x + xSpeed * multiplier, hitbox.y, hitbox.width, hitbox.height, lvlData))
            if (IsFloor(hitbox, xSpeed * multiplier, lvlData)) {
                hitbox.x += xSpeed * multiplier;
                return;
            }
        newState(IDLE);
        playing.addDialogue((int) hitbox.x, (int) hitbox.y, EXCLAMATION);
    }
}