package utils;

import javax.imageio.ImageIO;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;

public class LoadSave {
    public static class Images {
        public static final String ICON = "icon.png";
        public static final String PLAYING_BACKGROUND_IMAGE = "playing_bg_img.png";
        public static final String BIG_CLOUDS = "big_clouds.png";
        public static final String SMALL_CLOUDS = "small_clouds.png";
        public static final String STATUS_BAR = "health_power_bar.png";

        public static final String MENU_BACKGROUND = "menu_background.png";
        public static final String PAUSE_BACKGROUND = "pause_menu.png";
        public static final String MENU_BACKGROUND_IMAGE = "background_menu.png";
        public static final String COMPLETED_IMG = "completed_sprite.png";
        public static final String DEATH_SCREEN = "death_screen.png";
        public static final String OPTIONS_MENU = "options_background.png";
        public static final String GAME_COMPLETED = "game_completed.png";
    }

    public static class Sprites {
        public static final String PLAYER_ATLAS = "player_sprites.png";
        public static final String LEVEL_ATLAS = "outside_sprites.png";
        public static final String MENU_BUTTONS = "button_atlas.png";
        public static final String SOUND_BUTTONS = "sound_button.png";
        public static final String URM_BUTTONS = "urm_buttons.png";
        public static final String VOLUME_BUTTONS = "volume_buttons.png";

        public static final String QUESTION_ATLAS = "question_atlas.png";
        public static final String EXCLAMATION_ATLAS = "exclamation_atlas.png";

        public static final String CRABBY_SPRITE = "crabby_sprite.png";
        public static final String PINKSTAR_ATLAS = "pinkstar_atlas.png";
        public static final String SHARK_ATLAS = "shark_atlas.png";

        public static final String POTION_ATLAS = "potions_sprites.png";
        public static final String CONTAINER_ATLAS = "objects_sprites.png";
        public static final String TRAP_ATLAS = "trap_atlas.png";
        public static final String CANNON_ATLAS = "cannon_atlas.png";
        public static final String CANNON_BALL = "ball.png";
        public static final String GRASS_ATLAS = "grass_atlas.png";
        public static final String WATER_TOP = "water_atlas_animation.png";
        public static final String WATER_BOTTOM = "water.png";
    }

    public static class Texts {
        public static final String CREDITS = "credits.txt";
        public static final String EXT_FOLDER = "data/";
        public static final String SETTINGS = "settings.txt";
        public static final String SAVE = "savegame.txt";
    }

    public static class Fonts {
        public static final String TITLE = "Jersey10-Regular.ttf";
    }

    /**
     * Loads a sprite atlas image from the resources.
     * @param fileName the name of the sprite atlas file
     * @return BufferedImage containing the sprite atlas
     */
    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/images/" + fileName);
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

    /**
     * Loads all level images from the resources.
     * @return BufferedImage array containing all level images
     */
    public static BufferedImage[] GetAllLevels() {
        URL url = LoadSave.class.getResource("/images/lvls");
        File file = null;

        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File[] files = file.listFiles();
        File[] sortedFiles = new File[files.length];

        for (int i = 0; i < sortedFiles.length; i++)
            for (int j = 0; j < files.length; j++) {
                if (files[j].getName().equals((i + 1) + ".png"))
                    sortedFiles[i] = files[j];
            }

        BufferedImage[] imgs = new BufferedImage[sortedFiles.length];

        for (int i = 0; i < imgs.length; i++) {
            try {
                imgs[i] = ImageIO.read(sortedFiles[i]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return imgs;
    }

    /**
     * Loads a text file from the resources.
     * @param fileName the name of the text file to load
     * @return BufferedReader to read the text file
     */
    public static BufferedReader GetText(String fileName) {
        BufferedReader br = null;
        InputStream is = null;

        if (fileName.equals(Texts.SETTINGS)) {
            File file = new File(Texts.EXT_FOLDER + fileName);
            if (file.exists()) {
                try {
                    is = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else
            is = LoadSave.class.getResourceAsStream("/texts/" + fileName);

        if (is != null)
            br = new BufferedReader(new InputStreamReader(is));

        return br;
    }

    /**
     * Loads a font from the resources.
     * @param fileName the name of the font file to load
     * @return Font object representing the loaded font
     */
    public static Font GetFont(String fileName) {
        Font font = null;
        InputStream is = LoadSave.class.getResourceAsStream("/fonts/" + fileName);

        try {
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return font;
    }

    /**
     * Saves text to a file in the specified directory.
     * @param fileName the name of the file to save
     * @param text the text content to save
     */
    public static void SaveText(String fileName, String text) {
        File dir = new File(Texts.EXT_FOLDER);
        if (!dir.exists())
            dir.mkdirs();

        File file = new File(Texts.EXT_FOLDER + fileName);

        try {
            if (!file.exists())
                if (!file.createNewFile())
                    throw new IOException("Could not create file: " + file.getAbsolutePath());

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}