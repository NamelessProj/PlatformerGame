package objects;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utils.Constants.GameConstants.ANIMATION_SPEED;
import static utils.Constants.GameConstants.SCALE;
import static utils.Constants.ObjectConstants.*;

public class GameObject {
    protected int x, y, objectType;
    protected Rectangle2D.Float hitbox;
    protected boolean doAnimation, active = true;
    protected int animationTick, animationIndex;
    protected int xDrawOffset, yDrawOffset;

    public GameObject(int x, int y, int objectType) {
        this.x = x;
        this.y = y;
        this.objectType = objectType;
    }

    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= GetSpriteAmount(objectType)) {
                animationIndex = 0;
                if (objectType == BARREL || objectType == BOX) {
                    doAnimation = false;
                    active = false;
                } else if (objectType == CANNON_LEFT || objectType == CANNON_RIGHT)
                    doAnimation = false;
            }
        }
    }

    public void reset() {
        active = true;
        animationTick = 0;
        animationIndex = 0;

        doAnimation = objectType != BARREL && objectType != BOX && objectType != CANNON_LEFT && objectType != CANNON_RIGHT;
    }

    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int) (width * SCALE), (int) (height * SCALE));
    }

    public void drawHitbox(Graphics g, int xLvlOffset) {
        g.setColor(Color.PINK);
        g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    public int getYDrawOffset() {
        return yDrawOffset;
    }

    public int getXDrawOffset() {
        return xDrawOffset;
    }

    public int getObjectType() {
        return objectType;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public void setDoAnimation(boolean doAnimation) {
        this.doAnimation = doAnimation;
    }
}