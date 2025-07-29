package objects;

import static utils.Constants.Tree.NUM_ANIMATIONS;

import java.util.Random;

public class BackgroundTree {
    private int x, y, type, animationIndex, animationTick;

    /**
     * Constructor for the BackgroundTree class.
     * @param x The x-coordinate of the tree.
     * @param y The y-coordinate of the tree.
     * @param type The type of the tree (e.g., {@link utils.Constants.Tree#TREE_ONE}, {@link utils.Constants.Tree#TREE_TWO}, etc.).
     */
    public BackgroundTree(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
        
        // Randomly select an animation index for the tree so that each tree can have a different initial animation.
        Random r = new Random();
        animationIndex = r.nextInt(NUM_ANIMATIONS);
    }

    /**
     * Updates the animation index of the tree.
     */
    public void update() {
        animationTick++;
        if (animationTick >= 35) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= NUM_ANIMATIONS)
                animationIndex = 0;
        }
    }

    /**
     * Gets the x-coordinate of the tree.
     * @return The x-coordinate of the tree.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the tree.
     * @return The y-coordinate of the tree.
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the type of the tree.
     * @return The type of the tree (e.g., {@link utils.Constants.Tree#TREE_ONE}, {@link utils.Constants.Tree#TREE_TWO}, etc.).
     */
    public int getType() {
        return type;
    }

    /**
     * Gets the current animation index of the tree.
     * @return The current animation index.
     */
    public int getAnimationIndex() {
        return animationIndex;
    }

    /**
     * Sets the x-coordinate of the tree.
     * @param x The new x-coordinate.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate of the tree.
     * @param y The new y-coordinate.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Sets the type of the tree.
     * @param type The new type of the tree (e.g., {@link utils.Constants.Tree#TREE_ONE}, {@link utils.Constants.Tree#TREE_TWO}, etc.).
     */
    public void setType(int type) {
        this.type = type;
    }
}