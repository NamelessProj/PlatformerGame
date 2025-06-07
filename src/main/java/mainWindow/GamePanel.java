package mainWindow;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.swing.JPanel;
import java.awt.Graphics;

public class GamePanel extends JPanel {
    private MouseInputs mouseInputs;
    private int xDelta = 100, yDelta = 100;

    public GamePanel() {
        mouseInputs = new MouseInputs(this);
        this.addKeyListener(new KeyboardInputs(this));
        this.addMouseListener(mouseInputs);
        this.addMouseMotionListener(mouseInputs);
    }

    public void changeXDelta(int val) {
        this.xDelta += val;
        this.repaint();
    }

    public void changeYDelta(int val) {
        this.yDelta += val;
        this.repaint();
    }

    public void setRectPos(int x, int y) {
        this.xDelta = x;
        this.yDelta = y;
        this.repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.fillRect(xDelta, yDelta, 200, 50); // Example drawing a rectangle
    }
}