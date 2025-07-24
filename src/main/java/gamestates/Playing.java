package gamestates;

import entities.EnemyManager;
import entities.Player;
import gamesaves.GameSaves;
import levels.LevelManager;
import mainWindow.Game;
import objects.ObjectManager;
import ui.GameCompletedOverlay;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import effects.DialogueEffect;

import static utils.Constants.*;
import static utils.Constants.Dialogue.*;
import static utils.Constants.Environment.*;

public class Playing extends State implements Statemethods {
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private GameCompletedOverlay gameCompletedOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private GameSaves gameSaves;

    private boolean paused = false;

    private int xLevelOffset;
    private int leftBorder = (int) (0.2 * GameConstants.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * GameConstants.GAME_WIDTH);
    private int maxLevelOffsetX;

    private BufferedImage backgroundImg, bigCloud, smallCloud;
    private BufferedImage[] questionImgs, exclamationImgs;
    private ArrayList<DialogueEffect> dialogEffects = new ArrayList<>();

    private int[] smallCloudsPos;
    private Random random = new Random();

    private boolean gameOver;
    private boolean levelCompleted;
    private boolean gameCompleted;
    private boolean playerDying;

    /**
     * Constructor for the Playing class.
     * @param game the {@link Game} instance
     */
    public Playing(Game game) {
        super(game);
        initClasses();

        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.Images.PLAYING_BACKGROUND_IMAGE);
        bigCloud = LoadSave.GetSpriteAtlas(LoadSave.Images.BIG_CLOUDS);
        smallCloud = LoadSave.GetSpriteAtlas(LoadSave.Images.SMALL_CLOUDS);
        smallCloudsPos = new int[8];

        for (int i = 0; i < smallCloudsPos.length; i++)
            smallCloudsPos[i] = (int) (90 * GameConstants.SCALE) + random.nextInt((int) (100 * GameConstants.SCALE));

