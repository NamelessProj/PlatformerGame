package gamestates;

import mainWindow.Game;
import ui.AudioOptions;
import ui.UrmButton;
import utils.LoadSave;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constants.GameConstants.*;
import static utils.Constants.UI.URMButtons.URM_SIZE;
import static utils.HelpMethods.IsIn;

public class GameOptions extends State implements Statemethods {
    private AudioOptions audioOptions;
    private BufferedImage backgroundImg, optionsBackgroundImg;
    private int bgX, bgY, bgW, bgH;
    private UrmButton menuBtn;

    /**
     * Constructor for GameOptions.
     * @param game the {@link Game} instance
     */
    public GameOptions(Game game) {
        super(game);
        loadImages();
        loadButton();
        audioOptions = game.getAudioOptions();
    }

    /**
     * Loads the button for returning to the main menu.
     */
    private void loadButton() {
        int menuX = (int) (387 * SCALE);
        int menuY = (int) (325 * SCALE);

        menuBtn = new UrmButton(menuX, menuY, URM_SIZE, URM_SIZE, 2);
    }

    /**
     * Loads the background images for the options menu.
     */
    private void loadImages() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.Images.MENU_BACKGROUND_IMAGE);
        optionsBackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.Images.OPTIONS_MENU);

        bgW = (int) (optionsBackgroundImg.getWidth() * SCALE);
        bgH = (int) (optionsBackgroundImg.getHeight() * SCALE);
        bgX = GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (33 * SCALE);
    }

    @Override
    public void update() {
        menuBtn.update();
        audioOptions.update();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        g.drawImage(optionsBackgroundImg, bgX, bgY, bgW, bgH, null);

        menuBtn.draw(g);
        audioOptions.draw(g);
    }

    /**
     * Handles mouse dragging events for audio options.
     * @param e the {@link MouseEvent} triggered by dragging
     */
    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (IsIn(e, menuBtn))
            menuBtn.setMousePressed(true);
        else
            audioOptions.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (IsIn(e, menuBtn)) {
            if (menuBtn.isMousePressed())
                Gamestate.state = Gamestate.MENU;
        } else
            audioOptions.mouseReleased(e);

        menuBtn.resetBools();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        menuBtn.setMouseOver(false);

        if (IsIn(e, menuBtn))
            menuBtn.setMouseOver(true);
        else
            audioOptions.mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            Gamestate.state = Gamestate.MENU;
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}
}