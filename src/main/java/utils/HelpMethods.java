package utils;

import static utils.Constants.*;

public class HelpMethods {
    private static boolean IsSolid(float x, float y, int[][] lvlData) {
        if (x < 0 || x >= GameConstants.GAME_WIDTH)
            return true;
        if (y < 0 || y >= GameConstants.GAME_HEIGHT)
            return true;

        float xIndex = x / GameConstants.TILES_SIZE;
        float yIndex = y / GameConstants.TILES_SIZE;

        int val = lvlData[(int) yIndex][(int) xIndex];

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
}