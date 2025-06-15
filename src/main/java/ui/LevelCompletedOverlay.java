package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import utils.LoadSave;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constants.GameConstants.*;
import static utils.Constants.UI.URMButtons.*;

public class LevelCompletedOverlay {
    private Playing playing;
    private UrmButton menu, next;
    private BufferedImage img;
    private int bxX, bgY, bgW, bgH;

    public LevelCompletedOverlay(Playing playing) {
        this.playing = playing;
        initImage();
        initButtons();
    }

    private void initButtons() {
        int menuX = (int) (330 * SCALE);
        int nextX = (int) (445 * SCALE);
        int y = (int) (195 * SCALE);
        next = new UrmButton(nextX, y, URM_SIZE, URM_SIZE, 0);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    private void initImage() {
        img = LoadSave.GetSpriteAtlas(LoadSave.COMPLETED_IMG);
        bgW = (int) (img.getWidth() * SCALE);
        bgH = (int) (img.getHeight() * SCALE);
        bxX = GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (75 * SCALE);
    }

    public void update() {
        next.update();
        menu.update();
    }

    public void draw(Graphics g) {
        g.drawImage(img, bxX, bgY, bgW, bgH, null); // Draw background image
        next.draw(g);
        menu.draw(g);
    }

    private boolean isIn(UrmButton b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        next.setMouseOver(false);
        menu.setMouseOver(false);

        if (isIn(menu, e))
            menu.setMouseOver(true);
        else if (isIn(next, e))
            next.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(menu, e) && menu.isMousePressed()) {
            playing.resetAll();
            Gamestate.state = Gamestate.MENU;
        } else if (isIn(next, e) && next.isMousePressed())
            playing.loadNextLevel();

        next.resetBools();
        menu.resetBools();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(menu, e))
            menu.setMousePressed(true);
        else if (isIn(next, e))
            next.setMousePressed(true);
    }
}