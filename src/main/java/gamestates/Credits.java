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
    private final String HEADER_1 = "# ";
    private final String HEADER_2 = "## ";
    private final String HEADER_3 = "### ";

    private final int TYPE_TEXT = 0;
    private final int TYPE_HEADER_1 = 1;
    private final int TYPE_HEADER_2 = 2;
    private final int TYPE_HEADER_3 = 3;

    private BufferedImage backgroundImg;
    private ArrayList<CreditsItem> creditsItems = new ArrayList<>();



    private class CreditsItem {
        private int type, x, y;
        private String text;

        public CreditsItem(int x, int y, int type, String text) {
            this.x = x;
            this.y = y;
            this.type = type;
            this.text = text;
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
            String line = reader.readLine().trim();
            int i = 0;
            while (line != null) {
                if (!line.isEmpty()) {
                    int y = i * 100 + GAME_HEIGHT;
                    if (line.startsWith(HEADER_3))
                        creditsItems.add(new CreditsItem(100, y, TYPE_HEADER_3, line.substring(HEADER_3.length())));
                    else if (line.startsWith(HEADER_2))
                        creditsItems.add(new CreditsItem(100, y, TYPE_HEADER_2, line.substring(HEADER_2.length())));
                    else if (line.startsWith(HEADER_1))
                        creditsItems.add(new CreditsItem(100, y, TYPE_HEADER_1, line.substring(HEADER_1.length())));
                    else
                        creditsItems.add(new CreditsItem(100, y, TYPE_TEXT, line));

                    i++;
                }

                line = reader.readLine().trim();
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
            ci.y -= 1;
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
            switch (ci.type) {
                case TYPE_HEADER_1 -> g.setFont(g.getFont().deriveFont(24f * SCALE));
                case TYPE_HEADER_2 -> g.setFont(g.getFont().deriveFont(20f * SCALE));
                case TYPE_HEADER_3 -> g.setFont(g.getFont().deriveFont(16f * SCALE));
                case TYPE_TEXT -> g.setFont(g.getFont().deriveFont(12f * SCALE));
            }
            g.drawString(ci.text, ci.x, ci.y);
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
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            game.getPlaying().setGamestate(Gamestate.MENU);
            creditsItems.clear();
            loadCredits();
        }
    }
}