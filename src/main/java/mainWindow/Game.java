package mainWindow;

import gamestates.Credits;
import gamestates.GameOptions;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;
import ui.AudioOptions;
import utils.Constants;
import audio.AudioPlayer;
import gamesaves.GameSaves;

import java.awt.Graphics;

import static utils.Constants.GameConstants.*;

public class Game implements Runnable {
    private GamePanel gamePanel;
    private Thread gameThread;

    private Playing playing;
    private Menu menu;
    private Credits credits;
    private GameOptions gameOptions;
    private AudioOptions audioOptions;
    private AudioPlayer audioPlayer;
    private Settings settings;

    private final boolean SHOW_FPS_UPS = true;

    /**
     * Constructor for the Game class.
     */
    public Game() {
        initClasses();

        gamePanel = new GamePanel(this);
        new GameWindow(gamePanel);
        gamePanel.requestFocusInWindow();
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();

        startGameLoop();
    }

    /**
     * Initializes the game classes used in the game.
     */
    private void initClasses() {
        settings = new Settings();

        Constants.UpdateConstantsDependingOnScale(settings.getScale());

        audioPlayer = new AudioPlayer();
        audioOptions = new AudioOptions(this, settings);
        menu = new Menu(this);
        credits = new Credits(this);
        gameOptions = new GameOptions(this);
        
        // Create Playing instance first (without GameSaves)
        playing = new Playing(this);
        
        // Create GameSaves instance after Playing is initialized
        GameSaves gameSaves = new GameSaves(this);
        
        // Set the GameSaves instance in Playing
        playing.setGameSaves(gameSaves);
    }

    /**
     * Starts the game loop in a new thread.
     */
    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Updates the game state based on the current gamestate.
     */
    public void update() {
        switch (Gamestate.state) {
            case MENU -> menu.update();
            case PLAYING -> playing.update();
            case OPTIONS -> gameOptions.update();
            case CREDITS -> credits.update();
            case QUIT -> quitGame(0);
            default -> quitGame(1);
        }
    }

    /**
     * Quits the game and exits the application.
     * @param exitCode the exit code to return to the operating system
     */
    private void quitGame(int exitCode) {
        audioPlayer.shutdown();
        if (settings.getSaveOnExit())
            playing.getGameSaves().saveGame();
        System.exit(exitCode);
    }

    /**
     * Renders the current game state to the graphics context.
     * @param g the {@link Graphics} object to draw on
     */
    public void render(Graphics g) {
        switch (Gamestate.state) {
            case MENU -> menu.draw(g);
            case PLAYING -> playing.draw(g);
            case OPTIONS -> gameOptions.draw(g);
            case CREDITS -> credits.draw(g);
            case QUIT -> {
                // No need to render anything when quitting
            }
        }
    }

    @Override
    public void run() {
        final double nanoSeconds = 1_000_000_000.0;
        final double timePerFrame = nanoSeconds / FPS_SET;
        final double timePerUpdate = nanoSeconds / UPS_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            if (SHOW_FPS_UPS)
                if (System.currentTimeMillis() - lastCheck >= 1000) {
                    lastCheck = System.currentTimeMillis();
                    System.out.println("FPS: " + frames + " | UPS: " + updates);
                    frames = 0;
                    updates = 0;
                }
        }
    }

    /**
     * Called when the window loses focus.
     */
    public void windowFocusLost() {
        if (Gamestate.state == Gamestate.PLAYING)
            playing.getPlayer().resetDirBooleans();
    }

    /**
     * Returns the Menu object for the game.
     * @return the {@link Menu} object
     */
    public Menu getMenu() {
        return menu;
    }

    /**
     * Returns the Playing object for the game, which contains the game logic and state.
     * @return the {@link Playing} object
     */
    public Playing getPlaying() {
        return playing;
    }

    /**
     * Returns the Credits object for the game, which contains the credits state.
     * @return the {@link Credits} object
     */
    public Credits getCredits() {
        return credits;
    }

    /**
     * Returns the GameOptions object for the game, which contains game settings.
     * @return the {@link GameOptions} object
     */
    public GameOptions getGameOptions() {
        return gameOptions;
    }

    /**
     * Returns the AudioOptions object for the game, which contains audio settings.
     * @return the {@link AudioOptions} object
     */
    public AudioOptions getAudioOptions() {
        return audioOptions;
    }

    /**
     * Returns the AudioPlayer object for the game, which handles audio playback.
     * @return the {@link AudioPlayer} object
     */
    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }
}
