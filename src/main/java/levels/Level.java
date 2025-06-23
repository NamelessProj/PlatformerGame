package levels;

import entities.Crabby;
import objects.Cannon;
import objects.GameContainer;
import objects.Grass;
import objects.Potion;
import objects.Spike;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.EnemyConstants.*;
import static utils.Constants.ObjectConstants.*;
import static utils.Constants.GameConstants.*;

public class Level {
    private int[][] lvlData;
    private BufferedImage image;

    private ArrayList<Crabby> crabs = new ArrayList<>();
    private ArrayList<Potion> potions = new ArrayList<>();
    private ArrayList<GameContainer> containers = new ArrayList<>();
    private ArrayList<Spike> spikes = new ArrayList<>();
    private ArrayList<Cannon> cannons = new ArrayList<>();
    private ArrayList<Grass> grass = new ArrayList<>();

    private int levelTilesWide;
    private int maxTilesOffset;
    private int maxLevelOffsetX;
    private Point playerSpawn;

    public Level(BufferedImage image) {
        this.image = image;
        lvlData = new int[image.getHeight()][image.getWidth()];
        loadLevel();
        calculateOffsets();
    }

    private void loadLevel() {
        for (int y = 0; y < image.getHeight(); y++)
            for (int x = 0; x < image.getWidth(); x++) {
                Color c = new Color(image.getRGB(x, y));
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();

                loadLevelData(red, x, y);
                loadEntities(green, x, y);
                loadObjects(blue, x, y);
            }
    }

    private void loadLevelData(int red, int x, int y) {
        if (red >= 50)
            lvlData[y][x] = 0;
        else
            lvlData[y][x] = red;

        switch (red) {
            case 0, 1, 2, 3, 30, 31, 33, 34, 35, 36, 37, 38, 39 -> grass.add(new Grass((int) (x * TILES_SIZE), (int) (y * TILES_SIZE), getRandomGrassType(x)));
        }
    }

    private int getRandomGrassType(int x) {
        return x % 2;
    }

    private void loadEntities(int green, int x, int y) {
        switch (green) {
            case CRABBY -> crabs.add(new Crabby(x * TILES_SIZE, y * TILES_SIZE));
            case 100 -> playerSpawn = new Point(x * TILES_SIZE, y * TILES_SIZE);
        }
    }

    private void loadObjects(int blue, int x, int y) {
        switch (blue) {
            case RED_POTION, BLUE_POTION -> potions.add(new Potion(x * TILES_SIZE, y * TILES_SIZE, blue));
            case BARREL, BOX -> containers.add(new GameContainer(x * TILES_SIZE, y * TILES_SIZE, blue));
            case SPIKE -> spikes.add(new Spike(x * TILES_SIZE, y * TILES_SIZE, SPIKE));
            case CANNON_LEFT, CANNON_RIGHT -> cannons.add(new Cannon(x * TILES_SIZE, y * TILES_SIZE, blue));
        }
    }

    private void calculateOffsets() {
        levelTilesWide = image.getWidth();
        maxTilesOffset = levelTilesWide - TILES_IN_WIDTH;
        maxLevelOffsetX = TILES_SIZE * maxTilesOffset;
    }

    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }

    public int[][] getLevelData() {
        return lvlData;
    }

    public int getLevelOffsetX() {
        return maxLevelOffsetX;
    }

    public ArrayList<Crabby> getCrabs() {
        return crabs;
    }

    public ArrayList<GameContainer> getContainers() {
        return containers;
    }

    public ArrayList<Potion> getPotions() {
        return potions;
    }

    public ArrayList<Spike> getSpikes() {
        return spikes;
    }

    public ArrayList<Cannon> getCannons() {
        return cannons;
    }

    public ArrayList<Grass> getGrass() {
        return grass;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }
}