        loadDialogue();
        calculateLevelOffsets();
        // Don't load the start level yet - wait for GameSaves to be set
    }

    /**
     * Loads the dialogue effects used for displaying questions and exclamations.
     */
    private void loadDialogue() {
        loadDialogueImages();

        int maxDialogEffects = 10;

        for (int i = 0; i < maxDialogEffects; i++)
            dialogEffects.add(new DialogueEffect(0, 0, EXCLAMATION));
        for (int i = 0; i < maxDialogEffects; i++)
            dialogEffects.add(new DialogueEffect(0, 0, QUESTION));

        for (DialogueEffect de : dialogEffects)
            de.deactivate();
    }

    /**
     * Loads the dialogue images used for displaying questions and exclamations.
     */
    private void loadDialogueImages() {
        int dialogueWidth = 14;
        int dialogueHeight = 12;

        BufferedImage tempQ = LoadSave.GetSpriteAtlas(LoadSave.Sprites.QUESTION_ATLAS);
        questionImgs = new BufferedImage[5];
        for (int i = 0; i < questionImgs.length; i++)
            questionImgs[i] = tempQ.getSubimage(i * dialogueWidth, 0, dialogueWidth, dialogueHeight);

        BufferedImage tempE = LoadSave.GetSpriteAtlas(LoadSave.Sprites.EXCLAMATION_ATLAS);
        exclamationImgs = new BufferedImage[5];
        for (int i = 0; i < exclamationImgs.length; i++)
            exclamationImgs[i] = tempE.getSubimage(i * dialogueWidth, 0, dialogueWidth, dialogueHeight);
    }

    /**
     * Loads the next level in the game.
     */
    public void loadNextLevel() {
        levelManager.setLevelIndex(levelManager.getLevelIndex() + 1);
        levelManager.loadNextLevel();
        resetAll();
    }

    /**
     * Loads the starting level data, including enemies and objects.
     */
    private void loadStartLevel() {
        if (gameSaves.isSaveAvailable())
            gameSaves.loadGame();
        else {
            enemyManager.loadEnemies(levelManager.getCurrentLevel());
            objectManager.loadObjects(levelManager.getCurrentLevel());
        }
    }

    /**
     * Calculates the maximum level offset based on the current level's offset.
     */
    private void calculateLevelOffsets() {
        maxLevelOffsetX = levelManager.getCurrentLevel().getLevelOffsetX();
    }

    /**
     * Initializes the game classes used in the Playing state.
     */
    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);

        player = new Player(200, 200, (int) (64 * GameConstants.SCALE), (int) (40 * GameConstants.SCALE), this);
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        
        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
        gameCompletedOverlay = new GameCompletedOverlay(this);
    }

    /**
     * Sets the maximum level offset for the game.
     * @param maxLevelOffset the maximum level offset value
     */
    public void setMaxLevelOffset(int maxLevelOffset) {
        this.maxLevelOffsetX = maxLevelOffset;
    }

    /**
     * Unpauses the game, allowing it to continue.
     */
    public void unPauseGame() {
        paused = false;
    }

    /**
     * Checks if the player is close to the borders of the level and adjusts the level offset accordingly.
     */
    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLevelOffset;

        if (diff > rightBorder)
            xLevelOffset += diff - rightBorder;
        else if (diff < leftBorder)
            xLevelOffset += diff - leftBorder;

        if (xLevelOffset > maxLevelOffsetX)
            xLevelOffset = maxLevelOffsetX;
        else if (xLevelOffset < 0)
            xLevelOffset = 0;
    }

    /**
     * Draws the clouds in the background of the game.
     * @param g the {@link Graphics} context to draw on
     */
    private void drawClouds(Graphics g) {
        for (int i = 0; i < 3; i++)
            g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int) (xLevelOffset * 0.3), (int) (204 * GameConstants.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);

        for (int i = 0; i < smallCloudsPos.length; i++)
            g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4 * i - (int) (xLevelOffset * 0.7), smallCloudsPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
    }

    /**
     * Updates the dialogue effects in the game.
     */
    private void updateDialogue() {
        for (DialogueEffect de : dialogEffects)
            if (de.isActive())
                de.update();
    }

    /**
     * Draws the dialogue effects on the screen.
     * @param g the {@link Graphics} context to draw on
     * @param xLevelOffset the x offset for the level, used to adjust the position of the dialogue effects
     */
    private void drawDialogue(Graphics g, int xLevelOffset) {
        for (DialogueEffect de : dialogEffects)
            if (de.isActive()) {
                BufferedImage img = de.getType() == QUESTION ? questionImgs[de.getAnimationIndex()] : exclamationImgs[de.getAnimationIndex()];
                g.drawImage(img, de.getX() - xLevelOffset, de.getY(), DIALOGUE_WIDTH, DIALOGUE_HEIGHT, null);
            }
    }

    public void addDialogue(int x, int y, int type) {
        dialogEffects.add(new DialogueEffect(x, y - (int) (GameConstants.SCALE * 15), type));
        for (DialogueEffect de : dialogEffects)
            if (!de.isActive())
                if (de.getType() == type) {
                    de.reset(x, y - (int) (GameConstants.SCALE * 15));
                    return;
                }
    }

    /**
     * Resets all game states and entities to their initial conditions.
     */
    public void resetAll() {
        gameOver = false;
        paused = false;
        levelCompleted = false;
        playerDying = false;

        player.setSpawnPoint(levelManager.getCurrentLevel().getPlayerSpawn());
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();
        dialogEffects.clear();
    }

    /**
     * Sets the game over state.
     * @param gameOver the new game over state (true if the game is over, false otherwise)
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * Checks if the player has hit an enemy with their attack box.
     * @param attackBox the attack box ({@link Rectangle2D.Float}) of the player
     */
    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    /**
     * Checks if the player has hit an object with their attack box.
     * @param attackBox the attack box ({@link Rectangle2D.Float}) of the player
     */
    public void checkObjectHit(Rectangle2D.Float attackBox) {
        objectManager.checkObjectHit(attackBox);
    }

    /**
     * Checks if the player has touched a potion with their hitbox.
     * @param hitbox the hitbox ({@link Rectangle2D.Float}) of the player
     */
    public void checkPotionTouchedPlayer(Rectangle2D.Float hitbox) {
        objectManager.checkObjectTouchedPlayer(hitbox);
    }

    /**
     * Checks if the player has touched spikes with their hitbox.
     * @param player the {@link Player} instance
     */
    public void checkSpikesTouched(Player player) {
        objectManager.checkSpikesTouchedPlayer(player);
    }

    @Override
    public void update() {
        if (paused)
            pauseOverlay.update();
        else if (levelCompleted)
            levelCompletedOverlay.update();
        else if (gameCompleted)
            gameCompletedOverlay.update();
        else if (gameOver)
            gameOverOverlay.update();
        else if (playerDying)
            player.update();
        else {
            updateDialogue();
            levelManager.update();
            objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData());
            checkCloseToBorder();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, null);
        
        drawClouds(g);

        levelManager.draw(g, xLevelOffset);
        objectManager.draw(g, xLevelOffset);
        enemyManager.draw(g, xLevelOffset);
        player.render(g, xLevelOffset);
        drawDialogue(g, xLevelOffset);

        if (paused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT);
            pauseOverlay.draw(g);
        } else if (gameOver)
            gameOverOverlay.draw(g);
        else if (levelCompleted)
            levelCompletedOverlay.draw(g);
        else if (gameCompleted)
            gameCompletedOverlay.draw(g);
    }

    /**
     * Handles mouse dragging events for the pause overlay or level completed overlay.
     * @param e the {@link MouseEvent} triggered by dragging
     */
    public void mouseDragged(MouseEvent e) {
        if (!gameOver && !gameCompleted && !levelCompleted && paused)
            pauseOverlay.mouseDragged(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver) {
            if (e.getButton() == MouseEvent.BUTTON1)
                player.setAttacking(true);
            else if (e.getButton() == MouseEvent.BUTTON3)
                player.powerAttack();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (gameOver)
            gameOverOverlay.mousePressed(e);
        else if (paused)
            pauseOverlay.mousePressed(e);
        else if (levelCompleted)
            levelCompletedOverlay.mousePressed(e);
        else if (gameCompleted)
            gameCompletedOverlay.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (gameOver)
            gameOverOverlay.mouseReleased(e);
        else if (paused)
            pauseOverlay.mouseReleased(e);
        else if (levelCompleted)
            levelCompletedOverlay.mouseReleased(e);
        else if (gameCompleted)
            gameCompletedOverlay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (gameOver)
            gameOverOverlay.mouseMoved(e);
        else if (paused)
            pauseOverlay.mouseMoved(e);
        else if (levelCompleted)
            levelCompletedOverlay.mouseMoved(e);
        else if (gameCompleted)
            gameCompletedOverlay.mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver && !gameCompleted && !levelCompleted)
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A -> player.setLeft(true);
                case KeyEvent.VK_D -> player.setRight(true);
                case KeyEvent.VK_SPACE -> player.setJump(true);
                case KeyEvent.VK_ESCAPE -> paused = !paused;
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver && !gameCompleted && !levelCompleted)
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A -> player.setLeft(false);
                case KeyEvent.VK_D -> player.setRight(false);
                case KeyEvent.VK_SPACE -> player.setJump(false);
            }
    }

    /**
     * Handles the event when the window loses focus.
     */
    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public void resetGameCompleted() {
        gameCompleted = false;
    }

    /**
     * Gets the player instance.
     * @return the {@link Player} instance
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the enemy manager instance.
     * @return the {@link EnemyManager} instance
     */
    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    /**
     * Sets the level completed state.
     * @param b {@code true} if the level is completed, {@code false} otherwise
     */
    public void setLevelCompleted(boolean b) {
        game.getAudioPlayer().levelCompleted();

        if (levelManager.getLevelIndex() + 1 >= levelManager.getAmountOfLevels()) {
            gameCompleted = true;
            levelManager.setLevelIndex(0);
            levelManager.loadNextLevel();
            resetAll();
            return;
        }

        this.levelCompleted = b;
    }

    /**
     * Get the object manager instance.
     * @return the {@link ObjectManager} instance
     */
    public ObjectManager getObjectManager() {
        return objectManager;
    }

    /**
     * Get the level manager instance.
     * @return the {@link LevelManager} instance
     */
    public LevelManager getLevelManager() {
        return levelManager;
    }

    /**
     * Set the player dying state.
     * @param playerDying {@code true} if the player is dying, {@code false} otherwise
     */
    public void setPlayerDying(boolean playerDying) {
        this.playerDying = playerDying;
    }

    /**
     * Get the game saves instance.
     * @return the {@link GameSaves} instance
     */
    public GameSaves getGameSaves() {
        return gameSaves;
    }

    /**
     * Set the game saves instance and load the start level.
     * @param gameSaves the {@link GameSaves} instance to set
     */
    public void setGameSaves(GameSaves gameSaves) {
        this.gameSaves = gameSaves;
        loadStartLevel(); // Now we can load the start level
    }
}