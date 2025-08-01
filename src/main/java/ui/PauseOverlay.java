package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import utils.LoadSave;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constants.GameConstants.GAME_WIDTH;
import static utils.Constants.GameConstants.SCALE;
import static utils.Constants.UI.URMButtons.URM_SIZE;
import static utils.HelpMethods.IsIn;

public class PauseOverlay {
    private Playing playing;
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgWidth, bgHeight;
    private UrmButton menuBtn, replayBtn, unpauseBtn;
    private MenuButton saveBtn;
    private AudioOptions audioOptions;

    /**
     * Constructor for the PauseOverlay class.
     * @param playing the {@link Playing}    instance to access game state and methods
     */
    public PauseOverlay(Playing playing) {
        this.playing = playing;
        this.audioOptions = playing.getGame().getAudioOptions();
        loadBackground();
        createButtons();
    }

    /**
     * Creates the URM (Unpause, Replay, Menu) buttons for the pause overlay.
     */
    private void createButtons() {
        int menuX = (int) (313 * SCALE);
        int replayX = (int) (387 * SCALE);
        int unpauseX = (int) (461 * SCALE);
        int saveX = (int) (415 * SCALE);

        int btnY = (int) (302 * SCALE);
        int saveY = (int) (358 * SCALE);

        menuBtn = new UrmButton(menuX, btnY, URM_SIZE, URM_SIZE, 2);
        replayBtn = new UrmButton(replayX, btnY, URM_SIZE, URM_SIZE, 1);
        unpauseBtn = new UrmButton(unpauseX, btnY, URM_SIZE, URM_SIZE, 0);
        saveBtn = new MenuButton(saveX, saveY, 5, Gamestate.OPTIONS);
    }

    /**
     * Loads the background image for the pause overlay.
     */
    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.Images.PAUSE_BACKGROUND);
        bgWidth = (int) (backgroundImg.getWidth() * SCALE);
        bgHeight = (int) (backgroundImg.getHeight() * SCALE);
        bgX = GAME_WIDTH / 2 - bgWidth / 2;
        bgY = (int) (5 * SCALE);
    }

    /**
     * Updates the state of the pause overlay, including button states and audio options.
     */
    public void update() {
        menuBtn.update();
        replayBtn.update();
        unpauseBtn.update();
        saveBtn.update();

        audioOptions.update();
    }

    /**
     * Draws the pause overlay on the screen.
     * @param g the Graphics object used for drawing
     */
    public void draw(Graphics g) {
        // Background
        g.drawImage(backgroundImg, bgX, bgY, bgWidth, bgHeight, null);

        // URM (Unpause, Replay, Menu) buttons
        menuBtn.draw(g);
        replayBtn.draw(g);
        unpauseBtn.draw(g);
        saveBtn.draw(g);

        audioOptions.draw(g);
    }

    /**
     * Handles mouse dragging events for the audio options.
     * @param e the MouseEvent containing the mouse position
     */
    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    /**
     * Handles mouse pressed events for the pause overlay buttons.
     * @param e the MouseEvent containing the mouse position
     */
    public void mousePressed(MouseEvent e) {
        if (IsIn(e, menuBtn))
            menuBtn.setMousePressed(true);
        else if (IsIn(e, replayBtn))
            replayBtn.setMousePressed(true);
        else if (IsIn(e, unpauseBtn))
            unpauseBtn.setMousePressed(true);
        else if (IsIn(e, saveBtn))
            saveBtn.setMousePressed(true);
        else
            audioOptions.mousePressed(e);
    }

    /**
     * Handles mouse released events for the pause overlay buttons.
     * @param e the MouseEvent containing the mouse position
     */
    public void mouseReleased(MouseEvent e) {
        if (IsIn(e, menuBtn)) {
            if (menuBtn.isMousePressed()) {
                playing.setGamestate(Gamestate.MENU);
                playing.unPauseGame();
            }
        } else if (IsIn(e, replayBtn)) {
            if (replayBtn.isMousePressed()) {
                playing.resetAll();
                playing.unPauseGame();
            }
        } else if (IsIn(e, unpauseBtn)) {
            if (unpauseBtn.isMousePressed())
                playing.unPauseGame();
        } else if (IsIn(e, saveBtn)) {
            if (saveBtn.isMousePressed())
                playing.getGameSaves().saveGame();
        } else
            audioOptions.mouseReleased(e);

        menuBtn.resetBools();
        replayBtn.resetBools();
        unpauseBtn.resetBools();
        saveBtn.resetBools();
    }

    /**
     * Handles mouse movement events to update button states.
     * @param e the MouseEvent containing the mouse position
     */
    public void mouseMoved(MouseEvent e) {
        menuBtn.setMouseOver(false);
        replayBtn.setMouseOver(false);
        unpauseBtn.setMouseOver(false);
        saveBtn.setMouseOver(false);

        if (IsIn(e, menuBtn))
            menuBtn.setMouseOver(true);
        else if (IsIn(e, replayBtn))
            replayBtn.setMouseOver(true);
        else if (IsIn(e, unpauseBtn))
            unpauseBtn.setMouseOver(true);
        else if (IsIn(e, saveBtn))
            saveBtn.setMouseOver(true);
        else
            audioOptions.mouseMoved(e);
    }
}