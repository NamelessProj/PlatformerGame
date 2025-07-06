package gamestates;

public enum Gamestate {
    PLAYING, MENU, OPTIONS, CREDITS, QUIT;

    /**
     * The current state of the game.
     */
    public static Gamestate state = MENU;
}