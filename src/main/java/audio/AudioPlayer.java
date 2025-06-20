package audio;

import java.net.URL;
import java.util.Random;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

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

    private Music[] songs;
    private Sound[] effects;
    private int currentSongId;
    private float volume = 1f;
    private boolean songMute, effectMute;
    private Random random = new Random();

    public AudioPlayer() {
        TinySound.init();
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
        songs = new Music[names.length];
        for (int i = 0; i < songs.length; i++)
            songs[i] = getMusic(names[i]);
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
        effects = new Sound[effectNames.length];
        for (int i = 0; i < effects.length; i++)
            effects[i] = getSound(effectNames[i]);

        updateEffectsVolume();
    }

    private Music getMusic(String name) {
        URL url = getClass().getResource("/audio/" + name + ".wav");
        Music music;

        try {
            music = TinySound.loadMusic(url);
            return music;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Sound getSound(String name) {
        URL url = getClass().getResource("/audio/" + name + ".wav");
        Sound sound;

        try {
            sound = TinySound.loadSound(url);
            return sound;
        } catch (Exception e) {
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
        if (songs[currentSongId].playing())
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
        songs[currentSongId].setLoop(true);
        songs[currentSongId].play(true);
    }

    public void playEffect(int effect) {
        if (effectMute || effects[effect] == null)
            return; // Do not play if muted or effect is null
        effects[effect].play(volume);
    }

    public void toggleSongMute() {
        this.songMute = !this.songMute;
        for (Music s : songs) {
            if (s != null && s.playing()) {
                if (songMute)
                    s.stop();
            }
            if (!songMute && s == songs[currentSongId]) {
                s.setLoop(true);
                s.play(true);
            }
        }
    }

    public void toggleEffectMute() {
        this.effectMute = !this.effectMute;
        for (Sound e : effects) {
            if (e != null) {
                if (effectMute)
                    e.stop();
            }
        }
        if (!effectMute)
            playEffect(JUMP);
    }

    private void updateSongVolume() {
        for (Music s : songs) {
            if (s != null) {
                s.setVolume(volume);
            }
        }
        // If the current song is muted, stop it
        if (songMute && songs[currentSongId].playing()) {
            songs[currentSongId].stop();
        } else if (!songMute && !songs[currentSongId].playing()) {
            songs[currentSongId].setLoop(true);
            songs[currentSongId].play(true);
        }
    }

    private void updateEffectsVolume() {
        for (Sound c : effects) {
            if (c != null) {
                System.out.println("Setting effect volume to: " + volume);
            }
        }
    }

    public void shutdown() {
        TinySound.shutdown();
        for (Music s : songs) {
            if (s != null) {
                s.stop();
            }
        }
        for (Sound e : effects) {
            if (e != null) {
                e.stop();
            }
        }
    }
}