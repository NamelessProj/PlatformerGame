package ui;

import java.awt.Rectangle;

public class PauseButton {
    protected int x, y, width, height;
    protected Rectangle bounds;

    /**
     * Constructor for the PauseButton class.
     * @param x the x-coordinate of the button
     * @param y the y-coordinate of the button
     * @param width the width of the button
     * @param height the height of the button
     */
    public PauseButton(int x, int y, int width, int height) {
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
     * Get the x-coordinate of the button.
     * @return the x-coordinate of the button
     */
    public int getX() {
        return x;
    }

    /**
     * Set the x-coordinate of the button.
     * @param x the new x-coordinate of the button
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Get the y-coordinate of the button.
     * @return the y-coordinate of the button
     */
    public int getY() {
        return y;
    }

    /**
     * Set the y-coordinate of the button.
     * @param y the new y-coordinate of the button
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Get the width of the button.
     * @return the width of the button
     */
    public int getWidth() {
        return width;
    }

    /**
     * Set the width of the button.
     * @param width the new width of the button
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Get the height of the button.
     * @return the height of the button
     */
    public int getHeight() {
        return height;
    }

    /**
     * Set the height of the button.
     * @param height the new height of the button
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Get the bounds of the button.
     * @return the bounds of the button
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Set the bounds of the button.
     * @param bounds the new bounds of the button
     */
    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }
}