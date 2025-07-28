package objects;

import static utils.Constants.Tree.NUM_ANIMATIONS;

import java.util.Random;

public class BackgroundTree {
    private int x, y, type, animationIndex, animationTick;

    public BackgroundTree(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
        
        Random r = new Random();
        animationIndex = r.nextInt(NUM_ANIMATIONS);
    }

    public void update() {
        animationTick++;
        if (animationTick >= 35) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= NUM_ANIMATIONS)
                animationIndex = 0;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getType() {
        return type;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setType(int type) {
        this.type = type;
    }
}