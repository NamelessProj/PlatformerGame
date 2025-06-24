package entities;

import gamestates.Playing;
import utils.LoadSave;

import static utils.Constants.Directions.*;
import static utils.Constants.GameConstants.*;
import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;

public class Player extends Entity {
    private BufferedImage[][] animations;
    private boolean moving = false, attacking = false;
    private boolean left, right, jump;
    private int[][] lvlData;
    private float xDrawOffset = 21 * SCALE;
    private float yDrawOffset = 4 * SCALE;

    // Jumping / Gravity
    private float jumpSpeed = -2.25f * SCALE;
    private float fallSpeedAfterCollision = 0.5f * SCALE;

    // Status bar UI
    private BufferedImage statusBarImg;

    private int statusBarWidth = (int) (192 * SCALE);
    private int statusBarHeight = (int) (58 * SCALE);
    private int statusBarX = (int) (10 * SCALE);
    private int statusBarY = (int) (10 * SCALE);

    private int healthBarWidth = (int) (150 * SCALE);
    private int healthBarHeight = (int) (4 * SCALE);
    private int healthBarXStart = (int) (34 * SCALE);
    private int healthBarYStart = (int) (14 * SCALE);
    private int healthWidth = healthBarWidth;

    private int powerBarWidth = (int) (104 * SCALE);
    private int powerBarHeight = (int) (2 * SCALE);
    private int powerBarXStart = (int) (44 * SCALE);
    private int powerBarYStart = (int) (34 * SCALE);
    private int powerWidth = powerBarWidth;
    private int powerMaxValue = 200;
    private int powerValue = powerMaxValue;

    private int flipX = 0;
    private int flipW = 1;

    private boolean attackChecked;
    private Playing playing;

    private int tileY = 0;
    private boolean powerAttackActive;
    private int powerAttackTick;
    private int powerGrowSpeed = 15;
    private int powerGrowTick;

