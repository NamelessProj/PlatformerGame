package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface Statemethods {
    /**
     * Updates the state of the game.
     */
    void update();

    /**
     * Draws the current state to the graphics context.
     * @param g the {@link Graphics} context to draw on
     */
    void draw(Graphics g);

    /**
     * Handles mouse click events.
     * @param e the {@link MouseEvent} instance
     */
    void mouseClicked(MouseEvent e);

    /**
     * Handles mouse press events.
     * @param e the {@link MouseEvent} instance
     */
    void mousePressed(MouseEvent e);

    /**
     * Handles mouse release events.
     * @param e the {@link MouseEvent} instance
     */
    void mouseReleased(MouseEvent e);

    /**
     * Handles mouse movement events.
     * @param e the {@link MouseEvent} instance
     */
    void mouseMoved(MouseEvent e);

    /**
     * Handles key press events.
     * @param e the {@link KeyEvent} instance
     */
    void keyPressed(KeyEvent e);

    /**
     * Handles key release events.
     * @param e the {@link KeyEvent} instance
     */
    void keyReleased(KeyEvent e);
}