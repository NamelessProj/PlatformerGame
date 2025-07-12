package objects;

import entities.Enemy;
import entities.Player;
import gamestates.Playing;
import levels.Level;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.Directions.*;
import static utils.Constants.GameConstants.*;
import static utils.Constants.ObjectConstants.*;
import static utils.Constants.Projectiles.*;
import static utils.HelpMethods.CanCannonSeePlayer;
import static utils.HelpMethods.IsProjectileHittingLevel;

public class ObjectManager {
    private Playing playing;
    private BufferedImage spikeImage, cannonBallImage;
    private BufferedImage[] cannonImages, grassImages;
    private BufferedImage[][] potionImages, containerImages;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Projectile> projectiles = new ArrayList<>();

    private Level currentLevel;

    /**
     * Constructor for the ObjectManager class.
     * @param playing the Playing instance that manages the game state
     */
    public ObjectManager(Playing playing) {
        this.playing = playing;
        this.currentLevel = playing.getLevelManager().getCurrentLevel();
        loadImages();
    }

    /**
     * Checks if the player has touched any spikes in the current level.
     * @param p the Player instance to check for collisions with spikes
     */
    public void checkSpikesTouchedPlayer(Player p) {
        for (Spike s : currentLevel.getSpikes())
            if (s.getHitbox().intersects(p.getHitbox())) {
                p.kill();
                return;
            }
    }

    /**
     * Checks if an enemy has touched any spikes in the current level.
     * @param e the Enemy instance to check for collisions with spikes
     */
    public void checkSpikesTouchedEnemy(Enemy e) {
        for (Spike s : currentLevel.getSpikes())
            if (s.getHitbox().intersects(e.getHitbox()))
                e.hurt(200);
    }

    /**
     * Checks if the player has touched any potions in the current level.
     * @param hitbox the hitbox of the player to check for collisions with potions
     */
    public void checkObjectTouchedPlayer(Rectangle2D.Float hitbox) {
        for (Potion p : potions)
            if (p.isActive() && hitbox.intersects(p.getHitbox())) {
                p.setActive(false);
                applyEffectToPlayer(p);
            }
    }

    /**
     * Applies the effect of a potion to the player based on its type.
     * @param potion the Potion instance to apply the effect from
     */
    public void applyEffectToPlayer(Potion potion) {
        if (potion.getObjectType() == RED_POTION)
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        else
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
    }

    /**
     * Checks if any game containers (like barrels or boxes) are hit by an attack box.
     * @param attackBox the Rectangle2D.Float representing the attack area
     */
    public void checkObjectHit(Rectangle2D.Float attackBox) {
        for (GameContainer gc : containers)
            if (gc.isActive() && !gc.doAnimation && gc.getHitbox().intersects(attackBox)) {
                gc.setDoAnimation(true);
                int type = RED_POTION;
                if (gc.getObjectType() == BARREL)
                    type = BLUE_POTION;
                potions.add(new Potion((int) (gc.getHitbox().x + gc.getHitbox().width / 2), (int) (gc.getHitbox().y - gc.getHitbox().height / 4), type));
                return;
            }
    }

    /**
     * Loads the objects for the current level, including potions, containers, and projectiles.
     * @param newLevel the Level instance to load objects from
     */
    public void loadObjects(Level newLevel) {
        this.currentLevel = newLevel;
        potions = new ArrayList<>(newLevel.getPotions());
        containers = new ArrayList<>(newLevel.getContainers());
        projectiles.clear();
    }

    /**
     * Loads the images for various game objects such as potions, containers, traps, and cannons.
     */
    private void loadImages() {
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.Sprites.POTION_ATLAS);
        potionImages = new BufferedImage[2][7];
        int potionWidth = 12;
        int potionHeight = 16;

        for (int j = 0; j < potionImages.length; j++)
            for (int i = 0; i < potionImages[j].length; i++)
                potionImages[j][i] = potionSprite.getSubimage(i * potionWidth, j * potionHeight, potionWidth, potionHeight);

        BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.Sprites.CONTAINER_ATLAS);
        containerImages = new BufferedImage[2][8];
        int containerWidth = 40;
        int containerHeight = 30;

        for (int j = 0; j < containerImages.length; j++)
            for (int i = 0; i < containerImages[j].length; i++)
                containerImages[j][i] = containerSprite.getSubimage(i * containerWidth, j * containerHeight, containerWidth, containerHeight);

        spikeImage = LoadSave.GetSpriteAtlas(LoadSave.Sprites.TRAP_ATLAS);

        cannonImages = new BufferedImage[GetSpriteAmount(CANNON_RIGHT)];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.Sprites.CANNON_ATLAS);
        for (int i = 0; i < cannonImages.length; i++)
            cannonImages[i] = temp.getSubimage(i * CANNON_WIDTH_DEFAULT, 0, CANNON_WIDTH_DEFAULT, CANNON_HEIGHT_DEFAULT);

        cannonBallImage = LoadSave.GetSpriteAtlas(LoadSave.Sprites.CANNON_BALL);

        BufferedImage grassTemp = LoadSave.GetSpriteAtlas(LoadSave.Sprites.GRASS_ATLAS);
        grassImages = new BufferedImage[2];
        for (int i = 0; i < grassImages.length; i++)
            grassImages[i] = grassTemp.getSubimage(i * 32, 0, 32, 32);
    }

    /**
     * Updates the state of all game objects, including potions, containers, cannons, and projectiles.
     * @param lvlData the level data array representing the current level
     * @param player the Player instance to check for interactions with objects
     */
    public void update(int[][] lvlData, Player player) {
        for (Potion p : potions)
            if (p.isActive())
                p.update();

        for (GameContainer gc : containers)
            if (gc.isActive())
                gc.update();

        updateCannons(lvlData, player);
        updateProjectiles(lvlData, player);
    }

    /**
     * Updates the position and state of all projectiles in the game.
     * @param lvlData the level data array representing the current level
     * @param player the Player instance to check for collisions with projectiles
     */
    private void updateProjectiles(int[][] lvlData, Player player) {
        for (Projectile p : projectiles)
            if (p.isActive()) {
                p.updatePosition();
                if (p.getHitbox().intersects(player.getHitbox())) {
                    player.changeHealth(-25);
                    player.setPushBackDirection(p.getDirection() == 1 ? RIGHT : LEFT);
                    p.setActive(false);
                } else if (IsProjectileHittingLevel(p, lvlData))
                    p.setActive(false);
            }
    }

    /**
     * Updates the state of all cannons in the current level, checking if they should fire based on the player's position.
     * @param lvlData the level data array representing the current level
     * @param player the Player instance to check for interactions with cannons
     */
    private void updateCannons(int[][] lvlData, Player player) {
        for (Cannon c : currentLevel.getCannons()) {
            if (!c.doAnimation && c.getTileY() == player.getTileY())
                if (isPlayerInRange(c, player))
                    if (isPlayerInFrontOfCannon(c, player))
                        if (CanCannonSeePlayer(lvlData, player.getHitbox(), c.getHitbox(), c.getTileY())) {
                            c.setDoAnimation(true);
                        }

            c.update();
            if (c.getAnimationIndex() == 4 && c.getAnimationTick() == 0)
                shootCannon(c);
        }
    }

    /**
     * Shoots a cannon by creating a new Projectile instance at the cannon's position.
     * @param c the Cannon instance that is firing
     */
    private void shootCannon(Cannon c) {
        int dir = c.getObjectType() == CANNON_LEFT ? -1 : 1;
        projectiles.add(new Projectile((int) c.getHitbox().x, (int) c.getHitbox().y, dir));
    }

    /**
     * Checks if the player is in front of the cannon based on its type and the player's position.
     * @param c the Cannon instance to check
     * @param p the Player instance to check against the cannon
     * @return true if the player is in front of the cannon, false otherwise
     */
    private boolean isPlayerInFrontOfCannon(Cannon c, Player p) {
        if (c.getObjectType() == CANNON_LEFT) {
            return c.getHitbox().x > p.getHitbox().x;
        } else return c.getHitbox().x < p.getHitbox().x;
    }

    /**
     * Checks if the player is within a certain range of the cannon.
     * @param c the Cannon instance to check
     * @param p the Player instance to check against the cannon
     * @return true if the player is within range, false otherwise
     */
    private boolean isPlayerInRange(Cannon c, Player p) {
        int absValue = (int) (Math.abs(p.getHitbox().x - c.getHitbox().x));
        return absValue <= TILES_SIZE * 5;
    }

    /**
     * Draws all game objects on the screen, including potions, containers, traps, cannons, projectiles, and grass.
     * @param g the Graphics object used for drawing
     * @param xLvlOffset the x-level offset for drawing objects
     */
    public void draw(Graphics g, int xLvlOffset) {
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
        drawTraps(g, xLvlOffset);
        drawCannons(g, xLvlOffset);
        drawProjectiles(g, xLvlOffset);
        drawGrass(g, xLvlOffset);
    }

    /**
     * Draws the grass in the current level using the grass images.
     * @param g the Graphics object used for drawing
     * @param xLvlOffset the x-level offset for drawing grass
     */
    private void drawGrass(Graphics g, int xLvlOffset) {
        for (Grass grass : currentLevel.getGrass())
            g.drawImage(grassImages[grass.getType()],
                    grass.getX() - xLvlOffset,
                    grass.getY(),
                    (int) (32 * SCALE),
                    (int) (32 * SCALE),
                    null);
    }

    /**
     * Draws the projectiles on the screen, checking if they are active before drawing.
     * @param g the Graphics object used for drawing
     * @param xLvlOffset the x-level offset for drawing projectiles
     */
    private void drawProjectiles(Graphics g, int xLvlOffset) {
        for (Projectile p : projectiles)
            if (p.isActive())
                g.drawImage(cannonBallImage,
                        (int) (p.getHitbox().x - xLvlOffset),
                        (int) (p.getHitbox().y),
                        CANNON_BALL_WIDTH,
                        CANNON_BALL_HEIGHT,
                        null);
    }

    /**
     * Draws the cannons in the current level, adjusting their position based on the x-level offset.
     * @param g the Graphics object used for drawing
     * @param xLvlOffset the x-level offset for drawing cannons
     */
    private void drawCannons(Graphics g, int xLvlOffset) {
        for (Cannon c : currentLevel.getCannons()) {
            int x = (int) (c.getHitbox().x - xLvlOffset);
            int width = CANNON_WIDTH;

            if (c.getObjectType() == CANNON_RIGHT) {
                x += width;
                width *= -1;
            }

            g.drawImage(cannonImages[c.getAnimationIndex()],
                    x,
                    (int) (c.getHitbox().y),
                    width,
                    CANNON_HEIGHT,
                    null);
        }
    }

    /**
     * Draws the traps (spikes) in the current level, adjusting their position based on the x-level offset.
     * @param g the Graphics object used for drawing
     * @param xLvlOffset the x-level offset for drawing traps
     */
    private void drawTraps(Graphics g, int xLvlOffset) {
        for (Spike s : currentLevel.getSpikes())
            g.drawImage(spikeImage,
                    (int) (s.getHitbox().x - xLvlOffset),
                    (int) (s.getHitbox().y - s.getYDrawOffset()),
                    SPIKE_WIDTH,
                    SPIKE_HEIGHT,
                    null);
    }

    /**
     * Draws the containers (like barrels and boxes) in the current level, adjusting their position based on the x-level offset.
     * @param g the Graphics object used for drawing
     * @param xLvlOffset the x-level offset for drawing containers
     */
    private void drawContainers(Graphics g, int xLvlOffset) {
        for (GameContainer gc : containers)
            if (gc.isActive()) {
                int rowIndex = BOX_ROW_INDEX;
                if (gc.getObjectType() == BARREL)
                    rowIndex = BARREL_ROW_INDEX;

                g.drawImage(containerImages[rowIndex][gc.getAnimationIndex()],
                        (int) gc.getHitbox().x - gc.getXDrawOffset() - xLvlOffset,
                        (int) gc.getHitbox().y - gc.getYDrawOffset(),
                        CONTAINER_WIDTH,
                        CONTAINER_HEIGHT,
                        null);
            }
    }

    /**
     * Draws the potions in the current level, adjusting their position based on the x-level offset.
     * @param g the Graphics object used for drawing
     * @param xLvlOffset the x-level offset for drawing potions
     */
    private void drawPotions(Graphics g, int xLvlOffset) {
        for (Potion p : potions)
            if (p.isActive()) {
                int rowIndex = RED_POTION_ROW_INDEX;
                if (p.getObjectType() == BLUE_POTION)
                    rowIndex = BLUE_POTION_ROW_INDEX;

                g.drawImage(potionImages[rowIndex][p.getAnimationIndex()],
                        (int) p.getHitbox().x - p.getXDrawOffset() - xLvlOffset,
                        (int) p.getHitbox().y - p.getYDrawOffset(),
                        POTION_WIDTH,
                        POTION_HEIGHT,
                        null);
            }
    }

    /**
     * Resets all game objects to their initial state, including potions, containers, and cannons.
     */
    public void resetAllObjects() {
        loadObjects(playing.getLevelManager().getCurrentLevel());

        for (Potion p : potions)
            p.reset();

        for (GameContainer gc : containers)
            gc.reset();

        for (Cannon c : currentLevel.getCannons())
            c.reset();
    }
}