package utils;

import entities.Crabby;
import objects.*;
import utils.Constants.GameConstants;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.EnemyConstants.CRABBY;
import static utils.Constants.GameConstants.TILES_SIZE;
import static utils.Constants.ObjectConstants.*;
import static utils.Constants.PlayerConstants.PLAYER_SPAWN_ID;

public class HelpMethods {
    /**
     * Checks if a given position is solid in the level data.
     * @param x the x-coordinate to check
     * @param y the y-coordinate to check
     * @param lvlData the level data array
     * @return true if the position is solid, false otherwise
     */
    private static boolean IsSolid(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * GameConstants.TILES_SIZE;

        if (x < 0 || x >= maxWidth)
            return true;
        if (y < 0 || y >= GameConstants.GAME_HEIGHT)
            return true;

        float xIndex = x / GameConstants.TILES_SIZE;
        float yIndex = y / GameConstants.TILES_SIZE;

        return IsTileSolid((int) xIndex, (int) yIndex, lvlData);
    }

    /**
     * Checks if a tile at the specified coordinates is solid in the level data.
     * @param xTile the x-coordinate of the tile
     * @param yTile the y-coordinate of the tile
     * @param lvlData the level data array
     * @return true if the tile is solid, false otherwise
     */
    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {
        int val = lvlData[yTile][xTile];

        return switch (val) {
            case 11, 48, 49 -> false;
            default -> true;
        };
    }

    /**
     * Checks if an entity can move to a specified position without colliding with solid tiles.
     * @param x the x-coordinate of the entity
     * @param y the y-coordinate of the entity
     * @param width the width of the entity
     * @param height the height of the entity
     * @param lvlData the level data array
     * @return true if the entity can move to the position, false otherwise
     */
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        if (!IsSolid(x, y, lvlData))
            if (!IsSolid(x + width, y + height, lvlData))
                if (!IsSolid(x + width, y, lvlData))
                    if (!IsSolid(x, y + height, lvlData))
                        return true;

