package gamesaves;

import static utils.Constants.Saves.*;

import java.util.ArrayList;

import entities.Enemy;
import entities.Player;
import gamestates.Playing;
import utils.LoadSave;

public class GameSaves {
    private Playing playing;

    /**
     * Constructor for the GameSaves class.
     * @param playing the {@link Playing} instance.
     */
    public GameSaves(Playing playing) {
        this.playing = playing;
    }

    /**
     * Saves the current game state to a file.
     */
    public void saveGame() {
        StringBuilder saveData = new StringBuilder();
        ArrayList<Enemy> allEnemies = playing.getEnemyManager().getAllEnemies();
        Player player = playing.getPlayer();
        int currentLevelIndex = playing.getLevelManager().getLevelIndex();

        // Save current level index
        saveData.append(LEVEL_DECLARATION).append(Integer.toString(currentLevelIndex)).append("\n");

        // Save player data
        saveData.append(PLAYER_DECLARATION).append(player.toString()).append("\n");

        // Save enemies data
        int currentEnemyType = allEnemies.get(0).getEnemyType();
        saveData.append(ENEMIES_DECLARATION).append(currentEnemyType).append(DATA_EQUAL);

        for (Enemy enemy : allEnemies) {
            if (currentEnemyType != enemy.getEnemyType()) {
                currentEnemyType = enemy.getEnemyType();
                saveData.append(TYPE_SEPARATOR).append(currentEnemyType).append(DATA_EQUAL);
            }

            saveData.append(enemy.toString()).append(ENTITY_SEPARATOR);
        }

        // Remove the last ENTITY_SEPARATOR
        if (saveData.length() > 0 && saveData.charAt(saveData.length() - 1) == ENTITY_SEPARATOR.charAt(0))
            saveData.deleteCharAt(saveData.length() - 1);

        // Save the data to a file
        LoadSave.SaveText(LoadSave.Texts.SAVE, saveData.toString());
    }

    /**
     * Checks if a saved game is available.
     * @return {@code true} if a saved game is available, {@code false} otherwise.
     */
    public boolean isSaveAvailable() {
        return LoadSave.DoesFileExists(LoadSave.Texts.SAVE);
    }

    /**
     * Load the saved game state from a file.
     * </p>
     * This method initializes the enemies and objects in the current level based on the saved data.
     */
    public void loadGame() {
        System.out.println("Loading game...");
        playing.getEnemyManager().loadEnemies(playing.getLevelManager().getCurrentLevel());
        playing.getObjectManager().loadObjects(playing.getLevelManager().getCurrentLevel());
    }
}