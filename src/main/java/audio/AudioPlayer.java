package audio;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {
    public static final int MENU_1 = 0;
    public static final int LEVEL_1 = 1;
    public static final int LEVEL_2 = 2;

    public static final int DIE = 0;
    public static final int JUMP = 1;
    public static final int GAME_OVER = 2;
    public static final int LVL_COMPLETED = 3;
    public static final int ATTACK_1 = 4;
    public static final int ATTACK_2 = 5;
    public static final int ATTACK_3 = 6;

    private Clip[] songs, effects;
    private int currentSongId;
    private float volume = 1f;
    private boolean songMute, effectMute;
    private Random random = new Random();

    public AudioPlayer() {
        loadSongs();
        loadEffects();
        playSong(MENU_1);
    }

    private void loadSongs() {
        String[] names = {
            "menu",
            "level1",
            "level2"
        };
        songs = new Clip[names.length];
        for (int i = 0; i < songs.length; i++)
            songs[i] = getClip(names[i]);
    }

    private void loadEffects() {
        String[] effectNames = {
            "die",
            "jump",
            "gameover",
            "lvlcompleted",
            "attack1",
            "attack2",
            "attack3"
        };
        effects = new Clip[effectNames.length];
        for (int i = 0; i < effects.length; i++)
            effects[i] = getClip(effectNames[i]);

        updateEffectsVolume();
    }

    private Clip getClip(String name) {
        URL url = getClass().getResource("/audio/" + name + ".wav");
        AudioInputStream audio;

        try {
            audio = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setVolume(float volume) {
        this.volume = volume;
        updateSongVolume();
        updateEffectsVolume();
    }

    public void stopSong() {
        if (songs[currentSongId].isActive())
            songs[currentSongId].stop();
    }

    public void setLevelSong(int levelIndex) {
        if (levelIndex % 2 == 0)
            playSong(LEVEL_1);
        else
            playSong(LEVEL_2);
    }

    public void levelCompleted() {
        stopSong();
        playEffect(LVL_COMPLETED);
    }

    public void playAttackSound() {
        int start = 4;
        start += random.nextInt(3);
        playEffect(start);
    }

    public void playSong(int song) {
        stopSong();

        currentSongId = song;
        updateSongVolume();
        songs[currentSongId].setMicrosecondPosition(0);
        songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void playEffect(int effect) {
        effects[effect].setMicrosecondPosition(0);
        effects[effect].start();
    }

    public void toggleSongMute() {
        this.songMute = !this.songMute;
        for (Clip c : songs) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(songMute);
        }
    }

    public void toggleEffectMute() {
        this.effectMute = !this.effectMute;
        for (Clip c : effects) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(effectMute);
        }
        if (!effectMute)
            playEffect(JUMP);
    }

    private void updateSongVolume() {
        FloatControl gaiControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
        float range = gaiControl.getMaximum() - gaiControl.getMinimum();
        float gain = (range * volume) + gaiControl.getMinimum();
        gaiControl.setValue(gain);
    }

    private void updateEffectsVolume() {
        for (Clip c : effects) {
            FloatControl gaiControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gaiControl.getMaximum() - gaiControl.getMinimum();
            float gain = (range * volume) + gaiControl.getMinimum();
            gaiControl.setValue(gain);
        }
    }
}