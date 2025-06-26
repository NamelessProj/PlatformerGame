package levels;

import entities.Crabby;
import entities.Pinkstar;
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
    private ArrayList<Pinkstar> pinkstars = new ArrayList<>();
    private ArrayList<Potion> potions = new ArrayList<>();
    private ArrayList<GameContainer> containers = new ArrayList<>();
    private ArrayList<Spike> spikes = new ArrayList<>();
    private ArrayList<Cannon> cannons = new ArrayList<>();
    private ArrayList<Grass> grass = new ArrayList<>();

    private int levelTilesWide;
    private int maxTilesOffset;
    private int maxLevelOffsetX;
    private Point playerSpawn;

    /**
     * Constructor for the Level class.
     * @param image the BufferedImage representing the level
     */
    public Level(BufferedImage image) {
        this.image = image;
        lvlData = new int[image.getHeight()][image.getWidth()];
        loadLevel();
        calculateOffsets();
    }

    /**
     * Loads the level data from the BufferedImage.
     */
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

    /**
     * Loads the level data based on the red value of the pixel.
     * @param red the red component of the pixel color
     * @param x the x-coordinate of the pixel in the level
     * @param y the y-coordinate of the pixel in the level
     */
    private void loadLevelData(int red, int x, int y) {
        if (red >= 50)
            lvlData[y][x] = 0;
        else
            lvlData[y][x] = red;

        switch (red) {
            case 0, 1, 2, 3, 30, 31, 33, 34, 35, 36, 37, 38, 39 -> grass.add(new Grass((int) (x * TILES_SIZE), (int) (y * TILES_SIZE), getRandomGrassType(x)));
        }
    }

    /**
     * Returns a random grass type based on the x-coordinate.
     * @param x the x-coordinate of the pixel in the level
     * @return the grass type (0 or 1)
     */
    private int getRandomGrassType(int x) {
        return x % 2;
    }

    /**
     * Loads entities based on the green value of the pixel.
     * @param green the green component of the pixel color
     * @param x the x-coordinate of the pixel in the level
     * @param y the y-coordinate of the pixel in the level
     */
    private void loadEntities(int green, int x, int y) {
        switch (green) {
            case CRABBY -> crabs.add(new Crabby(x * TILES_SIZE, y * TILES_SIZE));
            case PINKSTAR -> pinkstars.add(new Pinkstar(x * TILES_SIZE, y * TILES_SIZE));
            case 100 -> playerSpawn = new Point(x * TILES_SIZE, y * TILES_SIZE);
        }
    }

    /**
     * Loads objects based on the blue value of the pixel.
     * @param blue the blue component of the pixel color
     * @param x the x-coordinate of the pixel in the level
     * @param y the y-coordinate of the pixel in the level
     */
    private void loadObjects(int blue, int x, int y) {
        switch (blue) {
            case RED_POTION, BLUE_POTION -> potions.add(new Potion(x * TILES_SIZE, y * TILES_SIZE, blue));
            case BARREL, BOX -> containers.add(new GameContainer(x * TILES_SIZE, y * TILES_SIZE, blue));
            case SPIKE -> spikes.add(new Spike(x * TILES_SIZE, y * TILES_SIZE, SPIKE));
            case CANNON_LEFT, CANNON_RIGHT -> cannons.add(new Cannon(x * TILES_SIZE, y * TILES_SIZE, blue));
        }
    }

    /**
     * Calculates the offsets for the level based on the image dimensions.
     */
    private void calculateOffsets() {
        levelTilesWide = image.getWidth();
        maxTilesOffset = levelTilesWide - TILES_IN_WIDTH;
        maxLevelOffsetX = TILES_SIZE * maxTilesOffset;
    }

    /**
     * Returns the sprite index for a given tile position.
     * @param x the x-coordinate of the tile
     * @param y the y-coordinate of the tile
     * @return the sprite index for the tile at (x, y)
     */
    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }

    /**
     * Returns the level data as a 2D array.
     * @return the level data as a 2D array of integers
     */
    public int[][] getLevelData() {
        return lvlData;
    }

    /**
     * Get the level offset for the x-axis.
     */
    public int getLevelOffsetX() {
        return maxLevelOffsetX;
    }

    /**
     * Returns all crabs in the level.
     * @return an ArrayList of Crabby objects
     */
    public ArrayList<Crabby> getCrabs() {
        return crabs;
    }

    /**
     * Returns all pink stars in the level.
     * @return an ArrayList of Pinkstar objects
     */
    public ArrayList<Pinkstar> getPinkstars() {
		return pinkstars;
	}

    /**
     * Returns all game containers in the level.
     * @return an ArrayList of GameContainer objects
     */
    public ArrayList<GameContainer> getContainers() {
        return containers;
    }

    /**
     * Returns all potions in the level.
     * @return an ArrayList of Potion objects
     */
    public ArrayList<Potion> getPotions() {
        return potions;
    }

    /**
     * Returns all spikes in the level.
     * @return an ArrayList of Spike objects
     */
    public ArrayList<Spike> getSpikes() {
        return spikes;
    }

    /**
     * Returns all cannons in the level.
     * @return an ArrayList of Cannon objects
     */
    public ArrayList<Cannon> getCannons() {
        return cannons;
    }

    /**
     * Returns all grass objects in the level.
     * @return an ArrayList of Grass objects
     */
    public ArrayList<Grass> getGrass() {
        return grass;
    }

    /**
     * Returns the player spawn point in the level.
     * @return a Point representing the player's spawn location
     */
    public Point getPlayerSpawn() {
        return playerSpawn;
    }
}