package gamestates;

import static utils.Constants.GameConstants.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.util.ArrayList;

import mainWindow.Game;
import utils.LoadSave;

public class Credits extends State implements Statemethods {
    private final String HEADER_1 = "#";
    private final String HEADER_2 = "##";
    private final String HEADER_3 = "###";

    private final int TYPE_TEXT = 0;
    private final int TYPE_HEADER_1 = 1;
    private final int TYPE_HEADER_2 = 2;
    private final int TYPE_HEADER_3 = 3;

    private final int MAX_Y_POS = -120;

    private BufferedImage backgroundImg;
    private ArrayList<CreditsItem> creditsItems = new ArrayList<>();
    private Font font;



    private class CreditsItem {
        private int type, x, y, yPosition;
        private String text;
        private boolean isActive;

        /**
         * Constructor for CreditsItem.
         * @param x the x-coordinate of the item
         * @param y the initial y-coordinate of the item
         * @param type the type of the item (text or header)
         * @param text the text of the item
         */
        public CreditsItem(int x, int y, int type, String text) {
            this.x = x;
            this.y = y;
            this.yPosition = y;
            this.type = type;
            this.text = text;
            this.isActive = true;
        }

        /**
         * Gets the text of the credits item.
         * @return the text of the item
         */
        public String getText() {
            return text;
        }

        /**
         * Gets the x-coordinate of the credits item.
         * @return the x-coordinate
         */
        public int getX() {
            return x;
        }

        /**
         * Gets the y-coordinate of the credits item as it moves.
         * @return the current y-coordinate
         */
        public int getY() {
            return yPosition;
        }

        /**
         * Gets the type of the credits item.
         * @return the type of the item
         */
        public int getType() {
            return type;
        }

        /**
         * Checks if the credits item is active.
         * @return true if the item is active, false otherwise
         */
        public boolean isActive() {
            return isActive;
        }

        /**
         * Sets the active state of the credits item.
         * @param isActive true to set the item as active, false to deactivate it
         */
        public void setActive(boolean isActive) {
            this.isActive = isActive;
        }

        /**
         * Updates the position of the credits item, moving it upwards.
         */
        public void update() {
            yPosition -= (int) (0.5f * SCALE);
        }

        /**
         * Resets the y-coordinate of the credits item to its initial position.
         */
        public void reset() {
            yPosition = y;
            isActive = true;
        }
    }



    /**
     * Constructor for the Credits class.
     * @param game the Game instance that this Credits state belongs to.
     */
    public Credits(Game game) {
        super(game);
        loadBackgroundImage();
        loadFont();
        loadCredits();
    }

    /**
     * Loads the font used for displaying credits.
     */
    private void loadFont() {
        font = LoadSave.GetFont(LoadSave.Fonts.TITLE);
    }

    /**
     * Loads the credits from a text file.
     */
    private void loadCredits() {
        BufferedReader reader = LoadSave.GetText(LoadSave.Texts.CREDITS);

        try {
            int startMargin = (int) (100 * SCALE);
            int margin = (int) (50 * SCALE);
            int x = (int) (GAME_WIDTH / 6 * SCALE);
            String line = reader.readLine();
            int i = 0;
            while (line != null) {
                if (!line.trim().isEmpty()) {
                    int y = i * margin + GAME_HEIGHT + startMargin;
                    if (line.startsWith(HEADER_3))
                        creditsItems.add(new CreditsItem(x, y, TYPE_HEADER_3, line.substring(HEADER_3.length()).trim()));
                    else if (line.startsWith(HEADER_2))
                        creditsItems.add(new CreditsItem(x, y, TYPE_HEADER_2, line.substring(HEADER_2.length()).trim()));
                    else if (line.startsWith(HEADER_1))
                        creditsItems.add(new CreditsItem(x, y, TYPE_HEADER_1, line.substring(HEADER_1.length()).trim()));
                    else
                        creditsItems.add(new CreditsItem(x, y, TYPE_TEXT, line.trim()));

                    i++;
                }

                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the background image for the credits screen.
     */
    private void loadBackgroundImage() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.Images.MENU_BACKGROUND_IMAGE);
    }

    /**
     * Updates the position of each credits item, moving them upwards.
     */
    private void updateCreditsItems() {
        for (CreditsItem ci : creditsItems)
            if (ci.isActive()) {
                ci.update();
                if (ci.getY() < MAX_Y_POS)
                    ci.setActive(false);
            }

        if (creditsItems.getLast().getY() < MAX_Y_POS)
            goTo(Gamestate.MENU);
    }

    /**
     * Changes the game state to the specified Gamestate and resets all credits items.
     * @param state the Gamestate to switch to.
     */
    private void goTo(Gamestate state) {
        setGamestate(state);
        for (CreditsItem ci : creditsItems)
            ci.reset();
    }

    @Override
    public void update() {
        updateCreditsItems();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);

        drawCreditsItems(g);
    }

    /**
     * Draws the credits items on the screen.
     * @param g the Graphics object used for drawing.
     */
    private void drawCreditsItems(Graphics g) {
        g.setColor(Color.BLACK);
        float fontSize = 16f * SCALE;
        for (CreditsItem ci : creditsItems)
            if (ci.isActive()) {
                switch (ci.getType()) {
                    case TYPE_HEADER_1 -> fontSize = 30f * SCALE;
                    case TYPE_HEADER_2 -> fontSize = 25f * SCALE;
                    case TYPE_HEADER_3 -> fontSize = 20f * SCALE;
                    case TYPE_TEXT -> fontSize = 16f * SCALE;
                }
                g.setFont(font.deriveFont(fontSize));
                g.drawString(ci.getText(), ci.getX(), ci.getY());
            }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            goTo(Gamestate.MENU);
    }
}