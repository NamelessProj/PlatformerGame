package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import utils.LoadSave;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constants.GameConstants.GAME_WIDTH;
import static utils.Constants.GameConstants.SCALE;
import static utils.Constants.UI.PauseButtons.SOUND_SIZE;
import static utils.Constants.UI.URMButtons.URM_SIZE;
import static utils.Constants.UI.VolumeButtons.*;

public class PauseOverlay {
    private Playing playing;
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgWidth, bgHeight;
    private SoundButton musicButton, sfxButton;
    private UrmButton menuBtn, replayBtn, unpauseBtn;
    private VolumeButton volumeBtn;

    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        createSoundButtons();
        createUrmButtons();
        createVolumeButton();
    }

    private void createVolumeButton() {
        int vX = (int) (309 * SCALE);
        int vY = (int) (278 * SCALE);
        volumeBtn = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
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

    private void createSoundButtons() {
        int soundX = (int) (450 * SCALE);
        int musicY = (int) (140 * SCALE);
        int sfxY = (int) (186 * SCALE);

        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgWidth = (int) (backgroundImg.getWidth() * SCALE);
        bgHeight = (int) (backgroundImg.getHeight() * SCALE);
        bgX = GAME_WIDTH / 2 - bgWidth / 2;
        bgY = (int) (25 * SCALE);
    }

    public void update() {
        musicButton.update();
        sfxButton.update();

        menuBtn.update();
        replayBtn.update();
        unpauseBtn.update();

        volumeBtn.update();
    }

    public void draw(Graphics g) {
        // Background
        g.drawImage(backgroundImg, bgX, bgY, bgWidth, bgHeight, null);

        // Sound buttons
        musicButton.draw(g);
        sfxButton.draw(g);

        // URM (Unpause, Replay, Menu) buttons
        menuBtn.draw(g);
        replayBtn.draw(g);
        unpauseBtn.draw(g);

        // Volume slider
        volumeBtn.draw(g);
    }

    private boolean isIn(MouseEvent e, PauseButton btn) {
        return btn.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseDragged(MouseEvent e) {
        if (volumeBtn.isMousePressed()) {
            volumeBtn.changeX(e.getX());
        }
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, musicButton))
            musicButton.setMousePressed(true);
        else if (isIn(e, sfxButton))
            sfxButton.setMousePressed(true);
        else if (isIn(e, menuBtn))
            menuBtn.setMousePressed(true);
        else if (isIn(e, replayBtn))
            replayBtn.setMousePressed(true);
        else if (isIn(e, unpauseBtn))
            unpauseBtn.setMousePressed(true);
        else if (isIn(e, volumeBtn))
            volumeBtn.setMousePressed(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, musicButton) && musicButton.isMousePressed())
            musicButton.setMuted(!musicButton.isMuted());
        else if (isIn(e, sfxButton) && sfxButton.isMousePressed())
            sfxButton.setMuted(!sfxButton.isMuted());
        else if (isIn(e, menuBtn) && menuBtn.isMousePressed()) {
            Gamestate.state = Gamestate.MENU;
            playing.unPauseGame();
        } else if (isIn(e, replayBtn) && replayBtn.isMousePressed()) {
            playing.resetAll();
            playing.unPauseGame();
        } else if (isIn(e, unpauseBtn) && unpauseBtn.isMousePressed())
            playing.unPauseGame();

        musicButton.resetBools();
        sfxButton.resetBools();
        menuBtn.resetBools();
        replayBtn.resetBools();
        unpauseBtn.resetBools();
        volumeBtn.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        menuBtn.setMouseOver(false);
        replayBtn.setMouseOver(false);
        unpauseBtn.setMouseOver(false);
        volumeBtn.setMouseOver(false);

        if (isIn(e, musicButton))
            musicButton.setMouseOver(true);
        else if (isIn(e, sfxButton))
            sfxButton.setMouseOver(true);
        else if (isIn(e, menuBtn))
            menuBtn.setMouseOver(true);
        else if (isIn(e, replayBtn))
            replayBtn.setMouseOver(true);
        else if (isIn(e, unpauseBtn))
            unpauseBtn.setMouseOver(true);
        else if (isIn(e, volumeBtn))
            volumeBtn.setMouseOver(true);
    }
}