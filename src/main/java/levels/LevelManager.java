package levels;

import gamestates.Gamestate;
import mainWindow.Game;
import utils.LoadSave;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.GameConstants.*;

public class LevelManager {
    private Game game;
    private BufferedImage[] levelSprite, waterSprite;
    private ArrayList<Level> levels;
    private int levelIndex = 0, animationTick, animationIndex, waterBottomIndex;

    /**
     * Constructor for the LevelManager class.
     * @param game the Game instance that this LevelManager belongs to
     */
    public LevelManager(Game game) {
        this.game = game;
        importOutsideSprites();
        createWater();
        levels = new ArrayList<>();
        builtAllLevels();
    }

    /**
     * Creates the water sprites used in the game.
     */
    private void createWater() {
        waterSprite = new BufferedImage[5];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.Sprites.WATER_TOP);
        waterBottomIndex = waterSprite.length - 1;
        for (int i = 0; i < waterBottomIndex; i++)
            waterSprite[i] = temp.getSubimage(i * TILES_DEFAULT_SIZE, 0, TILES_DEFAULT_SIZE, TILES_DEFAULT_SIZE);
        waterSprite[waterBottomIndex] = LoadSave.GetSpriteAtlas(LoadSave.Sprites.WATER_BOTTOM);
    }

    /**
     * Builds all levels by loading images from the resources.
     */
    private void builtAllLevels() {
        BufferedImage[] allLevels = LoadSave.GetAllLevels();
        for (BufferedImage img : allLevels)
            levels.add(new Level(img));
    }

    /**
     * Imports the outside sprites used in the game.
     */
    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.Sprites.LEVEL_ATLAS);

        final int spriteWidth = 12;
        final int spriteHeight = 4;
        levelSprite = new BufferedImage[spriteWidth * spriteHeight];

        for (int j = 0; j < spriteHeight; j++)
            for (int i = 0; i < spriteWidth; i++) {
                int index = j * spriteWidth + i;
                levelSprite[index] = img.getSubimage(i * TILES_DEFAULT_SIZE, j * TILES_DEFAULT_SIZE, TILES_DEFAULT_SIZE, TILES_DEFAULT_SIZE);
            }
    }

    /**
     * Draws the current level on the provided Graphics object.
     * @param g the Graphics object to draw on
     * @param xLvlOffset the x-level offset for drawing the level
     */
    public void draw(Graphics g, int xLvlOffset) {
        for (int j = 0; j < TILES_IN_HEIGHT; j++)
            for (int i = 0; i < levels.get(levelIndex).getLevelData()[0].length; i++) {
                int index = levels.get(levelIndex).getSpriteIndex(i, j);
                int x = TILES_SIZE * i - xLvlOffset;
                int y = TILES_SIZE * j;
                if (index == 48)
                    g.drawImage(waterSprite[animationIndex], x, y, TILES_SIZE, TILES_SIZE, null);
                else if (index == 49)
                    g.drawImage(waterSprite[waterBottomIndex], x, y, TILES_SIZE, TILES_SIZE, null);
                else
                    g.drawImage(levelSprite[index], x, y, TILES_SIZE, TILES_SIZE, null);
            }
    }

    /**
     * Updates the level manager, including the water animation.
     */
    public void update() {
        updateWaterAnimation();
    }

    /**
     * Updates the water animation by cycling through the water sprites.
     */
    private void updateWaterAnimation() {
        animationTick++;
        if (animationTick >= 40) {
            animationTick = 0;
            animationIndex++;

            if (animationIndex >= waterBottomIndex)
                animationIndex = 0;
        }
    }

    /**
     * Returns the current level.
     * @return the current Level object
     */
    public Level getCurrentLevel() {
        return levels.get(levelIndex);
    }

    /**
     * Returns the amount of levels available.
     * @return the number of levels
     */
    public int getAmountOfLevels() {
        return levels.size();
    }

    /**
     * Loads the next level in the game.
     */
    public void loadNextLevel() {
        Level newLevel = levels.get(levelIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
        game.getPlaying().setMaxLevelOffset(newLevel.getLevelOffsetX());
        game.getPlaying().getObjectManager().loadObjects(newLevel);
    }

    /**
     * Returns the index of the current level.
     * @return the index of the current level
     */
    public int getLevelIndex() {
        return levelIndex;
    }

    /**
     * Sets the index of the current level.
     * @param index the index to set for the current level
     */
    public void setLevelIndex(int index) {
        this.levelIndex = index;
    }
}