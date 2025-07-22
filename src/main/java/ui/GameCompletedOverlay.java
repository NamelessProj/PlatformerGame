package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import utils.LoadSave;

import static utils.Constants.GameConstants.*;
import static utils.HelpMethods.IsIn;

public class GameCompletedOverlay {
    private Playing playing;
    private BufferedImage img;
    private MenuButton quitBtn, creditBtn;
    private int imgX, imgY, imgW, imgH;

    /**
     * Constructor for the GameCompletedOverlay class.
     * @param playing The {@link Playing} instance that this overlay belongs to.
     */
    public GameCompletedOverlay(Playing playing) {
        this.playing = playing;
        createImage();
        createButtons();
    }

    /**
     * Creates the buttons for the overlay.
     */
    private void createButtons() {
        quitBtn = new MenuButton(GAME_WIDTH / 2, (int) (270 * SCALE), 2, Gamestate.MENU);
        creditBtn = new MenuButton(GAME_WIDTH / 2, (int) (200 * SCALE), 3, Gamestate.CREDITS);
    }

    /**
     * Creates the image for the game completed overlay.§
     */
    private void createImage() {
        img = LoadSave.GetSpriteAtlas(LoadSave.Images.GAME_COMPLETED);
        imgW = (int) (img.getWidth() * SCALE);
        imgH = (int) (img.getHeight() * SCALE);
        imgX = (int) GAME_WIDTH / 2 - imgW / 2;
        imgY = (int) (100 * SCALE);
    }

    /**
     * Draws the game completed overlay on the screen.
     * @param g The {@link Graphics} object used for drawing.
     */
    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        g.drawImage(img, imgX, imgY, imgW, imgH, null);

        quitBtn.draw(g);
        creditBtn.draw(g);
    }

    /**
     * Updates the state of the overlay.
     */
    public void update() {
        quitBtn.update();
        creditBtn.update();
    }

    /**
     * Handles mouse movement events for the overlay.
     * @param e The {@link MouseEvent} that triggered the movement.
     */
    public void mouseMoved(MouseEvent e) {
        quitBtn.setMouseOver(false);
        creditBtn.setMouseOver(false);

        if (IsIn(e, quitBtn))
            quitBtn.setMouseOver(true);
        else if (IsIn(e, creditBtn))
            creditBtn.setMouseOver(true);
    }

    /**
     * Handles mouse release events for the overlay.
     * @param e The {@link MouseEvent} that triggered the release.
     */
    public void mouseReleased(MouseEvent e) {
        if (IsIn(e, quitBtn)) {
            if (quitBtn.isMousePressed()) {
                playing.resetAll();
                playing.resetGameCompleted();
                quitBtn.applyGamestate();
            }
        } else if (IsIn(e, creditBtn)) {
            if (creditBtn.isMousePressed()) {
                playing.resetAll();
                playing.resetGameCompleted();
                creditBtn.applyGamestate();
            }
        }

        quitBtn.resetBools();
        creditBtn.resetBools();
    }

    /**
     * Handles mouse press events for the overlay.
     * @param e The {@link MouseEvent} that triggered the press.
     */
    public void mousePressed(MouseEvent e) {
        if (IsIn(e, quitBtn))
            quitBtn.setMousePressed(true);
        else if (IsIn(e, creditBtn))
            creditBtn.setMousePressed(true);
    }
}