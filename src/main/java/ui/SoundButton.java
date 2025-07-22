package ui;

import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.UI.PauseButtons.SOUND_SIZE_DEFAULT;

public class SoundButton extends PauseButton {
    private BufferedImage[][] soundImgs;
    private boolean mouseOver, mousePressed;
    private boolean muted;
    private int rowIndex, colIndex;

    /**
     * Constructor for the SoundButton class.
     * @param x the x-coordinate of the button
     * @param y the y-coordinate of the button
     * @param width the width of the button
     * @param height the height of the button
     */
    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);

        loadSoundImages();
    }

    /**
     * Loads the sound button images from the sprite sheet.
     */
    private void loadSoundImages() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.Sprites.SOUND_BUTTONS);
        soundImgs = new BufferedImage[2][3];

        for (int j = 0; j < soundImgs.length; j++)
            for (int i = 0; i < soundImgs[j].length; i++)
                soundImgs[j][i] = temp.getSubimage(i * SOUND_SIZE_DEFAULT, j * SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT);
    }

    /**
     * Resets the mouse interaction states of the button.
     */
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    /**
     * Updates the state of the sound button based on mouse interaction and mute status.
     */
    public void update() {
        if (muted)
            rowIndex = 1;
        else
            rowIndex = 0;

        colIndex = 0;
        if (mouseOver)
            colIndex = 1;
        if (mousePressed)
            colIndex = 2;
    }

    /**
     * Draws the sound button on the screen.
     * @param g the {@link Graphics} object used for drawing
     */
    public void draw(Graphics g) {
        g.drawImage(soundImgs[rowIndex][colIndex], x, y, width, height, null);
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
     * Returns if the sound is muted.
     * @return {@code true} if the sound is muted, {@code false} otherwise
     */
    public boolean isMuted() {
        return muted;
    }

    /**
     * Sets the muted state of the sound button.
     * @param muted {@code true} to mute the sound, {@code false} to unmute
     */
    public void setMuted(boolean muted) {
        this.muted = muted;
    }
}