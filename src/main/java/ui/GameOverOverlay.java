package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constants.GameConstants.*;
import static utils.Constants.UI.URMButtons.URM_SIZE;
import static utils.HelpMethods.IsIn;

public class GameOverOverlay {
    private Playing playing;
    private BufferedImage img;
    private int imgX, imgY, imgW, imgH;
    private UrmButton menu, play;

    /**
     * Constructor for the GameOverOverlay class.
     * @param playing the {@link Playing} instance to access game state and methods
     */
    public GameOverOverlay(Playing playing) {
        this.playing = playing;
        createImg();
        createButtons();
    }

    /**
     * Creates the buttons for the game over overlay.
     */
    private void createButtons() {
        int menuX = (int) (335 * SCALE);
        int playX = (int) (440 * SCALE);
        int y = (int) (195 * SCALE);
        play = new UrmButton(playX, y, URM_SIZE, URM_SIZE, 0);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    /**
     * Creates the image for the game over overlay.
     */
    private void createImg() {
        img = LoadSave.GetSpriteAtlas(LoadSave.Images.DEATH_SCREEN);
        imgW = (int) (img.getWidth() * SCALE);
        imgH = (int) (img.getHeight() * SCALE);
        imgX = GAME_WIDTH / 2 - imgW / 2;
        imgY = (int) (100 * SCALE);
    }

    /**
     * Draws the game over overlay on the screen.
     * @param g the {@link Graphics} object used for drawing
     */
    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        g.drawImage(img, imgX, imgY, imgW, imgH, null);

        menu.draw(g);
        play.draw(g);
    }

    /**
     * Updates the state of the game over overlay buttons.
     */
    public void update() {
        menu.update();
        play.update();
    }

    /**
     * Handles mouse movement events to update button states.
     * @param e the {@link MouseEvent} containing the mouse position
     */
    public void mouseMoved(MouseEvent e) {
        play.setMouseOver(false);
        menu.setMouseOver(false);

        if (IsIn(e, menu))
            menu.setMouseOver(true);
        else if (IsIn(e, play))
            play.setMouseOver(true);
    }

    /**
     * Handles mouse release events to perform actions based on button clicks.
     * @param e the {@link MouseEvent} containing the mouse position
     */
    public void mouseReleased(MouseEvent e) {
        if (IsIn(e, menu)) {
            if (menu.isMousePressed()) {
                playing.resetAll();
                playing.setGamestate(Gamestate.MENU);
            }
        } else if (IsIn(e, play)) {
            if (play.isMousePressed()) {
                playing.resetAll();
                playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLevelIndex());
            }
        }

        play.resetBools();
        menu.resetBools();
    }

    /**
     * Handles mouse pressed events to set button states.
     * @param e the {@link MouseEvent} containing the mouse position
     */
    public void mousePressed(MouseEvent e) {
        if (IsIn(e, menu))
            menu.setMousePressed(true);
        else if (IsIn(e, play))
            play.setMousePressed(true);
    }

    /**
     * Handles key typed events (not used in this overlay).
     * @param e the {@link KeyEvent} containing the key information
     */
    public void keyPressed(KeyEvent e) {}
}