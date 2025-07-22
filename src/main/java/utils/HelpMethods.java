package utils;

import objects.*;
import ui.MyButton;

import utils.Constants.GameConstants;

import java.awt.geom.Rectangle2D;

import java.awt.event.MouseEvent;

import static utils.Constants.GameConstants.TILES_SIZE;

public class HelpMethods {
    /**
     * Checks if a given position is solid in the level data.
     * </p>
     * This method also check if the tile is solid using {@link #IsTileSolid(int, int, int[][])}.
     * @param x the x-coordinate to check
     * @param y the y-coordinate to check
     * @param lvlData the level data array
     * @return {@code true} if the position is solid, {@code false} otherwise
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
     * @return {@code true} if the tile is solid, {@code false} otherwise
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
     * </>
     * This method checks if the entity's hitbox can move to the specified position without colliding with solid tiles {@link #IsSolid(float, float, int[][])}.
     * @param x the x-coordinate of the entity
     * @param y the y-coordinate of the entity
     * @param width the width of the entity
     * @param height the height of the entity
     * @param lvlData the level data array
     * @return {@code true} if the entity can move to the position, {@code false} otherwise
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
     * </p>
     * This method checks the pixels below the bottom left and bottom right corners of the entity's hitbox to determine if it is on the floor.
     * @param hitbox the hitbox of the entity
     * @param lvlData the level data array
     * @return {@code true} if the entity is on the floor, {@code false} otherwise
     */
    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        // Check the pixels below bottom left and bottom right
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
            return IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData);

        return true;
    }

    /**
     * Checks if an entity is on the floor based on its hitbox and horizontal speed.
     * </p>
     * This method checks the pixels below the bottom left and bottom right corners of the entity's hitbox to determine if it is on the floor, considering its horizontal speed.
     * @param hitbox the hitbox of the entity
     * @param xSpeed the horizontal speed of the entity
     * @param lvlData the level data array
     * @return {@code true} if the entity is on the floor, {@code false} otherwise
     */
    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        if (xSpeed > 0) // Moving right
            return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        else // Moving left
            return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }

    /**
     * Checks if an entity is on the floor based on its hitbox and level data.
     * </p>
     * This method checks the pixels below the bottom left and bottom right corners of the entity's hitbox to determine if it is on the floor.
     * @param hitbox the hitbox of the entity
     * @param lvlData the level data array
     * @return {@code true} if the entity is on the floor, {@code false} otherwise
     */
    public static boolean IsFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
            if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
                return false;
        return true;
    }

    /**
     * Checks if a projectile is hitting a solid tile in the level data.
     * @param p the projectile to check
     * @param lvlData the level data array
     * @return {@code true} if the projectile is hitting a solid tile, {@code false} otherwise
     */
    public static boolean IsProjectileHittingLevel(Projectile p, int[][] lvlData) {
        return IsSolid(p.getHitbox().x + p.getHitbox().width / 2, p.getHitbox().y + p.getHitbox().height / 2, lvlData);
    }

    /**
     * Checks if an entity is in water based on its hitbox and level data.
     * @param hitbox the hitbox of the entity
     * @param lvlData the level data array
     * @return {@code true} if the entity is in water, {@code false} otherwise
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
     * @return {@code true} if the cannon can see the player, {@code false} otherwise
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
     * @return {@code true} if all tiles in the range are clear, {@code false} otherwise
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
     * @return {@code true} if all tiles in the range are walkable, {@code false} otherwise
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
     * @return {@code true} if the sight is clear, {@code false} otherwise
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
     * Checks if a string can be parsed as a float.
     * @param val the string to check
     * @return {@code true} if the string can be parsed as a float, {@code false} otherwise
     */
    public static boolean IsFloat(String val) {
        try {
            Float.parseFloat(val);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if a string can be parsed as an integer.
     * @param val the string to check
     * @return {@code true} if the string can be parsed as an integer, {@code false} otherwise
     */
    public static boolean IsInt(String val) {
        try {
            Integer.parseInt(val);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the mouse event is within the bounds of the specified button.
     * @param e the {@link MouseEvent} to check
     * @param b the {@link MyButton} to check
     * @return {@code true} if the mouse is within the button bounds, {@code false} otherwise
     */
    public static boolean IsIn(MouseEvent e, MyButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}