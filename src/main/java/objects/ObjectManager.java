package objects;

import gamestates.Playing;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.ObjectConstants.*;

public class ObjectManager {
    private Playing playing;
    private BufferedImage[][] potionImages;
    private BufferedImage[][] containerImages;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImages();
        containers = new ArrayList<>();
        potions = new ArrayList<>();

        potions.add(new Potion(300, 300, RED_POTION));
        potions.add(new Potion(400, 300, BLUE_POTION));

        containers.add(new GameContainer(500, 300, BARREL));
        containers.add(new GameContainer(600, 300, BOX));
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
    }

    public void update() {
        for (Potion p : potions)
            if (p.isActive())
                p.update();

        for (GameContainer gc : containers)
            if (gc.isActive())
                gc.update();
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
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
}