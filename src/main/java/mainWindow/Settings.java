package mainWindow;

import java.io.BufferedReader;

import utils.LoadSave;

import static utils.HelpMethods.IsFloat;

public class Settings {
    public class Const {
        public static final int NUM_SETTINGS = 3;
        public static final String SOUND_MUTED = "sound_muted";
        public static final String MUSIC_MUTED = "music_muted";
        public static final String VOLUME = "volume";
    }

    private boolean musicMuted, soundMuted;
    private float volume = 0.5f;

    public Settings() {
        loadSettings();
    }

    private void loadSettings() {
        BufferedReader reader = LoadSave.GetText(LoadSave.Texts.SETTINGS);

        if (reader != null) {
            try {
                int i = 0;
                String line = reader.readLine();
                while (line != null) {
                    if (line.isBlank() || !line.contains("="))
                        continue;

                    if (i++ >= Const.NUM_SETTINGS)
                        break;

                    String[] parts = line.toLowerCase().split("=", 2);
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
                    }

                    line = reader.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            createSettingsFile();
    }

    private void createSettingsFile() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSettingsFile'");
    }

    public boolean getSoundMuted() {
        return soundMuted;
    }

    public boolean getMusicMuted() {
        return musicMuted;
    }

    public float getVolume() {
        return volume;
    }
}