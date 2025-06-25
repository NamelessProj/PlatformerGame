package gamestates;

public enum Gamestate {
    PLAYING, MENU, OPTIONS, QUIT;

    /**
     * The current state of the game.
     */
    public static Gamestate state = MENU;
}