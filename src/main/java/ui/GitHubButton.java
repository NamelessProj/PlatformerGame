package ui;

import static utils.Constants.UI.PauseButtons.SOUND_SIZE_DEFAULT;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utils.LoadSave;

public class GitHubButton extends MyButton {
    private BufferedImage[] gitHubBtnImages;
    private boolean mouseOver, mousePressed;
    private int colIndex;

    /**
     * Constructor for the GitHubButton class.
     * @param x the x-coordinate of the button
     * @param y the y-coordinate of the button
     * @param width the width of the button
     * @param height the height of the button
     */
    public GitHubButton(int x, int y, int width, int height) {
        super(x, y, width, height);
        loadGitHubButtonImages();
    }

    /**
     * Loads the images for the GitHub button.
     */
    private void loadGitHubButtonImages() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.Sprites.GITHUB_BUTTONS);
        gitHubBtnImages = new BufferedImage[3];

        for (int i = 0; i < gitHubBtnImages.length; i++)
            gitHubBtnImages[i] = temp.getSubimage(i * SOUND_SIZE_DEFAULT, 0, SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT);
    }

    /**
     * Resets the mouse over and pressed states of the button.
     */
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    /**
     * Updates the button's state based on mouse interaction.
     */
    public void update() {
        colIndex = 0;
        if (mouseOver)
            colIndex = 1;
        if (mousePressed)
            colIndex = 2;
    }

    /**
     * Draws the button on the screen.
     * @param g the {@link Graphics} object used for drawing
     */
    public void draw(Graphics g) {
        g.drawImage(gitHubBtnImages[colIndex], x, y, width, height, null);
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
}
