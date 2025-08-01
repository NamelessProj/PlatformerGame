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
    private BufferedImage[][] crabbyArr, pinkstarArr, sharkArr;
    private Level currentLevel;

    /**
     * EnemyManager constructor.
     * @param playing The {@link Playing} instance that manages the game state and player interactions.
     */
    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImages();
    }

    /**
     * Loads the enemies for the current level.
     * @param level The {@link Level} instance containing the enemies to be loaded.
     */
    public void loadEnemies(Level level) {
        this.currentLevel = level;
    }

    /**
     * Updates the state of all active enemies in the current level.
     * @param lvlData The level data containing information about the environment.
     */
    public void update(int[][] lvlData) {
        boolean isAnyActive = false;
        for (Crabby c : currentLevel.getCrabs())
            if (c.isActive()) {
                c.update(lvlData, playing);
                isAnyActive = true;
            }

        for (Pinkstar p : currentLevel.getPinkstars())
			if (p.isActive()) {
				p.update(lvlData, playing);
				isAnyActive = true;
			}

        for (Shark s : currentLevel.getSharks())
            if (s.isActive()) {
                s.update(lvlData, playing);
                isAnyActive = true;
            }

        if (!isAnyActive)
            playing.setLevelCompleted(true);
    }

    /**
     * Draws all active enemies in the current level.
     * @param g The {@link Graphics} object used for drawing the enemies.
     * @param xLvlOffset The x-coordinate offset for the level, used to adjust the drawing position of enemies.
     */
    public void draw(Graphics g, int xLvlOffset) {
        drawCrabs(g, xLvlOffset);
        drawPinkstars(g, xLvlOffset);
        drawSharks(g, xLvlOffset);
    }

    /**
     * Draws all active sharks in the current level.
     * @param g The {@link Graphics} object used for drawing the sharks.
     * @param xLevelOffset The x-coordinate offset for the level, used to adjust the drawing position of sharks.
     */
    private void drawSharks(Graphics g, int xLevelOffset) {
        for (Shark s : currentLevel.getSharks())
            if (s.isActive()) {
                g.drawImage(sharkArr[s.getState()][s.getAnimationIndex()],
                        (int) s.getHitbox().x - xLevelOffset - SHARK_DRAWOFFSET_X + s.flipX(),
                        (int) s.getHitbox().y - SHARK_DRAWOFFSET_Y + (int) s.getPushDrawOffset(),
                        SHARK_WIDTH * s.flipW(),
                        SHARK_HEIGHT,
                        null);
                // s.drawHitbox(g, xLevelOffset);
            }
    }

    /**
     * Draws all active pink stars in the current level.
     * @param g The {@link Graphics} object used for drawing the pink stars.
     * @param xLvlOffset The x-coordinate offset for the level, used to adjust the drawing position of pink stars.
     */
    private void drawPinkstars(Graphics g, int xLvlOffset) {
        for (Pinkstar p : currentLevel.getPinkstars())
            if (p.isActive()) {
                g.drawImage(pinkstarArr[p.getState()][p.getAnimationIndex()],
                        (int) p.getHitbox().x - xLvlOffset - PINKSTAR_DRAWOFFSET_X + p.flipX(),
                        (int) p.getHitbox().y - PINKSTAR_DRAWOFFSET_Y + (int) p.getPushDrawOffset(),
                        PINKSTAR_WIDTH * p.flipW(),
                        PINKSTAR_HEIGHT,
                        null);
                // p.drawHitbox(g, xLvlOffset);
            }
    }

    /**
     * Draws all active crabs in the current level.
     * @param g The {@link Graphics} object used for drawing the crabs.
     * @param xLvlOffset The x-coordinate offset for the level, used to adjust the drawing position of crabs.
     */
    private void drawCrabs(Graphics g, int xLvlOffset) {
        for (Crabby c : currentLevel.getCrabs())
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

    /**
     * Checks if any enemy is hit by the player's attack box.
     * @param attackBox The attack box ({@link Rectangle2D.Float}) of the player, used to detect collisions with enemies.
     */
    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (Crabby c : currentLevel.getCrabs())
            if (c.isActive())
                if (c.getState() != DEAD && c.getState() != HIT)
                    if (attackBox.intersects(c.getHitbox())) {
                        c.hurt(20);
                        return;
                    }

        for (Pinkstar p : currentLevel.getPinkstars())
            if (p.isActive()) {
                if (p.getState() == ATTACK && p.getAnimationIndex() >= 3)
                    return;
                else
                    if (p.getState() != DEAD && p.getState() != HIT)
                        if (attackBox.intersects(p.getHitbox())) {
                            p.hurt(20);
                            return;
                        }
            }

        for (Shark s : currentLevel.getSharks())
            if (s.isActive()) {
                if (s.getState() != DEAD && s.getState() != HIT)
                    if (attackBox.intersects(s.getHitbox())) {
                        s.hurt(20);
                        return;
                    }
            }
    }

    /**
     * Loads the enemy images from the sprite atlas.
     */
    private void loadEnemyImages() {
        crabbyArr = getImagesArray(LoadSave.GetSpriteAtlas(LoadSave.Sprites.CRABBY_SPRITE), 9, 5, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
        pinkstarArr = getImagesArray(LoadSave.GetSpriteAtlas(LoadSave.Sprites.PINKSTAR_ATLAS), 8, 5, PINKSTAR_WIDTH_DEFAULT, PINKSTAR_HEIGHT_DEFAULT);
        sharkArr = getImagesArray(LoadSave.GetSpriteAtlas(LoadSave.Sprites.SHARK_ATLAS), 8, 5, SHARK_WIDTH_DEFAULT, SHARK_HEIGHT_DEFAULT);
    }

    /**
     * Creates a 2D array of BufferedImages from the sprite atlas.
     * @param atlas The sprite atlas containing the enemy images.
     * @param xSize The number of images in the x-direction.
     * @param ySize The number of images in the y-direction.
     * @param spriteW The width of each sprite image.
     * @param spriteH The height of each sprite image.
     * @return A 2D array of BufferedImages representing the enemy sprites.
     */
    private BufferedImage[][] getImagesArray(BufferedImage atlas, int xSize, int ySize, int spriteW, int spriteH) {
        BufferedImage[][] tempArr = new BufferedImage[ySize][xSize];
        for (int j = 0; j < tempArr.length; j++)
            for (int i = 0; i < tempArr[j].length; i++)
                tempArr[j][i] = atlas.getSubimage(i * spriteW, j * spriteH, spriteW, spriteH);
        return tempArr;
    }

    /**
     * Resets all enemies in the current level to their initial state.
     */
    public void resetAllEnemies() {
        for (Crabby c : currentLevel.getCrabs())
            c.resetEnemy();

        for (Pinkstar p : currentLevel.getPinkstars())
            p.resetEnemy();

        for (Shark s : currentLevel.getSharks())
            s.resetEnemy();
    }

    /**
     * Returns a list of all enemies in the current level.
     * @return A list of all enemies.
     */
    public ArrayList<Enemy> getAllEnemies() {
        return currentLevel.getAllEnemies();
    }

    public void setCrabbies(ArrayList<Crabby> crabbies) {
        currentLevel.setCrabs(crabbies);
    }

    public void setPinkstars(ArrayList<Pinkstar> pinkstars) {
        currentLevel.setPinkstars(pinkstars);
    }

    public void setSharks(ArrayList<Shark> sharks) {
        currentLevel.setSharks(sharks);
    }
}