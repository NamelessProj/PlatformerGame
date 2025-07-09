package gamestates;

import static utils.Constants.GameConstants.*;

import java.awt.Color;
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

    private BufferedImage backgroundImg;
    private ArrayList<CreditsItem> creditsItems = new ArrayList<>();



    private class CreditsItem {
        private int type, x, y, yPosition;
        private String text;

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
         * Updates the position of the credits item, moving it upwards.
         */
        public void update() {
            yPosition -= 1;
        }

        /**
         * Resets the y-coordinate of the credits item to its initial position.
         */
        public void reset() {
            yPosition = y;
        }
    }



    /**
     * Constructor for the Credits class.
     * @param game the Game instance that this Credits state belongs to.
     */
    public Credits(Game game) {
        super(game);
        loadBackgroundImage();
        loadCredits();
    }

    /**
     * Loads the credits from a text file.
     */
    private void loadCredits() {
        BufferedReader reader = LoadSave.GetText(LoadSave.CREDITS);

        try {
            int startMargin = 200;
            int margin = 100;
            String line = reader.readLine();
            int i = 0;
            while (line != null) {
                if (!line.isEmpty()) {
                    int y = i * margin + GAME_HEIGHT + startMargin;
                    if (line.startsWith(HEADER_3))
                        creditsItems.add(new CreditsItem(100, y, TYPE_HEADER_3, line.substring(HEADER_3.length()).trim()));
                    else if (line.startsWith(HEADER_2))
                        creditsItems.add(new CreditsItem(100, y, TYPE_HEADER_2, line.substring(HEADER_2.length()).trim()));
                    else if (line.startsWith(HEADER_1))
                        creditsItems.add(new CreditsItem(100, y, TYPE_HEADER_1, line.substring(HEADER_1.length()).trim()));
                    else
                        creditsItems.add(new CreditsItem(100, y, TYPE_TEXT, line.trim()));

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
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMAGE);
    }

    /**
     * Updates the position of each credits item, moving them upwards.
     */
    private void updateCreditsItems() {
        for (CreditsItem ci : creditsItems)
            ci.update();

        if (creditsItems.getLast().getY() < -100)
            goTo(Gamestate.MENU);
    }

    /**
     * Changes the game state to the specified Gamestate and resets all credits items.
     * @param state the Gamestate to switch to.
     */
    private void goTo(Gamestate state) {
        game.getPlaying().setGamestate(state);
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
        for (CreditsItem ci : creditsItems) {
            switch (ci.getType()) {
                case TYPE_HEADER_1 -> g.setFont(g.getFont().deriveFont(24f * SCALE));
                case TYPE_HEADER_2 -> g.setFont(g.getFont().deriveFont(20f * SCALE));
                case TYPE_HEADER_3 -> g.setFont(g.getFont().deriveFont(16f * SCALE));
                case TYPE_TEXT -> g.setFont(g.getFont().deriveFont(12f * SCALE));
            }
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