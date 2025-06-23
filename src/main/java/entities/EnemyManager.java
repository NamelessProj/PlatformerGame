package entities;

import gamestates.Playing;
import levels.Level;
import utils.LoadSave;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.EnemyConstants.*;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] crabbyArr;
    private Level currLevel;

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImages();
    }

    public void loadEnemies(Level level) {
        this.currLevel = level;
    }

    public void update(int[][] lvlData) {
        boolean isAnyActive = false;
        for (Crabby c : currLevel.getCrabs())
            if (c.isActive()) {
                c.update(lvlData, playing.getPlayer());
                isAnyActive = true;
            }

        if (!isAnyActive)
            playing.setLevelCompleted(true);
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawCrabs(g, xLvlOffset);
    }

    private void drawCrabs(Graphics g, int xLvlOffset) {
        for (Crabby c : currLevel.getCrabs())
            if (c.isActive()) {
                g.drawImage(crabbyArr[c.getState()][c.getAnimationIndex()],
                        (int) c.getHitbox().x - CRABBY_DRAWOFFSET_X - xLvlOffset + c.flipX(),
                        (int) c.getHitbox().y - CRABBY_DRAWOFFSET_Y,
                        CRABBY_WIDTH * c.flipW(),
                        CRABBY_HEIGHT,
                        null);
                // c.drawHAttackBox(g, xLvlOffset);
            }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (Crabby c : currLevel.getCrabs())
            if (c.isActive())
                if (c.getState() != DEAD && c.getState() != HIT)
                    if (attackBox.intersects(c.getHitbox())) {
                        c.hurt(20);
                        return;
                    }
    }

    private void loadEnemyImages() {
        crabbyArr = getImagesArray(LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE), 9, 5, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
    }

    private BufferedImage[][] getImagesArray(BufferedImage atlas, int xSize, int ySize, int spriteW, int spriteH) {
        BufferedImage[][] tempArr = new BufferedImage[ySize][xSize];
        for (int j = 0; j < tempArr.length; j++)
            for (int i = 0; i < tempArr[j].length; i++)
                tempArr[j][i] = atlas.getSubimage(i * spriteW, j * spriteH, spriteW, spriteH);
        return tempArr;
    }

    public void resetAllEnemies() {
        for (Crabby c : currLevel.getCrabs())
            c.resetEnemy();
    }
}