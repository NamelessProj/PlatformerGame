package mainWindow;

import gamestates.Credits;
import gamestates.GameOptions;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;
import ui.AudioOptions;
import utils.Constants;
import audio.AudioPlayer;

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
        Settings settings = new Settings();

        Constants.UpdateConstantsDependingOnScale(settings.getScale());

        audioPlayer = new AudioPlayer();
        audioOptions = new AudioOptions(this, settings);
        menu = new Menu(this);
        playing = new Playing(this);
        credits = new Credits(this);
        gameOptions = new GameOptions(this);
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
            case QUIT -> {
                audioPlayer.shutdown();
                System.exit(0);
            }
            default -> {
                audioPlayer.shutdown();
                System.exit(1);
            }
        }
    }

    /**
     * Renders the current game state to the graphics context.
     * @param g the Graphics object to draw on
     */
    public void render(Graphics g) {
        switch (Gamestate.state) {
            case MENU -> menu.draw(g);
            case PLAYING -> playing.draw(g);
            case OPTIONS -> gameOptions.draw(g);
            case CREDITS -> credits.draw(g);
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
     * @return the Menu object
     */
    public Menu getMenu() {
        return menu;
    }

    /**
     * Returns the Playing object for the game, which contains the game logic and state.
     * @return the Playing object
     */
    public Playing getPlaying() {
        return playing;
    }

    /**
     * Returns the Credits object for the game, which contains the credits state.
     * @return the Credits object
     */
    public Credits getCredits() {
        return credits;
    }

    /**
     * Returns the GameOptions object for the game, which contains game settings.
     * @return the GameOptions object
     */
    public GameOptions getGameOptions() {
        return gameOptions;
    }

    /**
     * Returns the AudioOptions object for the game, which contains audio settings.
     * @return the AudioOptions object
     */
    public AudioOptions getAudioOptions() {
        return audioOptions;
    }

    /**
     * Returns the AudioPlayer object for the game, which handles audio playback.
     * @return the AudioPlayer object
     */
    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }
}