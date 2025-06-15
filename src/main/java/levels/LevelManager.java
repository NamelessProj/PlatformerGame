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
    private BufferedImage[] levelSprite;
    private ArrayList<Level> levels;
    private int levelIndex = 0;

    public LevelManager(Game game) {
        this.game = game;
        importOutsideSprites();
        levels = new ArrayList<>();
        builtAllLevels();
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
                g.drawImage(levelSprite[index], TILES_SIZE * i - xLvlOffset, TILES_SIZE * j, TILES_SIZE, TILES_SIZE, null);
            }
    }

    public void update() {}

    public Level getCurrentLevel() {
        return levels.get(levelIndex);
    }

    public int getAmountOfLevels() {
        return levels.size();
    }

    public void loadNextLevel() {
        levelIndex++;
        if (levelIndex >= getAmountOfLevels()) {
            levelIndex = 0;
            System.out.println("no more levels, game over");
            Gamestate.state = Gamestate.MENU;
        }

        Level newLevel = levels.get(levelIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
        game.getPlaying().setMaxLevelOffset(newLevel.getLevelOffsetX());
    }
}