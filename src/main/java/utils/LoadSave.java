package utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utils.Constants.GameConstants.TILES_IN_HEIGHT;
import static utils.Constants.GameConstants.TILES_IN_WIDTH;

public class LoadSave {
    public static final String PLAYER_ATLAS = "images/player_sprites.png";
    public static final String LEVEL_ATLAS = "images/outside_sprites.png";
    public static final String LEVEL_ONE_DATA = "images/level_one_data.png";

    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    public static int[][] GetLevelData() {
        int[][] lvlData = new int[TILES_IN_HEIGHT][TILES_IN_WIDTH];
        BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);

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
}