package entities;

import static utils.Constants.Directions.LEFT;
import static utils.Constants.EnemyConstants.*;
import static utils.HelpMethods.*;

import gamestates.Playing;

public class Shark extends Enemy {
    public Shark(float x, float y) {
        super(x, y, SHARK_WIDTH, SHARK_HEIGHT, SHARK);
        initHitbox(18, 22);
        initAttackBox(20, 18, 20);
    }

    public void update(int[][] lvlData, Playing playing) {
        updateBehavior(lvlData, playing);
        updateAnimationTick();
        updateAttackBoxFlip();
    }

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
    }
}