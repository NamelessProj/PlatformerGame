package objects;

import entities.Player;
import gamestates.Playing;
import levels.Level;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.ObjectConstants.*;

public class ObjectManager {
    private Playing playing;
    private BufferedImage spikeImage;
    private BufferedImage[] cannonImages;
    private BufferedImage[][] potionImages;
    private BufferedImage[][] containerImages;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;
    private ArrayList<Cannon> cannons;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImages();
    }

    public void checkSpikesTouchedPlayer(Player player) {
        for (Spike s : spikes)
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
        potions = new ArrayList<>(newLevel.getPotions());
        containers = new ArrayList<>(newLevel.getContainers());
        spikes = newLevel.getSpikes();
        cannons = newLevel.getCannons();
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
    }

    public void update() {
        for (Potion p : potions)
            if (p.isActive())
                p.update();

        for (GameContainer gc : containers)
            if (gc.isActive())
                gc.update();

        updateCannons();
    }

    private void updateCannons() {
        for (Cannon c : cannons)
            c.update();
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
        drawTraps(g, xLvlOffset);
        drawCannons(g, xLvlOffset);
    }

    private void drawCannons(Graphics g, int xLvlOffset) {
        for (Cannon c : cannons) {
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
        for (Spike s : spikes)
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

        for (Cannon c : cannons)
            c.reset();
    }
}