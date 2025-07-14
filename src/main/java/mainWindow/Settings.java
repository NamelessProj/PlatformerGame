package mainWindow;

import java.io.BufferedReader;

import utils.LoadSave;

import static utils.HelpMethods.IsFloat;

public class Settings {
    public class Const {
        public static final int NUM_SETTINGS = 4;
        public static final String SOUND_MUTED = "sound_muted";
        public static final String MUSIC_MUTED = "music_muted";
        public static final String VOLUME = "volume";
        public static final String SCALE = "scale";
    }

    private Game game;

    public Settings(Game game) {
        this.game = game;
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

                    if (++i >= Const.NUM_SETTINGS)
                        break;

                    String[] parts = line.toLowerCase().split("=", 2);
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    switch (key) {
                        case Const.MUSIC_MUTED -> {
                            boolean muted = value.equals("true");
                            game.getAudioOptions().setMusicMuted(muted);
                        }
                        case Const.SOUND_MUTED -> {
                            boolean muted = value.equals("true");
                            game.getAudioOptions().setSoundMuted(muted);
                        }
                        case Const.VOLUME -> {
                            if (IsFloat(value)) {
                                float volume = Float.parseFloat(value);
                                if (volume < 0 || volume > 1)
                                    volume = 0.5f;
                                game.getAudioOptions().setVolume(volume);
                            }
                        }
                        case Const.SCALE -> {
                            if (IsFloat(value)) {
                                float scale = Float.parseFloat(value);
                                if (scale <= 0)
                                    scale = 1.0f;
                                // Setting the scale in the game options
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
}