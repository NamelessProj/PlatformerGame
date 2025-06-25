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

    /**
     * AudioPlayer constructor initializes TinySound and loads songs and sound effects.
     * <p/>
     * It also plays the default menu song.
    */
    public AudioPlayer() {
        TinySound.init();
        loadSongs();
        loadEffects();
        playSong(MENU_1);
    }

    /**
     * Loads the songs from the audio resources.
     * <p/>
     * The songs are stored in an array for easy access.
     */
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

    /**
     * Loads the sound effects from the audio resources.
     * <p/>
     * The sound effects are stored in an array for easy access.
     * <p/>
     * The effects include sounds for actions like jumping, dying, and attacking.
    */
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
    }

    /**
     * Retrieves a Music object from the audio resources.
     * <p/>
     * The method constructs the URL for the audio file and loads it using TinySound.
     *
     * @param name The name of the music file (without extension).
     * @return The loaded Music object, or null if loading fails.
    */
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

    /**
     * Retrieves a Sound object from the audio resources.
     * <p/>
     * The method constructs the URL for the audio file and loads it using TinySound.
     *
     * @param name The name of the sound effect file (without extension).
     * @return The loaded Sound object, or null if loading fails.
    */
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

    /**
     * Sets the volume for all songs and updates the current song's volume.
     * <p/>
     * This method is called to adjust the volume level of the audio playback.
     *
     * @param volume The desired volume level (0.0 to 1.0).
    */
    public void setVolume(float volume) {
        this.volume = volume;
        updateSongVolume();
    }

    /**
     * Stops the currently playing song.
     * <p/>
     * This method checks if the current song is playing and stops it if so.
    */
    public void stopSong() {
        if (songs[currentSongId].playing())
            songs[currentSongId].stop();
    }

    /**
     * Sets the song based on the level index.
     * <p/>
     * If the level index is even, it plays LEVEL_1; otherwise, it plays LEVEL_2.
     *
     * @param levelIndex The index of the level to determine which song to play.
    */
    public void setLevelSong(int levelIndex) {
        if (levelIndex % 2 == 0)
            playSong(LEVEL_1);
        else
            playSong(LEVEL_2);
    }

    /**
     * Plays the level completed sound effect.
     * <p/>
     * This method stops the current song and plays the level completed sound effect.
    */
    public void levelCompleted() {
        stopSong();
        playEffect(LVL_COMPLETED);
    }

    /**
     * Play one of the attack sound effects.
     * <p/>
     * This method randomly selects one of the attack sound effects to play.
    */
    public void playAttackSound() {
        int start = 4;
        start += random.nextInt(3);
        playEffect(start);
    }

    /**
     * Plays a song based on the provided song index.
     * <p/>
     * This method stops the current song, sets the new song, and plays it.
     *
     * @param song The index of the song to play.
    */
    public void playSong(int song) {
        stopSong();

        currentSongId = song;
        updateSongVolume();
        songs[currentSongId].setLoop(true);
        songs[currentSongId].play(true);
    }

    /**
     * Plays a sound effect based on the provided effect index.
     * <p/>
     * This method checks if the effect is muted or null before playing the sound.
     *
     * @param effect The index of the sound effect to play.
    */
    public void playEffect(int effect) {
        if (effectMute || effects[effect] == null)
            return; // Do not play if muted or effect is null
        effects[effect].play(volume);
    }

    /**
     * Toggles the mute state for the current song.
     * <p/>
     * If the song is currently playing, it stops it; otherwise, it plays the song again.
    */
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

    /**
     * Toggles the mute state for sound effects.
     * <p/>
     * If sound effects are muted, it stops all currently playing effects; otherwise, it plays the jump effect to let the player know effects are unmuted.
    */
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

    /**
     * Updates the volume for all songs.
    */
    private void updateSongVolume() {
        for (Music s : songs) {
            if (s != null)
                s.setVolume(volume);
        }
        // If the current song is muted, stop it
        if (songMute && songs[currentSongId].playing()) {
            songs[currentSongId].stop();
        } else if (!songMute && !songs[currentSongId].playing()) {
            songs[currentSongId].setLoop(true);
            songs[currentSongId].play(true);
        }
    }

    /**
     * Shuts down TinySound and stops all currently playing songs and effects.
     * <p/>
     * This method is called to clean up resources when the audio player is no longer needed.
    */
    public void shutdown() {
        TinySound.shutdown();
        for (Music s : songs)
            if (s != null)
                s.stop();

        for (Sound e : effects)
            if (e != null)
                e.stop();
    }
}