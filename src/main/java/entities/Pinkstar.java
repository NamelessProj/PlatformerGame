package entities;

import static utils.Constants.Directions.*;
import static utils.Constants.EnemyConstants.*;
import static utils.HelpMethods.CanMoveHere;
import static utils.HelpMethods.IsFloor;

import gamestates.Playing;

public class Pinkstar extends Enemy {
    private boolean preRoll = true;
    private int tickSinceLastDmgToPlayer, tickAfterRollInIdle;
    private int rollDurationTick, rollDuration = 300;

    public Pinkstar(float x, float y) {
        super(x, y, PINKSTAR_WIDTH, PINKSTAR_HEIGHT, PINKSTAR);
        initHitbox(17, 21);
    }

    public void update(int[][] lvlData, Playing playing) {
        updateBehavior(lvlData, playing);
        updateAnimationTick();
    }

    public void updateBehavior(int[][] lvlData, Playing playing) {
        if (firstUpdate)
            firstUpdateCheck(lvlData);

        if (inAir)
            inAirChecks(lvlData, playing);
        else {
            switch (state) {
                case IDLE -> {
                    preRoll = true;
                    if (tickAfterRollInIdle >= 120) {
                        if (IsFloor(hitbox, lvlData))
                            newState(RUNNING);
                        else
                            inAir = true;
                        tickAfterRollInIdle = 0;
                        tickSinceLastDmgToPlayer = 60;
                    } else 
                        tickAfterRollInIdle++;
                }
                case RUNNING -> {
                    if (canSeePlayer(lvlData, playing.getPlayer())) {
                        newState(ATTACK);
                        setWalkDirection(playing.getPlayer());
                    }
                    move(lvlData, playing);
                }
                case ATTACK -> {
                    if (preRoll) {
                        if (animationIndex >= 3)
                            preRoll = false;
                    } else {
                        move(lvlData, playing);
                        checkDamageToPlayer(playing.getPlayer());
                        checkRollOver(playing);
                    }
                }
                case HIT -> {
                    if (animationIndex <= GetSpriteAmount(enemyType, state) - 2)
                        pushBack(pushBackDirection, 2f, lvlData);
                    updatePushBackDrawOffset();
                    tickAfterRollInIdle = 120;
                }
            }
        }
    }

    private void checkDamageToPlayer(Player player) {
        if (hitbox.intersects(player.getHitbox()))
            if (tickSinceLastDmgToPlayer >= 60) {
                tickSinceLastDmgToPlayer = 0;
                player.changeHealth(-GetEnemyDamage(enemyType), this);
            } else
                tickSinceLastDmgToPlayer++;
    }

    private void setWalkDirection(Player player) {
        if (player.getHitbox().x > hitbox.x)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    protected void move(int[][] lvlData, Playing playing) {
        float xSpeed = walkSpeed;

        if (walkDir == LEFT)
            xSpeed = -walkSpeed;

        if (state == ATTACK)
            xSpeed *= 2;

        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            if (IsFloor(hitbox, xSpeed, lvlData)) {
                hitbox.x += xSpeed;
                return;
            }

        if (state == ATTACK) {
            rollOver(playing);
            rollDurationTick = 0;
        }

        changeWalkDirection();
    }

    private void checkRollOver(Playing playing) {
        rollDurationTick++;
        if (rollDurationTick >= rollDuration) {
            rollOver(playing);
            rollDurationTick = 0;
        }
    }

    private void rollOver(Playing playing) {
        newState(IDLE);
    }
}