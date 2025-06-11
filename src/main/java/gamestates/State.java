package gamestates;

import mainWindow.Game;
import ui.MenuButton;

import java.awt.event.MouseEvent;

public class State {
    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public boolean isIn(MouseEvent e, MenuButton btn) {
        return btn.getBounds().contains(e.getX(), e.getY());
    }

    public Game getGame() {
        return game;
    }
}