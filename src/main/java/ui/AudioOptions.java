package ui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import mainWindow.Game;
import mainWindow.Settings;

import static utils.Constants.GameConstants.SCALE;
import static utils.Constants.UI.PauseButtons.SOUND_SIZE;
import static utils.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static utils.Constants.UI.VolumeButtons.VOLUME_HEIGHT;

public class AudioOptions {
    private Game game;
    private Settings settings;
    private VolumeButton volumeBtn;
    private SoundButton musicButton, sfxButton;

    /**
     * Constructor for the AudioOptions class.
     * @param game the game instance to access audio settings
     * @param settings the Settings instance to initialize audio options
     */
    public AudioOptions(Game game, Settings settings) {
        this.game = game;
        this.settings = settings;
        createSoundButtons();
        createVolumeButton();

        setMusicMuted(settings.getMusicMuted());
        setSoundMuted(settings.getSoundMuted());
        setVolume(settings.getVolume());
    }

    /**
     * Creates the volume button for adjusting the audio volume.
     */
    private void createVolumeButton() {
        int vX = (int) (309 * SCALE);
        int vY = (int) (258 * SCALE);
        volumeBtn = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    /**
     * Creates the sound buttons for toggling music and sound effects.
     */
    private void createSoundButtons() {
        int soundX = (int) (430 * SCALE);
        int musicY = (int) (120 * SCALE);
        int sfxY = (int) (166 * SCALE);

        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    /**
     * Updates the state of the audio options buttons.
     */
    public void update() {
        musicButton.update();
        sfxButton.update();
        volumeBtn.update();
    }

    /**
     * Draws the audio options buttons on the screen.
     * @param g the Graphics object used for drawing
     */
    public void draw(Graphics g) {
        musicButton.draw(g);
        sfxButton.draw(g);
        volumeBtn.draw(g);
    }

    /**
     * Checks if the mouse event is within the bounds of a given button.
     * @param e the MouseEvent to check
     * @param btn the PauseButton to check against
     * @return true if the mouse is within the button's bounds, false otherwise
     */
    private boolean isIn(MouseEvent e, PauseButton btn) {
        return btn.getBounds().contains(e.getX(), e.getY());
    }

    /**
     * Handles mouse dragging events for the volume slider.
     * @param e the MouseEvent containing the mouse position
     */
    public void mouseDragged(MouseEvent e) {
        if (volumeBtn.isMousePressed()) {
            float valueBefore = volumeBtn.getFloatValue();
            volumeBtn.changeX(e.getX());
            float valueAfter = volumeBtn.getFloatValue();
            if (valueBefore != valueAfter)
                game.getAudioPlayer().setVolume(valueAfter);
        }
    }

    /**
     * Handles mouse pressed events for the audio options buttons.
     * @param e the MouseEvent containing the mouse position
     */
    public void mousePressed(MouseEvent e) {
        if (isIn(e, musicButton))
            musicButton.setMousePressed(true);
        else if (isIn(e, sfxButton))
            sfxButton.setMousePressed(true);
        else if (isIn(e, volumeBtn))
            volumeBtn.setMousePressed(true);
    }

    /**
     * Handles mouse released events for the audio options buttons.
     * @param e the MouseEvent containing the mouse position
     */
    public void mouseReleased(MouseEvent e) {
        boolean isReleased = false;

        if (isIn(e, musicButton)) {
            if (musicButton.isMousePressed()) {
                isReleased = true;
                musicButton.setMuted(!musicButton.isMuted());
                game.getAudioPlayer().toggleSongMute();
            }
        } else if (isIn(e, sfxButton)) {
            if (sfxButton.isMousePressed()) {
                isReleased = true;
                sfxButton.setMuted(!sfxButton.isMuted());
                game.getAudioPlayer().toggleEffectMute();
            }
        }

        if (isReleased || volumeBtn.isMousePressed())
            settings.saveSettings(volumeBtn.getFloatValue(), sfxButton.isMuted(), musicButton.isMuted());

        musicButton.resetBools();
        sfxButton.resetBools();
        volumeBtn.resetBools();
    }

    /**
     * Handles mouse moved events to update the mouse-over state of the buttons.
     * @param e the MouseEvent containing the mouse position
     */
    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        volumeBtn.setMouseOver(false);

        if (isIn(e, musicButton))
            musicButton.setMouseOver(true);
        else if (isIn(e, sfxButton))
            sfxButton.setMouseOver(true);
        else if (isIn(e, volumeBtn))
            volumeBtn.setMouseOver(true);
    }

    /**
     * Sets the mute state for the music.
     * @param muted true to mute the music, false to unmute it
     */
    public void setMusicMuted(boolean muted) {
        musicButton.setMuted(muted);
        game.getAudioPlayer().setSongMute(muted);
    }

    /**
     * Sets the mute state for sound effects.
     * @param muted true to mute sound effects, false to unmute them
     */
    public void setSoundMuted(boolean muted) {
        sfxButton.setMuted(muted);
        game.getAudioPlayer().setEffectMute(muted);
    }

    /**
     * Sets the volume level for the audio.
     * @param volume the volume level to set (0.0 to 1.0)
     */
    public void setVolume(float volume) {
        volumeBtn.setFloatValue(volume);
        game.getAudioPlayer().setVolume(volume);
    }
}