        return false;
    }

    /**
     * Calculates the next x position of an entity next to a wall based on its hitbox and speed.
     * @param hitbox the hitbox of the entity
     * @param xSpeed the horizontal speed of the entity
     * @return the next x position of the entity next to a wall
     */
    public static float GetEntityXPositionNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = (int) (hitbox.x / GameConstants.TILES_SIZE);
        if (xSpeed > 0) { // Right
            int tileXPos = currentTile * GameConstants.TILES_SIZE;
            int xOffset = (int) (GameConstants.TILES_SIZE - hitbox.width);
            return tileXPos + xOffset - 1;
        } else // Left
            return currentTile * GameConstants.TILES_SIZE;
    }

    /**
     * Calculates the y position of an entity under a roof or above a floor based on its hitbox and air speed.
     * @param hitbox the hitbox of the entity
     * @param airSpeed the vertical speed of the entity (positive for falling, negative for jumping)
     * @return the y position of the entity under a roof or above a floor
     */
    public static float GetEntityYPositionUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int) (hitbox.y / GameConstants.TILES_SIZE);
        if (airSpeed > 0) { // Falling - touching floor
            int tileYPos = currentTile * GameConstants.TILES_SIZE;
            int yOffset = (int) (GameConstants.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1;
        } else // Jumping
            return currentTile * GameConstants.TILES_SIZE;
    }

    /**
     * Checks if an entity is on the floor based on its hitbox and level data.
     * @param hitbox the hitbox of the entity
     * @param lvlData the level data array
     * @return true if the entity is on the floor, false otherwise
     */
    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        // Check the pixels below bottom left and bottom right
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
            return IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData);

        return true;
    }

    /**
     * Checks if an entity is on the floor based on its hitbox and horizontal speed.
     * @param hitbox the hitbox of the entity
     * @param xSpeed the horizontal speed of the entity
     * @param lvlData the level data array
     * @return true if the entity is on the floor, false otherwise
     */
    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        if (xSpeed > 0) // Moving right
            return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        else // Moving left
            return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }

    /**
     * Checks if a projectile is hitting a solid tile in the level data.
     * @param p the projectile to check
     * @param lvlData the level data array
     * @return true if the projectile is hitting a solid tile, false otherwise
     */
    public static boolean IsProjectileHittingLevel(Projectile p, int[][] lvlData) {
        return IsSolid(p.getHitbox().x + p.getHitbox().width / 2, p.getHitbox().y + p.getHitbox().height / 2, lvlData);
    }

    /**
     * Checks if an entity is in water based on its hitbox and level data.
     * @param hitbox the hitbox of the entity
     * @param lvlData the level data array
     * @return true if the entity is in water, false otherwise
     */
    public static boolean IsEntityInWater(Rectangle2D.Float hitbox, int[][] lvlData) {
        // Will only check if entity touch top water. Can't reach bottom water if not
		// touched top water.
        if (GetTileValue(hitbox.x, hitbox.y + hitbox.height, lvlData) != 48)
            if (GetTileValue(hitbox.x + hitbox.width, hitbox.y + hitbox.height, lvlData) != 48)
                return false;
        return true;
    }

    /**
     * Gets the tile value at a specific position in the level data.
     * @param x the x-coordinate of the position
     * @param y the y-coordinate of the position
     * @param lvlData the level data array
     * @return the tile value at the specified position
     */
    private static int GetTileValue(float x, float y, int[][] lvlData) {
        int xCord = (int) (x / TILES_SIZE);
        int yCord = (int) (y / TILES_SIZE);
        return lvlData[yCord][xCord];
    }

    /**
     * Checks if a cannon can see the player based on their hitboxes and level data.
     * @param lvlData the level data array
     * @param firstHitbox the hitbox of the first entity (cannon)
     * @param secondHitbox the hitbox of the second entity (player)
     * @param yTile the y-coordinate of the tile to check visibility
     * @return true if the cannon can see the player, false otherwise
     */
    public static boolean CanCannonSeePlayer(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
        int firstXTile = (int) (firstHitbox.x / GameConstants.TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / GameConstants.TILES_SIZE);

        if (firstXTile > secondXTile)
            return IsAllTilesClear(secondXTile, firstXTile, yTile, lvlData);
        else
            return IsAllTilesClear(firstXTile, secondXTile, yTile, lvlData);
    }

    /**
     * Checks if all tiles in a specified range are clear (not solid) at a given y-coordinate.
     * @param xStart the starting x-coordinate of the range
     * @param xEnd the ending x-coordinate of the range
     * @param y the y-coordinate to check
     * @param lvlData the level data array
     * @return true if all tiles in the range are clear, false otherwise
     */
    public static boolean IsAllTilesClear(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++)
            if (IsTileSolid(xStart + i, y, lvlData))
                return false;
        return true;
    }

    /**
     * Checks if all tiles in a specified range are walkable (not solid) at a given y-coordinate,
     * @param xStart the starting x-coordinate of the range
     * @param xEnd the ending x-coordinate of the range
     * @param y the y-coordinate to check
     * @param lvlData the level data array
     * @return true if all tiles in the range are walkable, false otherwise
     */
    public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
        if (IsAllTilesClear(xStart, xEnd, y, lvlData))
            for (int i = 0; i < xEnd - xStart; i++)
                if (!IsTileSolid(xStart + i, y + 1, lvlData))
                    return false;
        return true;
    }

    /**
     * Checks if the sight between an enemy and a player is clear at a given y-coordinate.
     * @param lvlData the level data array
     * @param enemyBox the hitbox of the enemy
     * @param playerBox the hitbox of the player
     * @param yTile the y-coordinate of the tile to check visibility
     * @return true if the sight is clear, false otherwise
     */
    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float enemyBox, Rectangle2D.Float playerBox, int yTile) {
        int firstXTile = (int) (enemyBox.x / GameConstants.TILES_SIZE);
        int secondXTile;

        if (IsSolid(playerBox.x, playerBox.y + playerBox.width + 1, lvlData))
            secondXTile = (int) (playerBox.x / GameConstants.TILES_SIZE);
        else
            secondXTile = (int) ((playerBox.x + playerBox.width) / GameConstants.TILES_SIZE);

        if (firstXTile > secondXTile)
            return IsAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
        else
            return IsAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);
    }

    /**
     * Gets the level data from a BufferedImage.
     * @param img the BufferedImage representing the level
     * @return a 2D array of integers representing the level data
     */
    public static int[][] GetLevelData(BufferedImage img) {
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int val = color.getRed();
                if (val >= 48)
                    val = 0;
                lvlData[j][i] = val;
            }
        return lvlData;
    }

    /**
     * Gets a list of crabs from a BufferedImage.
     * @param img the BufferedImage representing the level
     * @return an ArrayList of Crabby objects representing the crabs in the level
     */
    public static ArrayList<Crabby> GetCrabs(BufferedImage img) {
        ArrayList<Crabby> list = new ArrayList<>();

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int val = color.getGreen();
                if (val == CRABBY)
                    list.add(new Crabby(i * TILES_SIZE, j * TILES_SIZE));
            }
        return list;
    }

    /**
     * Gets the spawn point of the player from a BufferedImage.
     * @param img the BufferedImage representing the level
     * @return a Point representing the player's spawn position
     */
    public static Point GetPlayerSpawn(BufferedImage img) {
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int val = color.getGreen();
                if (val == PLAYER_SPAWN_ID)
                    return new Point(i * TILES_SIZE, j * TILES_SIZE);
            }
        return new Point(TILES_SIZE, TILES_SIZE); // Default spawn if not found
    }

    /**
     * Gets a list of potions from a BufferedImage.
     * @param img the BufferedImage representing the level
     * @return an ArrayList of Potion objects representing the potions in the level
     */
    public static ArrayList<Potion> GetPotions(BufferedImage img) {
        ArrayList<Potion> list = new ArrayList<>();

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int val = color.getBlue();
                if (val == RED_POTION || val == BLUE_POTION)
                    list.add(new Potion(i * TILES_SIZE, j * TILES_SIZE, val));
            }
        return list;
    }

    /**
     * Gets a list of containers (boxes and barrels) from a BufferedImage.
     * @param img the BufferedImage representing the level
     * @return an ArrayList of GameContainer objects representing the containers in the level
     */
    public static ArrayList<GameContainer> GetContainers(BufferedImage img) {
        ArrayList<GameContainer> list = new ArrayList<>();

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int val = color.getBlue();
                if (val == BOX || val == BARREL)
                    list.add(new GameContainer(i * TILES_SIZE, j * TILES_SIZE, val));
            }
        return list;
    }

    /**
     * Gets a list of spikes from a BufferedImage.
     * @param img the BufferedImage representing the level
     * @return an ArrayList of Spike objects representing the spikes in the level
     */
    public static ArrayList<Spike> GetSpikes(BufferedImage img) {
        ArrayList<Spike> list = new ArrayList<>();

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int val = color.getBlue();
                if (val == SPIKE)
                    list.add(new Spike(i * TILES_SIZE, j * TILES_SIZE, SPIKE));
            }
        return list;
    }

    /**
     * Gets a list of cannons from a BufferedImage.
     * @param img the BufferedImage representing the level
     * @return an ArrayList of Cannon objects representing the cannons in the level
     */
    public static ArrayList<Cannon> GetCannons(BufferedImage img) {
        ArrayList<Cannon> list = new ArrayList<>();

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int val = color.getBlue();
                if (val == CANNON_LEFT || val == CANNON_RIGHT)
                    list.add(new Cannon(i * TILES_SIZE, j * TILES_SIZE, val));
            }
        return list;
    }
}