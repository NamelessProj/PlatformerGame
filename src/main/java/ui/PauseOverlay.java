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

public class PauseOverlay {
    private Playing playing;
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgWidth, bgHeight;
    private UrmButton menuBtn, replayBtn, unpauseBtn;
    private AudioOptions audioOptions;

    public PauseOverlay(Playing playing) {
        this.playing = playing;
        this.audioOptions = playing.getGame().getAudioOptions();
        loadBackground();
        createUrmButtons();
    }

    private void createUrmButtons() {
        int menuX = (int) (313 * SCALE);
        int replayX = (int) (387 * SCALE);
        int unpauseX = (int) (462 * SCALE);
        int btnY = (int) (325 * SCALE);

        menuBtn = new UrmButton(menuX, btnY, URM_SIZE, URM_SIZE, 2);
        replayBtn = new UrmButton(replayX, btnY, URM_SIZE, URM_SIZE, 1);
        unpauseBtn = new UrmButton(unpauseX, btnY, URM_SIZE, URM_SIZE, 0);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgWidth = (int) (backgroundImg.getWidth() * SCALE);
        bgHeight = (int) (backgroundImg.getHeight() * SCALE);
        bgX = GAME_WIDTH / 2 - bgWidth / 2;
        bgY = (int) (25 * SCALE);
    }

    public void update() {
        menuBtn.update();
        replayBtn.update();
        unpauseBtn.update();

        audioOptions.update();
    }

    public void draw(Graphics g) {
        // Background
        g.drawImage(backgroundImg, bgX, bgY, bgWidth, bgHeight, null);

        // URM (Unpause, Replay, Menu) buttons
        menuBtn.draw(g);
        replayBtn.draw(g);
        unpauseBtn.draw(g);

        audioOptions.draw(g);
    }

    private boolean isIn(MouseEvent e, PauseButton btn) {
        return btn.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuBtn))
            menuBtn.setMousePressed(true);
        else if (isIn(e, replayBtn))
            replayBtn.setMousePressed(true);
        else if (isIn(e, unpauseBtn))
            unpauseBtn.setMousePressed(true);
        else
            audioOptions.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuBtn) && menuBtn.isMousePressed()) {
            Gamestate.state = Gamestate.MENU;
            playing.unPauseGame();
        } else if (isIn(e, replayBtn) && replayBtn.isMousePressed()) {
            playing.resetAll();
            playing.unPauseGame();
        } else if (isIn(e, unpauseBtn) && unpauseBtn.isMousePressed())
            playing.unPauseGame();
        else
            audioOptions.mouseReleased(e);

        menuBtn.resetBools();
        replayBtn.resetBools();
        unpauseBtn.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        menuBtn.setMouseOver(false);
        replayBtn.setMouseOver(false);
        unpauseBtn.setMouseOver(false);

        if (isIn(e, menuBtn))
            menuBtn.setMouseOver(true);
        else if (isIn(e, replayBtn))
            replayBtn.setMouseOver(true);
        else if (isIn(e, unpauseBtn))
            unpauseBtn.setMouseOver(true);
        else
            audioOptions.mouseMoved(e);
    }
}