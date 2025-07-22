package ui;

import gamestates.Gamestate;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.UI.Buttons.*;

public class MenuButton extends MyButton {
    private int rowIndex, index;
    private int xOffsetCenter = B_WIDTH / 2;
    private Gamestate state;
    private BufferedImage[] imgs;
    private boolean mouseOver, mousePressed;

    /**
     * Constructor for the MenuButton class.
     * @param x the x-coordinate of the button
     * @param y the y-coordinate of the button
     * @param rowIndex the row index in the sprite sheet for the button images
     * @param state the {@link Gamestate} associated with the button
     */
    public MenuButton(int x, int y, int rowIndex, Gamestate state) {
        super(x, y, B_WIDTH, B_HEIGHT);
        this.rowIndex = rowIndex;
        this.state = state;

        loadImages();
        setBounds(new Rectangle(x - xOffsetCenter, y, B_WIDTH, B_HEIGHT));
    }

    /**
     * Loads the button images from the sprite sheet.
     */
    private void loadImages() {
        imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.Sprites.MENU_BUTTONS);

        for (int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
    }

    /**
     * Draws the button on the screen.
     * @param g the {@link Graphics} object used for drawing
     */
    public void draw(Graphics g) {
        g.drawImage(imgs[index], x - xOffsetCenter, y, B_WIDTH, B_HEIGHT, null);
    }

    /**
     * Updates the button's state based on mouse interactions.
     */
    public void update() {
        index = 0;
        if (mouseOver)
            index = 1;
        if (mousePressed)
            index = 2;
    }

    /**
     * Checks if the mouse is pressing the button.
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
     * Checks if the mouse is currently hovering over the button.
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
     * Returns the bounds of the button for collision detection.
     * @return the bounds ({@link Rectangle}) of the button
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Applies the current gamestate to the static Gamestate state variable.
     */
    public void applyGamestate() {
        Gamestate.state = state;
    }

    /**
     * Resets the mouse interaction states of the button.
     */
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    /**
     * Returns the current gamestate associated with the button.
     * @return the current {@link Gamestate}
     */
    public Gamestate getState() {
        return state;
    }
}