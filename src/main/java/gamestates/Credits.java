package gamestates;

import static utils.Constants.GameConstants.*;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import mainWindow.Game;
import utils.LoadSave;

public class Credits extends State implements Statemethods {
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgW, bgH;
    private float bgYFloat;

    public Credits(Game game) {
        super(game);
        loadBackgroundImage();
    }

    private void loadBackgroundImage() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        bgW = (int) (backgroundImg.getWidth() * SCALE);
        bgH = (int) (backgroundImg.getHeight() * SCALE);
        bgX = GAME_WIDTH / 2 - bgW / 2;
        bgY = GAME_HEIGHT;
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void draw(Graphics g) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'draw'");
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
            bgYFloat = 0;
            setGamestate(Gamestate.MENU);
        }
    }
}