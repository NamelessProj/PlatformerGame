package mainWindow;

import java.io.BufferedReader;

import java.awt.Dimension;
import java.awt.Toolkit;

import utils.LoadSave;

import static utils.HelpMethods.IsFloat;

public class Settings {
    public static class Const {
        public static final String EQUALS = "=";
        public static final int NUM_SETTINGS = 5;
        public static final String SOUND_MUTED = "sound_muted";
        public static final String MUSIC_MUTED = "music_muted";
        public static final String VOLUME = "volume";
        public static final String SCALE = "scale";
        public static final String SAVE_ON_EXIT = "save_on_exit";
    }

    private boolean musicMuted, soundMuted;
    private float volume = 0.5f;
    private float scale = -1f;
    private boolean saveOnExit = true;

    /**
     * Constructor for the Settings class.
     */
    public Settings() {
        loadSettings();
    }

    /**
     * Loads the settings from a file.
     */
    private void loadSettings() {
        BufferedReader reader = LoadSave.GetText(LoadSave.Texts.SETTINGS);

        if (reader != null) {
            try {
                int i = 0;
                String line = reader.readLine();
                while (line != null) {
                    if (line.isBlank() || !line.contains(Const.EQUALS))
                        continue;

                    if (i++ >= Const.NUM_SETTINGS)
                        break;

                    String[] parts = line.toLowerCase().split(Const.EQUALS, 2);
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    switch (key) {
                        case Const.MUSIC_MUTED -> musicMuted = value.equals("true");
                        case Const.SOUND_MUTED -> soundMuted = value.equals("true");
                        case Const.VOLUME -> {
                            if (IsFloat(value)) {
                                float volume = Float.parseFloat(value);
                                if (volume < 0 || volume > 1)
                                    volume = 0.5f;
                                this.volume = volume;
                            }
                        }
                        case Const.SCALE -> {
                            if (IsFloat(value)) {
                                float scale = Float.parseFloat(value);
                                if (scale <= 0)
                                    scale = 1f;
                                this.scale = scale;
                            }
                        }
                        case Const.SAVE_ON_EXIT -> saveOnExit = value.equals("true");
                    }

                    line = reader.readLine();
                }

                reader.close();

                if (scale == -1f)
                    updateScaleBasedOnScreenSize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        saveSettings(volume, soundMuted, musicMuted);
    }

    /**
     * Updates the scale based on the current screen size.
     */
    private void updateScaleBasedOnScreenSize() {
        int gameWidth = 832;
        int gameHeight = 448;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight();

        double scaleX = screenWidth / gameWidth;
        double scaleY = screenHeight / gameHeight;
        double scale = Math.min(scaleX, scaleY);

        this.scale = (float) scale - 1;
    }

    /**
     * Saves the current settings to a file.
     * @param volume the volume level to save.
     * @param soundMuted whether sound is muted.
     * @param musicMuted whether music is muted.
     */
    public void saveSettings(float volume, boolean soundMuted, boolean musicMuted) {
        this.volume = volume;
        this.soundMuted = soundMuted;
        this.musicMuted = musicMuted;

        StringBuilder sb = new StringBuilder();
        sb.append(Const.SOUND_MUTED).append(Const.EQUALS).append(soundMuted).append("\n");
        sb.append(Const.MUSIC_MUTED).append(Const.EQUALS).append(musicMuted).append("\n");
        sb.append(Const.VOLUME).append(Const.EQUALS).append(volume).append("\n");
        sb.append(Const.SCALE).append(Const.EQUALS).append(scale).append("\n");
        sb.append(Const.SAVE_ON_EXIT).append(Const.EQUALS).append(saveOnExit).append("\n");
        LoadSave.SaveText(LoadSave.Texts.SETTINGS, sb.toString());
    }

    /**
     * Returns whether sound is muted.
     * @return {@code true} if sound is muted, {@code false} otherwise.
     */
    public boolean getSoundMuted() {
        return soundMuted;
    }

    /**
     * Returns whether music is muted.
     * @return {@code true} if music is muted, {@code false} otherwise.
     */
    public boolean getMusicMuted() {
        return musicMuted;
    }

    /**
     * Returns the volume level.
     * @return the volume level.
     */
    public float getVolume() {
        return volume;
    }

    /**
     * Returns the scale factor.
     * @return the scale factor.
     */
    public float getScale() {
        return scale;
    }

    /**
     * Returns whether to save the game on exit.
     * @return {@code true} if the game should be saved on exit, {@code false} otherwise.
     */
    public boolean getSaveOnExit() {
        return saveOnExit;
    }
}