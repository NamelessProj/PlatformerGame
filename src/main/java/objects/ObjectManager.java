package objects;

import entities.Player;
import gamestates.Playing;
import levels.Level;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.Directions.*;
import static utils.Constants.GameConstants.*;
import static utils.Constants.ObjectConstants.*;
import static utils.Constants.Projectiles.*;
import static utils.HelpMethods.CanCannonSeePlayer;
import static utils.HelpMethods.IsProjectileHittingLevel;

public class ObjectManager {
    private Playing playing;
    private BufferedImage spikeImage, cannonBallImage;
    private BufferedImage[] cannonImages, grassImages;
    private BufferedImage[][] potionImages, containerImages;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Projectile> projectiles = new ArrayList<>();

    private Level currentLevel;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        this.currentLevel = playing.getLevelManager().getCurrentLevel();
        loadImages();
    }

    public void checkSpikesTouchedPlayer(Player player) {
        for (Spike s : currentLevel.getSpikes())
            if (s.getHitbox().intersects(player.getHitbox())) {
                player.kill();
                return;
            }
    }

    public void checkObjectTouchedPlayer(Rectangle2D.Float hitbox) {
        for (Potion p : potions)
            if (p.isActive() && hitbox.intersects(p.getHitbox())) {
                p.setActive(false);
                applyEffectToPlayer(p);
            }
    }

    public void applyEffectToPlayer(Potion potion) {
        if (potion.getObjectType() == RED_POTION)
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        else
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
    }

    public void checkObjectHit(Rectangle2D.Float attackBox) {
        for (GameContainer gc : containers)
            if (gc.isActive() && !gc.doAnimation && gc.getHitbox().intersects(attackBox)) {
                gc.setDoAnimation(true);
                int type = RED_POTION;
                if (gc.getObjectType() == BARREL)
                    type = BLUE_POTION;
                potions.add(new Potion((int) (gc.getHitbox().x + gc.getHitbox().width / 2), (int) (gc.getHitbox().y - gc.getHitbox().height / 4), type));
                return;
            }
    }

    public void loadObjects(Level newLevel) {
        this.currentLevel = newLevel;
        potions = new ArrayList<>(newLevel.getPotions());
        containers = new ArrayList<>(newLevel.getContainers());
        projectiles.clear();
    }

    private void loadImages() {
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
        potionImages = new BufferedImage[2][7];
        int potionWidth = 12;
        int potionHeight = 16;

        for (int j = 0; j < potionImages.length; j++)
            for (int i = 0; i < potionImages[j].length; i++)
                potionImages[j][i] = potionSprite.getSubimage(i * potionWidth, j * potionHeight, potionWidth, potionHeight);

        BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
        containerImages = new BufferedImage[2][8];
        int containerWidth = 40;
        int containerHeight = 30;

        for (int j = 0; j < containerImages.length; j++)
            for (int i = 0; i < containerImages[j].length; i++)
                containerImages[j][i] = containerSprite.getSubimage(i * containerWidth, j * containerHeight, containerWidth, containerHeight);

        spikeImage = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);

        cannonImages = new BufferedImage[GetSpriteAmount(CANNON_RIGHT)];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CANNON_ATLAS);
        for (int i = 0; i < cannonImages.length; i++)
            cannonImages[i] = temp.getSubimage(i * CANNON_WIDTH_DEFAULT, 0, CANNON_WIDTH_DEFAULT, CANNON_HEIGHT_DEFAULT);

        cannonBallImage = LoadSave.GetSpriteAtlas(LoadSave.CANNON_BALL);

        BufferedImage grassTemp = LoadSave.GetSpriteAtlas(LoadSave.GRASS_ATLAS);
        grassImages = new BufferedImage[2];
        for (int i = 0; i < grassImages.length; i++)
            grassImages[i] = grassTemp.getSubimage(i * 32, 0, 32, 32);
    }

    public void update(int[][] lvlData, Player player) {
        for (Potion p : potions)
            if (p.isActive())
                p.update();

        for (GameContainer gc : containers)
            if (gc.isActive())
                gc.update();

        updateCannons(lvlData, player);
        updateProjectiles(lvlData, player);
    }

    private void updateProjectiles(int[][] lvlData, Player player) {
        for (Projectile p : projectiles)
            if (p.isActive()) {
                p.updatePosition();
                if (p.getHitbox().intersects(player.getHitbox())) {
                    player.changeHealth(-25);
                    player.setPushBackDirection(p.getDirection() == 1 ? RIGHT : LEFT);
                    p.setActive(false);
                } else if (IsProjectileHittingLevel(p, lvlData))
                    p.setActive(false);
            }
    }

    private void updateCannons(int[][] lvlData, Player player) {
        for (Cannon c : currentLevel.getCannons()) {
            if (!c.doAnimation && c.getTileY() == player.getTileY())
                if (isPlayerInRange(c, player))
                    if (isPlayerInFrontOfCannon(c, player))
                        if (CanCannonSeePlayer(lvlData, player.getHitbox(), c.getHitbox(), c.getTileY())) {
                            c.setDoAnimation(true);
                        }

            c.update();
            if (c.getAnimationIndex() == 4 && c.getAnimationTick() == 0)
                shootCannon(c);
        }
    }

    private void shootCannon(Cannon c) {
        int dir = c.getObjectType() == CANNON_LEFT ? -1 : 1;
        projectiles.add(new Projectile((int) c.getHitbox().x, (int) c.getHitbox().y, dir));
    }

    private boolean isPlayerInFrontOfCannon(Cannon c, Player p) {
        if (c.getObjectType() == CANNON_LEFT) {
            return c.getHitbox().x > p.getHitbox().x;
        } else return c.getHitbox().x < p.getHitbox().x;
    }

    private boolean isPlayerInRange(Cannon c, Player p) {
        int absValue = (int) (Math.abs(p.getHitbox().x - c.getHitbox().x));
        return absValue <= TILES_SIZE * 5;
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
        drawTraps(g, xLvlOffset);
        drawCannons(g, xLvlOffset);
        drawProjectiles(g, xLvlOffset);
        drawGrass(g, xLvlOffset);
    }

    private void drawGrass(Graphics g, int xLvlOffset) {
        for (Grass grass : currentLevel.getGrass())
            g.drawImage(grassImages[grass.getType()],
                    grass.getX() - xLvlOffset,
                    grass.getY(),
                    (int) (32 * SCALE),
                    (int) (32 * SCALE),
                    null);
    }

    private void drawProjectiles(Graphics g, int xLvlOffset) {
        for (Projectile p : projectiles)
            if (p.isActive())
                g.drawImage(cannonBallImage,
                        (int) (p.getHitbox().x - xLvlOffset),
                        (int) (p.getHitbox().y),
                        CANNON_BALL_WIDTH,
                        CANNON_BALL_HEIGHT,
                        null);
    }

    private void drawCannons(Graphics g, int xLvlOffset) {
        for (Cannon c : currentLevel.getCannons()) {
            int x = (int) (c.getHitbox().x - xLvlOffset);
            int width = CANNON_WIDTH;

            if (c.getObjectType() == CANNON_RIGHT) {
                x += width;
                width *= -1;
            }

            g.drawImage(cannonImages[c.getAnimationIndex()],
                    x,
                    (int) (c.getHitbox().y),
                    width,
                    CANNON_HEIGHT,
                    null);
        }
    }

    private void drawTraps(Graphics g, int xLvlOffset) {
        for (Spike s : currentLevel.getSpikes())
            g.drawImage(spikeImage,
                    (int) (s.getHitbox().x - xLvlOffset),
                    (int) (s.getHitbox().y - s.getYDrawOffset()),
                    SPIKE_WIDTH,
                    SPIKE_HEIGHT,
                    null);
    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        for (GameContainer gc : containers)
            if (gc.isActive()) {
                int rowIndex = BOX_ROW_INDEX;
                if (gc.getObjectType() == BARREL)
                    rowIndex = BARREL_ROW_INDEX;

                g.drawImage(containerImages[rowIndex][gc.getAnimationIndex()],
                        (int) gc.getHitbox().x - gc.getXDrawOffset() - xLvlOffset,
                        (int) gc.getHitbox().y - gc.getYDrawOffset(),
                        CONTAINER_WIDTH,
                        CONTAINER_HEIGHT,
                        null);
            }
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for (Potion p : potions)
            if (p.isActive()) {
                int rowIndex = RED_POTION_ROW_INDEX;
                if (p.getObjectType() == BLUE_POTION)
                    rowIndex = BLUE_POTION_ROW_INDEX;

                g.drawImage(potionImages[rowIndex][p.getAnimationIndex()],
                        (int) p.getHitbox().x - p.getXDrawOffset() - xLvlOffset,
                        (int) p.getHitbox().y - p.getYDrawOffset(),
                        POTION_WIDTH,
                        POTION_HEIGHT,
                        null);
            }
    }

    public void resetAllObjects() {
        loadObjects(playing.getLevelManager().getCurrentLevel());

        for (Potion p : potions)
            p.reset();

        for (GameContainer gc : containers)
            gc.reset();

        for (Cannon c : currentLevel.getCannons())
            c.reset();
    }
}