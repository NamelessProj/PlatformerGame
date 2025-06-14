package utils;

import java.awt.geom.Rectangle2D;

import static utils.Constants.*;

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
        return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }

    public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++) {
            if (IsTileSolid(xStart + i, y, lvlData))
                return false;
            if (!IsTileSolid(xStart + i, y + 1, lvlData))
                return false;
        }
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
}