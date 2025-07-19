package mainWindow;

import java.io.BufferedReader;

import utils.LoadSave;

import static utils.HelpMethods.IsFloat;

public class Settings {
    public static class Const {
        public static final String EQUALS = "=";
        public static final int NUM_SETTINGS = 4;
        public static final String SOUND_MUTED = "sound_muted";
        public static final String MUSIC_MUTED = "music_muted";
        public static final String VOLUME = "volume";
        public static final String SCALE = "scale";
    }

    private boolean musicMuted, soundMuted;
    private float volume = 0.5f;
    private float scale = -1f;

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
                    }

                    line = reader.readLine();
                }

                if (scale == -1f) {
                    scale = 1f; // Default scale if not set
                }

                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            saveSettings(volume, soundMuted, musicMuted);
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
        LoadSave.SaveText(LoadSave.Texts.SETTINGS, sb.toString());
    }

    /**
     * Returns whether sound is muted.
     * @return true if sound is muted, false otherwise.
     */
    public boolean getSoundMuted() {
        return soundMuted;
    }

    /**
     * Returns whether music is muted.
     * @return true if music is muted, false otherwise.
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

    public float getScale() {
        return scale;
    }
}