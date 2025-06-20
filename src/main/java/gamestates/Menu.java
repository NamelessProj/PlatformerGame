package gamestates;

import mainWindow.Game;
import ui.MenuButton;
import utils.Constants;
import utils.LoadSave;

import static utils.Constants.GameConstants.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Menu extends State implements Statemethods {
    private MenuButton[] buttons = new MenuButton[3];
    private BufferedImage backgroundImg, backgroundImgPink;
    private int menuX, menuY, menuWidth, menuHeight;

    public Menu(Game game) {
        super(game);

        loadButtons();
        loadBackground();
        backgroundImgPink = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMAGE);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int) (backgroundImg.getWidth() * SCALE);
        menuHeight = (int) (backgroundImg.getHeight() * SCALE);
        menuX = GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (45 * SCALE);
    }

    private void loadButtons() {
        buttons[0] = new MenuButton(GAME_WIDTH / 2, (int) (150 * SCALE), 0, Gamestate.PLAYING);
        buttons[1] = new MenuButton(GAME_WIDTH / 2, (int) (220 * SCALE), 1, Gamestate.OPTIONS);
        buttons[2] = new MenuButton(GAME_WIDTH / 2, (int) (290 * SCALE), 2, Gamestate.QUIT);
    }

    private void resetButtons() {
        for (MenuButton btn : buttons)
            btn.resetBools();
    }

    @Override
    public void update() {
        for (MenuButton btn : buttons)
            btn.update();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImgPink, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);

        for (MenuButton btn : buttons)
            btn.draw(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton btn : buttons) {
            if (isIn(e, btn)) {
                btn.setMousePressed(true);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton btn : buttons) {
            if (isIn(e, btn)) {
                if (btn.isMousePressed())
                    btn.applyGamestate();

                if (btn.getState() == Gamestate.PLAYING)
                    game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
                break;
            }
        }
        resetButtons();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton btn : buttons)
            btn.setMouseOver(false);

        for (MenuButton btn : buttons)
            if (isIn(e, btn)) {
                btn.setMouseOver(true);
                break;
            }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            setGamestate(Gamestate.PLAYING);
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}