package levels;

import entities.Crabby;
import objects.Cannon;
import objects.GameContainer;
import objects.Potion;
import objects.Spike;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.GameConstants.*;
import static utils.HelpMethods.*;

public class Level {
    private BufferedImage image;
    private int[][] lvlData;
    private ArrayList<Crabby> crabs;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;
    private ArrayList<Cannon> cannons;
    private int levelTilesWide;
    private int maxTilesOffset;
    private int maxLevelOffsetX;
    private Point playerSpawn;

    public Level(BufferedImage image) {
        this.image = image;
        createLevelData();
        createEnemies();
        createPotions();
        createContainers();
        createSpikes();
        createCannons();
        calculateLevelOffsets();
        calculatePlayerSpawn();
    }

    private void createCannons() {
        cannons = GetCannons(image);
    }

    private void createSpikes() {
        spikes = GetSpikes(image);
    }

    private void createContainers() {
        containers = GetContainers(image);
    }

    private void createPotions() {
        potions = GetPotions(image);
    }

    private void calculatePlayerSpawn() {
        playerSpawn = GetPlayerSpawn(image);
    }

    private void calculateLevelOffsets() {
        levelTilesWide = image.getWidth();
        maxTilesOffset = levelTilesWide - TILES_IN_WIDTH;
        maxLevelOffsetX = TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        crabs = GetCrabs(image);
    }

    private void createLevelData() {
        lvlData = GetLevelData(image);
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

    public Point getPlayerSpawn() {
        return playerSpawn;
    }
}