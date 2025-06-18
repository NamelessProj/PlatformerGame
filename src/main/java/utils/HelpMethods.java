package utils;

import entities.Crabby;
import objects.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.*;
import static utils.Constants.EnemyConstants.CRABBY;
import static utils.Constants.GameConstants.TILES_SIZE;
import static utils.Constants.ObjectConstants.*;
import static utils.Constants.PlayerConstants.PLAYER_SPAWN_ID;

public class HelpMethods {
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

    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {
        int val = lvlData[yTile][xTile];
        if (val >= 48 || val < 0 || val != 11)
            return true;
        return false;
    }

    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        if (!IsSolid(x, y, lvlData))
            if (!IsSolid(x + width, y + height, lvlData))
                if (!IsSolid(x + width, y, lvlData))
                    if (!IsSolid(x, y + height, lvlData))
                        return true;

        return false;
    }

    public static float GetEntityXPositionNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = (int) (hitbox.x / GameConstants.TILES_SIZE);
        if (xSpeed > 0) { // Right
            int tileXPos = currentTile * GameConstants.TILES_SIZE;
            int xOffset = (int) (GameConstants.TILES_SIZE - hitbox.width);
            return tileXPos + xOffset - 1;
        } else // Left
            return currentTile * GameConstants.TILES_SIZE;
    }

    public static float GetEntityYPositionUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int) (hitbox.y / GameConstants.TILES_SIZE);
        if (airSpeed > 0) { // Falling - touching floor
            int tileYPos = currentTile * GameConstants.TILES_SIZE;
            int yOffset = (int) (GameConstants.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1;
        } else // Jumping
            return currentTile * GameConstants.TILES_SIZE;
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        // Check the pixels below bottom left and bottom right
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
                return false;

        return true;
    }

    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        if (xSpeed > 0) // Moving right
            return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        else // Moving left
            return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }

    public static boolean IsProjectileHittingLevel(Projectile p, int[][] lvlData) {
        return IsSolid(p.getHitbox().x + p.getHitbox().width / 2, p.getHitbox().y + p.getHitbox().height / 2, lvlData);
    }

    public static boolean CanCannonSeePlayer(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
        int firstXTile = (int) (firstHitbox.x / GameConstants.TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / GameConstants.TILES_SIZE);

        if (firstXTile > secondXTile)
            return IsAllTilesClear(secondXTile, firstXTile, yTile, lvlData);
        else
            return IsAllTilesClear(firstXTile, secondXTile, yTile, lvlData);
    }

    public static boolean IsAllTilesClear(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++)
            if (IsTileSolid(xStart + i, y, lvlData))
                return false;
        return true;
    }

    public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
        if (IsAllTilesClear(xStart, xEnd, y, lvlData))
            for (int i = 0; i < xEnd - xStart; i++)
                if (!IsTileSolid(xStart + i, y + 1, lvlData))
                    return false;
        return true;
    }

    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
        int firstXTile = (int) (firstHitbox.x / GameConstants.TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / GameConstants.TILES_SIZE);

        if (firstXTile > secondXTile)
            return IsAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
        else
            return IsAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);
    }

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