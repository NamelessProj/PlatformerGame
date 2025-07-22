package ui;

import utils.LoadSave;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import static utils.Constants.UI.VolumeButtons.*;

public class VolumeButton extends MyButton {
    private BufferedImage[] imgs;
    private BufferedImage sliderImg;
    private int index = 0;
    private boolean mouseOver, mousePressed;
    private int buttonX, minX, maxX;
    private float floatValue = 0;

    /**
     * Constructor for the VolumeButton class.
     * @param x the x-coordinate of the button
     * @param y the y-coordinate of the button
     * @param width the width of the button
     * @param height the height of the button
     */
    public VolumeButton(int x, int y, int width, int height) {
        super(x + width / 2, y, VOLUME_WIDTH, height);
        bounds.x -= VOLUME_WIDTH / 2;
        buttonX = x + width / 2;
        this.x = x;
        this.width = width;
        minX = x + VOLUME_WIDTH / 2;
        maxX = x + width - VOLUME_WIDTH / 2;
        loadImages();
    }

    /**
     * Loads the button images from the sprite sheet.
     */
    private void loadImages() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.Sprites.VOLUME_BUTTONS);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i * VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);

        sliderImg = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
    }

    /**
     * Updates the state of the volume button based on mouse interaction.
     */
    public void update() {
        index = 0;
        if (mouseOver)
            index = 1;
        if (mousePressed)
            index = 2;
    }

    /**
     * Draws the volume button on the screen.
     * @param g the {@link Graphics} object used for drawing
     */
    public void draw(Graphics g) {
        g.drawImage(sliderImg, x, y, width, height, null);
        g.drawImage(imgs[index], buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height, null);
    }

    /**
     * Changes the x-coordinate of the button based on the provided value.
     * @param x the new x-coordinate for the button
     */
    public void changeX(int x) {
        if (x < minX)
            buttonX = minX;
        else if (x > maxX)
            buttonX = maxX;
        else
            buttonX = x;

        updateFloatValue();
        bounds.x = buttonX - VOLUME_WIDTH / 2;
    }

    /**
     * Updates the float value based on the button's position.
     */
    private void updateFloatValue() {
        float range = maxX - minX;
        float val = buttonX - minX;
        floatValue = val / range;
    }

    /**
     * Resets the mouse interaction states of the button.
     */
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    /**
     * Checks if the mouse is currently over the button.
     * @return {@code true} if the mouse is over the button, {@code false} otherwise
     */
    public boolean isMouseOver() {
        return mouseOver;
    }

    /**
     * Sets the mouse over state of the button.
     * @param mouseOver {@code true} to set the button as hovered, {@code false} otherwise
     */
    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    /**
     * Checks if the mouse is currently pressed on the button.
     * @return {@code true} if the mouse is pressed, {@code false} otherwise
     */
    public boolean isMousePressed() {
        return mousePressed;
    }

    /**
     * Sets the mouse pressed state of the button.
     * @param mousePressed {@code true} to set the button as pressed, {@code false} otherwise
     */
    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    /**
     * Returns the current float value representing the volume level.
     * @return the float value of the volume level
     */
    public float getFloatValue() {
        return floatValue;
    }

    /**
     * Sets the float value for the volume level.
     * @param val the new float value to set
     */
    public void setFloatValue(float val) {
        float range = maxX - minX;
        float newX = minX + (val * range);
        changeX((int) newX);
    }
}