package objects;

public class Grass {
    private int x, y, type;

    /**
     * Constructor for the Grass class.
     * @param x the x-coordinate of the grass
     * @param y the y-coordinate of the grass
     * @param type the type of the grass
     */
    public Grass(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    /**
     * Returns the x-coordinate of the grass.
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the grass.
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the type of the grass.
     * @return the type of the grass
     */
    public int getType() {
        return type;
    }
}