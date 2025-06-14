package entities;

import gamestates.Playing;
import utils.LoadSave;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.EnemyConstants.*;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] crabbyArr;
    private ArrayList<Crabby> crabbies = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImages();
        addEnemies();
    }

    private void addEnemies() {
        crabbies = LoadSave.GetCrabs();
        System.out.println("Number of crabbies: " + crabbies.size());
    }

    public void update(int[][] lvlData) {
        for (Crabby c : crabbies)
            if (c.isActive())
                c.update(lvlData, playing.getPlayer());
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawCrabs(g, xLvlOffset);
    }

    private void drawCrabs(Graphics g, int xLvlOffset) {
        for (Crabby c : crabbies)
            if (c.isActive()) {
                g.drawImage(crabbyArr[c.getEnemyState()][c.getAnimationIndex()],
                        (int) c.getHitbox().x - CRABBY_DRAWOFFSET_X - xLvlOffset + c.flipX(),
                        (int) c.getHitbox().y - CRABBY_DRAWOFFSET_Y,
                        CRABBY_WIDTH * c.flipW(),
                        CRABBY_HEIGHT,
                        null);
                c.drawHAttackBox(g, xLvlOffset);
            }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (Crabby c : crabbies)
            if (c.isActive() && attackBox.intersects(c.getHitbox())) {
                c.hurt(10);
                return;
            }
    }

    private void loadEnemyImages() {
        crabbyArr = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE);
        for (int j = 0; j < crabbyArr.length; j++)
            for (int i = 0; i < crabbyArr[j].length; i++)
                crabbyArr[j][i] = temp.getSubimage(i * CRABBY_WIDTH_DEFAULT, j * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
    }
}