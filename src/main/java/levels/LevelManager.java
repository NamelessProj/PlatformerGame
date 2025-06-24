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

    public LevelManager(Game game) {
        this.game = game;
        importOutsideSprites();
        createWater();
        levels = new ArrayList<>();
        builtAllLevels();
    }

    private void createWater() {
        waterSprite = new BufferedImage[5];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.WATER_TOP);
        waterBottomIndex = waterSprite.length - 1;
        for (int i = 0; i < waterBottomIndex; i++)
            waterSprite[i] = temp.getSubimage(i * TILES_DEFAULT_SIZE, 0, TILES_DEFAULT_SIZE, TILES_DEFAULT_SIZE);
        waterSprite[waterBottomIndex] = LoadSave.GetSpriteAtlas(LoadSave.WATER_BOTTOM);
    }

    private void builtAllLevels() {
        BufferedImage[] allLevels = LoadSave.GetAllLevels();
        for (BufferedImage img : allLevels)
            levels.add(new Level(img));
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);

        final int spriteWidth = 12;
        final int spriteHeight = 4;
        levelSprite = new BufferedImage[spriteWidth * spriteHeight];

        for (int j = 0; j < spriteHeight; j++)
            for (int i = 0; i < spriteWidth; i++) {
                int index = j * spriteWidth + i;
                levelSprite[index] = img.getSubimage(i * TILES_DEFAULT_SIZE, j * TILES_DEFAULT_SIZE, TILES_DEFAULT_SIZE, TILES_DEFAULT_SIZE);
            }
    }

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

    public void update() {
        updateWaterAnimation();
    }

    private void updateWaterAnimation() {
        animationTick++;
        if (animationTick >= 40) {
            animationTick = 0;
            animationIndex++;

            if (animationIndex >= waterBottomIndex)
                animationIndex = 0;
        }
    }

    public Level getCurrentLevel() {
        return levels.get(levelIndex);
    }

    public int getAmountOfLevels() {
        return levels.size();
    }

    public void loadNextLevel() {
        if (levelIndex >= getAmountOfLevels()) {
            levelIndex = 0;
            game.getPlaying().setGamestate(Gamestate.MENU);
        }

        Level newLevel = levels.get(levelIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
        game.getPlaying().setMaxLevelOffset(newLevel.getLevelOffsetX());
        game.getPlaying().getObjectManager().loadObjects(newLevel);
    }

    public int getLevelIndex() {
        return levelIndex;
    }

    public void setLevelIndex(int index) {
        this.levelIndex = index;
    }
}