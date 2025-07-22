package ui;

import java.awt.Rectangle;

public class MyButton {
    protected Rectangle bounds;
    protected int x, y, width, height;

    /**
     * Constructor for the MyButton class.
     * @param x the x-coordinate of the button
     * @param y the y-coordinate of the button
     * @param width the width of the button
     * @param height the height of the button
     */
    public MyButton(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        createBounds();
    }

    /**
     * Creates the bounds of the button for collision detection.
     */
    private void createBounds() {
        bounds = new Rectangle(x, y, width, height);
    }

    /**
     * Gets the x-coordinate of the button.
     * @return the x-coordinate of the button
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of the button.
     * @param x the new x-coordinate of the button
     */
    protected void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y-coordinate of the button.
     * @return the y-coordinate of the button
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of the button.
     * @param y the new y-coordinate of the button
     */
    protected void setY(int y) {
        this.y = y;
    }

    /**
     * Gets the width of the button.
     * @return the width of the button
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of the button.
     * @param width the new width of the button
     */
    protected void setWidth(int width) {
        this.width = width;
    }

    /**
     * Gets the height of the button.
     * @return the height of the button
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of the button.
     * @param height the new height of the button
     */
    protected void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the bounds of the button.
     * @return the bounds {@link Rectangle} of the button as a Rectangle
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Sets the bounds of the button.
     * @param bounds the new bounds {@link Rectangle} of the button
     */
    protected void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }
}