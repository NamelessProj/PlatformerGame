package ui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import mainWindow.Game;
import mainWindow.Settings;

import static utils.Constants.GameConstants.SCALE;
import static utils.Constants.UI.PauseButtons.SOUND_SIZE;
import static utils.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static utils.Constants.UI.VolumeButtons.VOLUME_HEIGHT;
import static utils.HelpMethods.IsIn;

public class AudioOptions {
    private Game game;
    private Settings settings;
    private VolumeButton volumeBtn;
    private SoundButton musicButton, sfxButton;

    /**
     * Constructor for the AudioOptions class.
     * @param game the {@link Game} instance to access audio settings
     * @param settings the {@link Settings} instance to initialize audio options
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
     * @param g the {@link Graphics} object used for drawing
     */
    public void draw(Graphics g) {
        musicButton.draw(g);
        sfxButton.draw(g);
        volumeBtn.draw(g);
    }

    /**
     * Handles mouse dragging events for the volume slider.
     * @param e the {@link MouseEvent} containing the mouse position
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
     * @param e the {@link MouseEvent} containing the mouse position
     */
    public void mousePressed(MouseEvent e) {
        if (IsIn(e, musicButton))
            musicButton.setMousePressed(true);
        else if (IsIn(e, sfxButton))
            sfxButton.setMousePressed(true);
        else if (IsIn(e, volumeBtn))
            volumeBtn.setMousePressed(true);
    }

    /**
     * Handles mouse released events for the audio options buttons.
     * @param e the {@link MouseEvent} containing the mouse position
     */
    public void mouseReleased(MouseEvent e) {
        boolean isReleased = false;

        if (IsIn(e, musicButton)) {
            if (musicButton.isMousePressed()) {
                isReleased = true;
                musicButton.setMuted(!musicButton.isMuted());
                game.getAudioPlayer().toggleSongMute();
            }
        } else if (IsIn(e, sfxButton)) {
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
     * @param e the {@link MouseEvent} containing the mouse position
     */
    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        volumeBtn.setMouseOver(false);

        if (IsIn(e, musicButton))
            musicButton.setMouseOver(true);
        else if (IsIn(e, sfxButton))
            sfxButton.setMouseOver(true);
        else if (IsIn(e, volumeBtn))
            volumeBtn.setMouseOver(true);
    }

    /**
     * Sets the mute state for the music.
     * @param muted {@code true} to mute the music, {@code false} to unmute it
     */
    public void setMusicMuted(boolean muted) {
        musicButton.setMuted(muted);
        game.getAudioPlayer().setSongMute(muted);
    }

    /**
     * Sets the mute state for sound effects.
     * @param muted {@code true} to mute sound effects, {@code false} to unmute them
     */
    public void setSoundMuted(boolean muted) {
        sfxButton.setMuted(muted);
        game.getAudioPlayer().setEffectMute(muted);
    }

    /**
     * Sets the volume level for the audio.
     * @param volume the volume level to set ({@code 0.0} to {@code 1.0})
     */
    public void setVolume(float volume) {
        volumeBtn.setFloatValue(volume);
        game.getAudioPlayer().setVolume(volume);
    }
}