package entities;

import java.awt.geom.Rectangle2D;

import gamestates.Playing;

import static utils.Constants.Directions.*;
import static utils.Constants.EnemyConstants.*;
import static utils.Constants.GameConstants.*;
import static utils.Constants.Saves.DATA_SEPARATOR;
import static utils.HelpMethods.*;

public abstract class Enemy extends Entity {
    protected int enemyType;
    protected boolean firstUpdate = true;
    protected int walkDir = LEFT;
    protected int tileY;
    protected float attackDistance = TILES_SIZE;
    protected boolean active = true;
    protected boolean attackChecked;
    protected int attackBoxOffsetX;

    /**
     * Enemy constructor.
     * @param x The x-coordinate of the enemy's position.
     * @param y The y-coordinate of the enemy's position.
     * @param width The width of the enemy's hitbox.
     * @param height The height of the enemy's hitbox.
     * @param enemyType The type of the enemy, determining its behavior and attributes.
     */
    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
        walkSpeed = SCALE * 0.35f;
    }

    /**
     * Enemy constructor with additional parameters for direction, health, and active state.
     * @param x the x-coordinate of the enemy's position
     * @param y the y-coordinate of the enemy's position
     * @param width the width of the enemy's hitbox
     * @param height the height of the enemy's hitbox
     * @param enemyType the type of the enemy, determining its behavior and attributes
     * @param direction the initial walking direction of the enemy ({@link utils.Constants.Directions#LEFT} or {@link utils.Constants.Directions#RIGHT})
     * @param health the initial health of the enemy
     * @param active the active state of the enemy
     */
    public Enemy(float x, float y, int width, int height, int enemyType, int direction, int health, boolean active) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = health;
        walkSpeed = SCALE * 0.35f;
        walkDir = direction == LEFT ? LEFT : RIGHT;
        this.active = active;
    }

    /**
     * Update the entity's attack box position based on its hitbox.
     */
    protected void updateAttackBox() {
		attackBox.x = hitbox.x - attackBoxOffsetX;
		attackBox.y = hitbox.y;
	}

    /**
     * Update the entity's attack box position based on its walking direction.
     */
    protected void updateAttackBoxFlip() {
		if (walkDir == RIGHT)
			attackBox.x = hitbox.x + hitbox.width;
		else
			attackBox.x = hitbox.x - attackBoxOffsetX;

		attackBox.y = hitbox.y;
	}

    /**
     * Initializes the attack box for the enemy.
     * @param w the width of the attack box.
     * @param h the height of the attack box.
     * @param attackBoxOffsetX the x-coordinate offset for the attack box.
     */
    protected void initAttackBox(int w, int h, int attackBoxOffsetX) {
        attackBox = new Rectangle2D.Float(x, y, (int) (w * SCALE), (int) (h * SCALE));
        this.attackBoxOffsetX = (int) (attackBoxOffsetX * SCALE);
    }

    /**
     * Checks if the enemy is on the floor during the first update.
     * @param lvlData The level data containing information about the environment.
     */
    protected void firstUpdateCheck(int[][] lvlData) {
        firstUpdate = false;
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    /**
     * Performs checks for the enemy while it is in the air.
     * @param lvlData The level data containing information about the environment.
     * @param playing The {@link Playing} instance that manages the game state and player interactions.
     */
    protected void inAirChecks(int[][] lvlData, Playing playing) {
        if (state != HIT && state != DEAD) {
            updateInAir(lvlData);
            playing.getObjectManager().checkSpikesTouchedEnemy(this);
            if (IsEntityInWater(hitbox, lvlData))
                hurt(maxHealth);
        }
    }

    /**
     * Updates the enemy's position while in the air.
     * @param lvlData The level data containing information about the environment.
     */
    protected void updateInAir(int[][] lvlData) {
        if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
        } else {
            inAir = false;
            hitbox.y = GetEntityYPositionUnderRoofOrAboveFloor(hitbox, airSpeed);
            tileY = (int) (hitbox.y / TILES_SIZE);
        }
    }

    /**
     * Moves the enemy based on its walking direction and state.
     * @param lvlData The level data containing information about the environment.
     */
    protected void move(int[][] lvlData) {
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


        changeWalkDirection();
    }

    /**
     * Turns the enemy towards the player based on their position.
     * @param player The {@link Player} entity that the enemy will turn towards.
     */
    protected void turnTowardsPlayer(Player player) {
        if (player.hitbox.x > hitbox.x)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    /**
     * Checks if the enemy can see the player based on their position and the level data.
     * @param lvlData The level data containing information about the environment.
     * @param player The {@link Player} entity that the enemy will check visibility against.
     * @return {@code true} if the enemy can see the player, {@code false} otherwise.
     */
    protected boolean canSeePlayer(int[][] lvlData, Player player) {
        int playerTileY = (int) (player.getHitbox().y / TILES_SIZE);
        if (playerTileY == tileY)
            if (isPlayerInRange(player))
                if (IsSightClear(lvlData, hitbox, player.hitbox, tileY))
                    return true;

        return false;
    }

    /**
     * Checks if the player is within a certain range of the enemy.
     * @param player The {@link Player} entity to check against the enemy's position.
     * @return {@code true} if the player is within range, {@code false} otherwise.
     */
    protected boolean isPlayerInRange(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance * 5;
    }

    /**
     * Checks if the player is close enough for the enemy to attack.
     * @param player The {@link Player} entity to check against the enemy's position.
     * @return {@code true} if the player is close enough for an attack, {@code false} otherwise.
     */
    protected boolean isPlayerCloseForAttack(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        return switch (enemyType) {
            case CRABBY -> absValue <= attackDistance;
            case SHARK -> absValue <= attackDistance * 2;
            default -> false;
        };
    }

    /**
     * Changes the enemy's state to a new state.
     * @param enemyState The new state to set for the enemy.
     */
    protected void newState(int enemyState) {
        this.state = enemyState;
        animationIndex = 0;
        animationTick = 0;
    }

    /**
     * Reduces the enemy's health by a specified amount of damage.
     * @param dmg The amount of damage to reduce from the enemy's health.
     */
    public void hurt(int dmg) {
        currentHealth -= dmg;
        if (currentHealth <= 0)
            newState(DEAD);
        else {
            newState(HIT);
            if (walkDir == LEFT)
                pushBackDirection = RIGHT;
            else
                pushBackDirection = LEFT;
            pushBackOffsetDirection = UP;
            pushDrawOffset = 0;
        }
    }

    /**
     * Checks if the enemy's attack box intersects with the player's hitbox and applies damage if it does.
     * @param attackBox The attack box ({@link Rectangle2D.Float}) of the enemy used to check for collisions.
     * @param player The {@link Player} entity that the enemy is attacking.
     */
    protected void checkEnemyHit(Rectangle2D.Float attackBox, Player player) {
        if (attackBox.intersects(player.getHitbox()))
            player.changeHealth(-GetEnemyDamage(enemyType), this);
        else if (enemyType == SHARK)
            return;
        attackChecked = true;
    }

    /**
     * Updates the animation tick for the enemy, cycling through animation frames.
     */
    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= GetSpriteAmount(enemyType, state)) {
                switch (enemyType) {
                    case CRABBY, SHARK -> {
                        animationIndex = 0;

                        switch (state) {
                            case ATTACK, HIT -> state = IDLE;
                            case DEAD -> active = false;
                        }
                    }
                    case PINKSTAR -> {
                        if (state == ATTACK)
                            animationIndex = 3;
                        else {
                            animationIndex = 0;
                            if (state == HIT)
                                state = IDLE;
                            else if (state == DEAD)
                                active = false;
                        }
                    }
                }
            }
        }
    }

    /**
     * Changes the walking direction of the enemy.
     */
    protected void changeWalkDirection() {
        if (walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    /**
     * Checks if the enemy is currently active.
     * @return {@code true} if the enemy is active, {@code false} otherwise.
     */
    protected boolean isActive() {
        return active;
    }

    /**
     * Resets the enemy's state and position for reuse.
     */
    protected void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        airSpeed = 0;

        pushDrawOffset = 0;
    }

    /**
     * Flips the enemy's hitbox based on its walking direction.
     * @return The x-coordinate offset for flipping the hitbox.
     */
    public int flipX() {
		if (walkDir == RIGHT)
			return width;
		else
			return 0;
	}

    /**
     * Flips the enemy's walking direction for rendering.
     * @return {@code -1} if walking right, {@code 1} if walking left.
     */
	public int flipW() {
		if (walkDir == RIGHT)
			return -1;
		else
			return 1;
	}

    public int getEnemyType() {
        return enemyType;
    }

    /**
     * Gets the push draw offset for rendering the enemy's pushback effect.
     * @return The push draw offset value.
     */
    public float getPushDrawOffset() {
		return pushDrawOffset;
	}

    @Override
    public String toString() {
        String enemyString = Float.toString(hitbox.x) + DATA_SEPARATOR + Float.toString(hitbox.y) + DATA_SEPARATOR;
        int[] data = new int[]{walkDir, currentHealth, active ? 1 : 0};
        return enemyString + toString(data);
    }
}