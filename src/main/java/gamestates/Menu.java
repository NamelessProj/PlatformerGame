package gamestates;

import mainWindow.Game;
import ui.GitHubButton;
import ui.MenuButton;
import utils.LoadSave;

import static utils.Constants.GameConstants.*;
import static utils.Constants.UI.PauseButtons.SOUND_SIZE_DEFAULT;
import static utils.HelpMethods.IsIn;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URI;

public class Menu extends State implements Statemethods {
    private MenuButton[] buttons = new MenuButton[4];
    private GitHubButton githubBtn;
    private BufferedImage backgroundImg, backgroundImgPink;
    private int menuX, menuY, menuWidth, menuHeight;

    /**
     * Constructor for the Menu class.
     * @param game the {@link Game} instance
     */
    public Menu(Game game) {
        super(game);

        loadButtons();
        loadBackground();
        backgroundImgPink = LoadSave.GetSpriteAtlas(LoadSave.Images.MENU_BACKGROUND_IMAGE);
    }

    /**
     * Loads the background image for the menu.
     */
    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.Images.MENU_BACKGROUND);
        menuWidth = (int) (backgroundImg.getWidth() * SCALE);
        menuHeight = (int) (backgroundImg.getHeight() * SCALE);
        menuX = GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (25 * SCALE);
    }

    /**
     * Loads the buttons for the menu.
     */
    private void loadButtons() {
        int menuX = GAME_WIDTH / 2;
        buttons[0] = new MenuButton(menuX, (int) (130 * SCALE), 0, Gamestate.PLAYING);
        buttons[1] = new MenuButton(menuX, (int) (200 * SCALE), 1, Gamestate.OPTIONS);
        buttons[2] = new MenuButton(menuX, (int) (270 * SCALE), 3, Gamestate.CREDITS);
        buttons[3] = new MenuButton(menuX, (int) (340 * SCALE), 2, Gamestate.QUIT);

        int githubX = (int) (GAME_WIDTH - 50 * SCALE);
        int githubY = (int) (GAME_HEIGHT - 50 * SCALE);
        githubBtn = new GitHubButton(githubX, githubY, SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT);
    }

    /**
     * Resets the mouse pressed state of all buttons.
     */
    private void resetButtons() {
        for (MenuButton btn : buttons)
            btn.resetBools();

        githubBtn.resetBools();
    }

    @Override
    public void update() {
        for (MenuButton btn : buttons)
            btn.update();

        githubBtn.update();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImgPink, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);

        for (MenuButton btn : buttons)
            btn.draw(g);

        githubBtn.draw(g);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton btn : buttons) {
            if (IsIn(e, btn)) {
                btn.setMousePressed(true);
                break;
            }
        }

        if (IsIn(e, githubBtn))
            githubBtn.setMousePressed(true);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton btn : buttons) {
            if (IsIn(e, btn)) {
                if (btn.isMousePressed())
                    btn.applyGamestate();

                if (btn.getState() == Gamestate.PLAYING)
                    game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
                break;
            }
        }
        if (IsIn(e, githubBtn)) {
            if (githubBtn.isMousePressed()) {
                githubBtn.setMousePressed(false);
                // Open GitHub link or perform action
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/NamelessProj/PlatformerGame"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        resetButtons();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton btn : buttons)
            btn.setMouseOver(false);

        for (MenuButton btn : buttons)
            if (IsIn(e, btn)) {
                btn.setMouseOver(true);
                break;
            }

        githubBtn.setMouseOver(false);
        if (IsIn(e, githubBtn))
            githubBtn.setMouseOver(true);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            setGamestate(Gamestate.PLAYING);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}