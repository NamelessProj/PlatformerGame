package gamestates;

import mainWindow.Game;
import ui.MenuButton;

import java.awt.event.MouseEvent;

import audio.AudioPlayer;

public class State {
    protected Game game;

    /**
     * Constructor for the State class.
     * @param game the game instance
     */
    public State(Game game) {
        this.game = game;
    }

    /**
     * Checks if the mouse event is within the bounds of the given MenuButton.
     * @param e the mouse event
     * @param btn the MenuButton to check against
     * @return true if the mouse is within the button bounds, false otherwise
     */
    public boolean isIn(MouseEvent e, MenuButton btn) {
        return btn.getBounds().contains(e.getX(), e.getY());
    }

    /**
     * Returns the current game instance.
     * @return the game instance
     */
    public Game getGame() {
        return game;
    }

    /**
     * Sets the current game state and plays the appropriate audio track.
     * @param state the new game state to set
     */
    public void setGamestate(Gamestate state) {
        switch (state) {
            case MENU -> game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
            case PLAYING -> game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
        }

        Gamestate.state = state;
    }
}