    /**
     * Constructor for the Player class.
     *
     * @param x      The x-coordinate of the player.
     * @param y      The y-coordinate of the player.
     * @param width  The width of the player.
     * @param height The height of the player.
     */
    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        this.state = IDLE;
        this.maxHealth = 100;
        this.currentHealth = maxHealth;
        this.walkSpeed = 1.0f * SCALE;
        loadAnimations();
        initHitbox(PLAYER_WIDTH, PLAYER_HEIGHT);
        initAttackBox();
    }

    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (20 * SCALE), (int) (20 * SCALE));
        resetAttackBox();
    }

    /**
     * Updates the player's position and animation state.
     */
    public void update() {
        updateHealthBar();
        updatePowerBar();

        if (currentHealth <= 0) {
            if (state != DEAD) {
                state = DEAD;
                animationTick = 0;
                animationIndex = 0;
                playing.setPlayerDying(true);
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);

                if (!IsEntityOnFloor(hitbox, lvlData)) {
                    inAir = true;
                    airSpeed = 0;
                }
            } else if (animationIndex == GetSpriteAmount(DEAD) - 1 && animationTick >= ANIMATION_SPEED - 1) {
                playing.setGameOver(true);
                playing.getGame().getAudioPlayer().stopSong();
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAME_OVER);
            } else {
                updateAnimationTick();

                if (inAir)
                    if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                        hitbox.y += airSpeed;
                        airSpeed += GRAVITY;
                    } else
                        inAir = false;
            }
            return;
        }

        updateAttackBox();

        if (state == HIT) {
            if (!IsEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
                airSpeed = 0;
            }
        if (animationIndex <= GetSpriteAmount(state) - 3)
                pushBack(pushBackDirection, 1.25f, lvlData);
            updatePushBackDrawOffset();
        } else
            updatePosition();

        if (moving) {
            checkPotionTouched();
            checkSpikesTouched();
            checkInsideWater();
            tileY = (int) (hitbox.y / TILES_SIZE);
            if (powerAttackActive) {
                powerAttackTick++;
                if (powerAttackTick >= 35) {
                    powerAttackTick = 0;
                    powerAttackActive = false;
                }
            }
        }

        if (attacking || powerAttackActive)
            checkAttack();

        updateAnimationTick();
        setAnimation();
    }

    private void checkInsideWater() {
        if (IsEntityInWater(hitbox, playing.getLevelManager().getCurrentLevel().getLevelData()))
            currentHealth = 0;
    }

    private void checkSpikesTouched() {
        playing.checkSpikesTouched(this);
    }

    private void checkPotionTouched() {
        playing.checkPotionTouchedPlayer(hitbox);
    }

    private void checkAttack() {
        if (attackChecked || animationIndex != 1)
            return;
        attackChecked = true;

        if (powerAttackActive)
            attackChecked = false;

        playing.checkEnemyHit(attackBox);
        playing.checkObjectHit(attackBox);
        playing.getGame().getAudioPlayer().playAttackSound();
    }

    private void setAttackBoxOnRightSide() {
		attackBox.x = hitbox.x + hitbox.width - (int) (SCALE * 5);
	}

	private void setAttackBoxOnLeftSide() {
		attackBox.x = hitbox.x - hitbox.width - (int) (SCALE * 10);
	}

    private void updateAttackBox() {
        if (right && left) {
            if (flipW == 1)
                setAttackBoxOnRightSide();
            else
                setAttackBoxOnLeftSide();
        } else if (right || (powerAttackActive && flipW == 1))
            setAttackBoxOnRightSide();
        else if (left || (powerAttackActive && flipW == -1))
            setAttackBoxOnLeftSide();

        attackBox.y = hitbox.y + (10 * SCALE);
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
    }

    private void updatePowerBar() {
        powerWidth = (int) ((powerValue / (float) powerMaxValue) * powerBarWidth);

        powerGrowTick++;
        if (powerGrowTick >= powerGrowSpeed) {
            powerGrowTick = 0;
            changePower(1);
        }
    }

    /**
     * Draws the player on the screen.
     *
     * @param g The Graphics object used for drawing.
     */
    public void render(Graphics g, int xLvlOffset) {
        g.drawImage(animations[state][animationIndex],
                (int) (hitbox.x - xDrawOffset) - xLvlOffset + flipX,
                (int) (hitbox.y - yDrawOffset) + (int) pushDrawOffset,
                width * flipW,
                height,
                null);
        // drawHitbox(g, xLvlOffset);
        // drawAttackBox(g, xLvlOffset);
        drawUI(g);
    }

    private void drawUI(Graphics g) {
        // Background UI
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);

        // Health Bar
        g.setColor(Color.RED);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);

        // Power Bar
        g.setColor(Color.YELLOW);
        g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);
    }

    /**
     * Updates the animation tick and handles the animation index.
     */
    private void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= GetSpriteAmount(state)) {
                animationIndex = 0;
                attacking = false;
                attackChecked = false;
                if (state == HIT) {
                    newState(IDLE);
                    airSpeed = 0;
                    if (!IsFloor(hitbox, 0, lvlData))
                        inAir = true;
                }
            }
        }
    }

    /**
     * Sets the current animation based on the player's action.
     */
    private void setAnimation() {
        int startAnimation = state;

        if (state == HIT)
            return;

        if (moving)
            state = RUNNING;
        else
            state = IDLE;

        if (inAir) {
            if (airSpeed < 0)
                state = JUMP;
            else
                state = FALLING;
        }

        if (powerAttackActive) {
            state = ATTACK;
            animationIndex = 1;
            animationTick = 0;
            return;
        }

        if (attacking) {
            state = ATTACK;
            if (startAnimation != ATTACK) {
                animationIndex = 1;
                animationTick = 0;
                return;
            }
        }

        if (startAnimation != state)
            resetAnimationTick();
    }

    /**
     * Resets the animation tick and index to start the animation from the beginning.
     */
    private void resetAnimationTick() {
        animationTick = 0;
        animationIndex = 0;
    }

    /**
     * Updates the player's position based on the movement keys pressed.
     */
    private void updatePosition() {
        moving = false;

        if (jump)
            jump();

        if (!inAir)
            if (!powerAttackActive)
                if ((!left && !right) || (left && right))
                    return;

        float xSpeed = 0;

        if (left && !right) {
            xSpeed -= walkSpeed;
            flipX = width;
            flipW = -1;
        }

        if (right && !left) {
            xSpeed += walkSpeed;
            flipX = 0;
            flipW = 1;
        }

        if (powerAttackActive) {
            if ((!left && !right) || (left && right)) {
                if (flipW == -1)
                    xSpeed = -walkSpeed;
                else
                    xSpeed = walkSpeed;
            }

            xSpeed *= 3;
        }

        if (!inAir && !IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
        
        if (inAir && !powerAttackActive) {
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
            } else {
                hitbox.y = GetEntityYPositionUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
            }
        }

        updateXPosition(xSpeed);
        moving = true;
    }

    private void jump() {
        if (inAir)
            return;

        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPosition(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            hitbox.x += xSpeed;
        else {
            hitbox.x = GetEntityXPositionNextToWall(hitbox, xSpeed);
            if (powerAttackActive) {
                powerAttackActive = false;
                powerAttackTick = 0;
            }
        }
    }

    public void kill() {
        currentHealth = 0;
    }

    public void changeHealth(int val) {
        if (val < 0) {
            if (state == HIT)
                return;
            else
                newState(HIT);
        }

        currentHealth += val;
        currentHealth = Math.max(Math.min(currentHealth, maxHealth), 0);
    }

    public void changeHealth(int val, Enemy e) {
        if (state == HIT)
            return;

        changeHealth(val);
        pushBackOffsetDirection = UP;
        pushDrawOffset = 0;

        if (e.getHitbox().x < hitbox.x)
            pushBackDirection = RIGHT;
        else
            pushBackDirection = LEFT;
    }

    public void changePower(int val) {
        powerValue += val;
        if (powerValue >= powerMaxValue)
            powerValue = powerMaxValue;
        else if (powerValue <= 0)
            powerValue = 0;
    }

    /**
     * Loads the player animations from the sprite atlas.
     */
    private void loadAnimations() {
        BufferedImage playerSprites = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

        animations = new BufferedImage[NUM_ANIMATIONS][MAX_NUM_SPRITES];
        for (int j = 0; j < NUM_ANIMATIONS; j++)
            for (int i = 0; i < MAX_NUM_SPRITES; i++)
                animations[j][i] = playerSprites.getSubimage(i * IMAGE_WIDTH, j * IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);

        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
    }

    /**
     * Loads the level data for collision detection.
     *
     * @param lvlData The 2D array representing the level data.
     */
    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public void powerAttack() {
        if (powerAttackActive)
            return;

        int powerCost = 60;

        if (powerValue >= powerCost) {
            powerAttackActive = true;
            changePower(-powerCost);
        }
    }

    /**
     * Resets the player's direction booleans to false.
     */
    public void resetDirBooleans() {
        left = false;
        right = false;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public int getTileY() {
        return tileY;
    }

    private void resetAttackBox() {
        if (flipW == 1)
            setAttackBoxOnRightSide();
        else
            setAttackBoxOnLeftSide();
    }

    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        airSpeed = 0f;
        powerAttackActive = false;
        powerAttackTick = 0;
        state = IDLE;
        currentHealth = maxHealth;

        hitbox.x = x;
        hitbox.y = y;
        resetAttackBox();

        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }
}