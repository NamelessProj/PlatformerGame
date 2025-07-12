package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import utils.LoadSave;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constants.GameConstants.*;
import static utils.Constants.UI.URMButtons.*;

public class LevelCompletedOverlay {
    private Playing playing;
    private UrmButton menu, next;
    private BufferedImage img;
    private int bxX, bgY, bgW, bgH;

    /**
     * Constructor for the LevelCompletedOverlay class.
     * @param playing the Playing instance to access game state and methods
     */
    public LevelCompletedOverlay(Playing playing) {
        this.playing = playing;
        initImage();
        initButtons();
    }

    /**
     * Initializes the buttons for the level completed overlay.
     */
    private void initButtons() {
        int menuX = (int) (330 * SCALE);
        int nextX = (int) (445 * SCALE);
        int y = (int) (195 * SCALE);
        next = new UrmButton(nextX, y, URM_SIZE, URM_SIZE, 0);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    /**
     * Initializes the background image for the level completed overlay.
     */
    private void initImage() {
        img = LoadSave.GetSpriteAtlas(LoadSave.Images.COMPLETED_IMG);
        bgW = (int) (img.getWidth() * SCALE);
        bgH = (int) (img.getHeight() * SCALE);
        bxX = GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (75 * SCALE);
    }

    /**
     * Updates the state of the level completed overlay.
     */
    public void update() {
        next.update();
        menu.update();
    }

    /**
     * Draws the level completed overlay on the screen.
     * @param g the Graphics object used for drawing
     */
    public void draw(Graphics g) {
        g.drawImage(img, bxX, bgY, bgW, bgH, null); // Draw background image
        next.draw(g);
        menu.draw(g);
    }

    /**
     * Checks if the mouse event is within the bounds of a button.
     * @param b the UrmButton to check
     * @param e the MouseEvent to check
     * @return true if the mouse is within the button bounds, false otherwise
     */
    private boolean isIn(UrmButton b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    /**
     * Handles mouse movement events to update button states.
     * @param e the MouseEvent containing the mouse position
     */
    public void mouseMoved(MouseEvent e) {
        next.setMouseOver(false);
        menu.setMouseOver(false);

        if (isIn(menu, e))
            menu.setMouseOver(true);
        else if (isIn(next, e))
            next.setMouseOver(true);
    }

    /**
     * Handles mouse released events to perform actions based on button clicks.
     * @param e the MouseEvent containing the mouse position
     */
    public void mouseReleased(MouseEvent e) {
        if (isIn(menu, e)) {
            if (menu.isMousePressed()) {
                playing.resetAll();
                playing.setGamestate(Gamestate.MENU);
                playing.loadNextLevel();
            }
        } else if (isIn(next, e)) {
            if (next.isMousePressed()) {
                playing.loadNextLevel();
                if (playing.getLevelManager().getLevelIndex() > 0)
                    playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLevelIndex());
            }
        }

        next.resetBools();
        menu.resetBools();
    }

    /**
     * Handles mouse pressed events to set button states.
     * @param e the MouseEvent containing the mouse position
     */
    public void mousePressed(MouseEvent e) {
        if (isIn(menu, e))
            menu.setMousePressed(true);
        else if (isIn(next, e))
            next.setMousePressed(true);
    }
}