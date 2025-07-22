package gamestates;

import mainWindow.Game;
import ui.MenuButton;

import java.awt.event.MouseEvent;

import audio.AudioPlayer;

public class State {
    protected Game game;

    /**
     * Constructor for the State class.
     * @param game the {@link Game} instance
     */
    public State(Game game) {
        this.game = game;
    }

    /**
     * Returns the current game instance.
     * @return the {@link Game} instance
     */
    public Game getGame() {
        return game;
    }

    /**
     * Sets the current game state and plays the appropriate audio track.
     * @param state the new {@link Gamestate} to set
     */
    public void setGamestate(Gamestate state) {
        switch (state) {
            case MENU -> game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
            case PLAYING -> game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
        }

        Gamestate.state = state;
    }
}