package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import utils.LoadSave;

import static utils.Constants.GameConstants.*;

public class GameCompletedOverlay {
    private Playing playing;
    private BufferedImage img;
    private MenuButton quitBtn;
    private int imgX, imgY, imgW, imgH;

    public GameCompletedOverlay(Playing playing) {
        this.playing = playing;
        createImage();
        createButtons();
    }

    private void createButtons() {
        quitBtn = new MenuButton(GAME_WIDTH / 2, (int) (270 * SCALE), 2, Gamestate.MENU);
    }

    private void createImage() {
        img = LoadSave.GetSpriteAtlas(LoadSave.GAME_COMPLETED);
        imgW = (int) (img.getWidth() * SCALE);
        imgH = (int) (img.getHeight() * SCALE);
        imgX = (int) GAME_WIDTH / 2 - imgW / 2;
        imgY = (int) (100 * SCALE);
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        g.drawImage(img, imgX, imgY, imgW, imgH, null);

        quitBtn.draw(g);
    }

    public void update() {
        quitBtn.update();
    }

    private boolean isIn(MenuButton b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        quitBtn.setMouseOver(false);

        if (isIn(quitBtn, e))
            quitBtn.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(quitBtn, e))
            if (quitBtn.isMousePressed()) {
                playing.resetAll();
                playing.resetGameCompleted();
                playing.setGamestate(Gamestate.MENU);
            }

        quitBtn.resetBools();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(quitBtn, e))
            quitBtn.setMousePressed(true);
    }
}