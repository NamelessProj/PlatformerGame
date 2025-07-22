package mainWindow;

import static utils.Constants.GameConstants.GAME_HEIGHT;
import static utils.Constants.GameConstants.GAME_WIDTH;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel {
    private MouseInputs mouseInputs;
    private Game game;

    /**
     * Constructor for the GamePanel class.
     * @param game the {@link Game} instance that this GamePanel belongs to
     */
    public GamePanel(Game game) {
        this.game = game;
        mouseInputs = new MouseInputs(this);
        setPanelSize();

        this.addKeyListener(new KeyboardInputs(this));
        this.addMouseListener(mouseInputs);
        this.addMouseMotionListener(mouseInputs);
    }

    /**
     * Sets the size of the game panel.
     */
    private void setPanelSize() {
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        this.setMinimumSize(size);
        this.setPreferredSize(size);
        this.setMaximumSize(size);
    }

    /**
     * Updates the game state.
     */
    public void updateGame() {}

    /**
     * Renders the game graphics.
     * @param g the {@link Graphics} object used for rendering
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);
    }

    /**
     * Returns the Game instance associated with this GamePanel.
     * @return the {@link Game} instance
     */
    public Game getGame() {
        return game;
    }
}