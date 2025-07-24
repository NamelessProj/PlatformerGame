package gamesaves;

import static utils.Constants.EnemyConstants.CRABBY;
import static utils.Constants.EnemyConstants.PINKSTAR;
import static utils.Constants.EnemyConstants.SHARK;
import static utils.Constants.Saves.*;
import static utils.HelpMethods.IsInt;
import static utils.HelpMethods.IsFloat;

import java.io.BufferedReader;
import java.util.ArrayList;

import entities.Crabby;
import entities.Enemy;
import entities.Pinkstar;
import entities.Player;
import entities.Shark;
import gamestates.Playing;
import mainWindow.Game;
import utils.LoadSave;

public class GameSaves {
    private Game game;

    /**
     * Constructor for the GameSaves class.
     * @param game the {@link Game} instance.
     */
    public GameSaves(Game game) {
        this.game = game;
    }

    /**
     * Saves the current game state to a file.
     */
    public void saveGame() {
        StringBuilder saveData = new StringBuilder();
        ArrayList<Enemy> allEnemies = game.getPlaying().getEnemyManager().getAllEnemies();
        Player player = game.getPlaying().getPlayer();
        int currentLevelIndex = game.getPlaying().getLevelManager().getLevelIndex();

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

        ArrayList<Crabby> crabbies = new ArrayList<>();
        ArrayList<Pinkstar> pinkstars = new ArrayList<>();
        ArrayList<Shark> sharks = new ArrayList<>();

        boolean levelUpdated = false, playerUpdated = false, enemiesUpdated = false;

        BufferedReader reader = LoadSave.GetText(LoadSave.Texts.SAVE);

        if (reader == null) {
            System.out.println("No saved game found.");
            return;
        }

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                if (line.isBlank() || !line.contains(DATA_EQUAL))
                    continue;

                String[] parts = line.split(DATA_EQUAL, 2);
                String key = parts[0].trim().toLowerCase();
                String value = parts[1].trim();

                switch (key) {
                    case LEVEL -> {
                        if (!levelUpdated && IsInt(value)) {
                            levelUpdated = true;
                            int levelIndex = Integer.parseInt(value);
                            game.getPlaying().getLevelManager().setLevelIndex(levelIndex);
                        }
                    }
                    case PLAYER -> {
                        if (playerUpdated)
                            continue;

                        playerUpdated = true;
                        String[] playerData = value.split(DATA_SEPARATOR);

                        if (playerData.length >= 6) {
                            for (int i = 0; i < 2; i++)
                                if (!IsFloat(playerData[i]))
                                    return;

                            for (int i = 2; i < 6; i++)
                                if (!IsInt(playerData[i]))
                                    return;

                            float playerX = Float.parseFloat(playerData[0]);
                            float playerY = Float.parseFloat(playerData[1]);
                            int playerLeft = Integer.parseInt(playerData[2]);
                            int playerRight = Integer.parseInt(playerData[3]);
                            int playerHealth = Integer.parseInt(playerData[4]);
                            int playerPower = Integer.parseInt(playerData[5]);

                            game.getPlaying().getPlayer().setPlayer(playerX, playerY, playerLeft, playerRight, playerHealth, playerPower);
                        }
                    }
                    case ENEMIES -> {
                        if (enemiesUpdated)
                            continue;

                        enemiesUpdated = true;
                        String[] enemiesType = value.split(TYPE_SEPARATOR);

                        for (String block : enemiesType) {
                            String[] enemyParts = block.split(DATA_EQUAL, 2);

                            if (!IsInt(enemyParts[0]))
                                continue;

                            int enemyType = Integer.parseInt(enemyParts[0]);
                            String[] enemyData = enemyParts[1].split(ENTITY_SEPARATOR);

                            for (String entity : enemyData) {
                                float x = 0, y = 0;
                                int walkDir = 0, currentHealth = 0;
                                boolean active = true;

                                String[] entityParts = entity.split(DATA_SEPARATOR);

                                if (entityParts.length < 5)
                                    continue;

                                for (int i = 0; i < 2; i++)
                                    if (!IsFloat(entityParts[i]))
                                        return;

                                for (int i = 2; i < entityParts.length; i++)
                                    if (!IsInt(entityParts[i]))
                                        return;

                                x = Float.parseFloat(entityParts[0]);
                                y = Float.parseFloat(entityParts[1]);
                                walkDir = Integer.parseInt(entityParts[2]);
                                currentHealth = Integer.parseInt(entityParts[3]);
                                if (entityParts[4].equals("0"))
                                    active = false;

                                switch (enemyType) {
                                    case CRABBY -> crabbies.add(new Crabby(x, y, walkDir, currentHealth, active));
                                    case PINKSTAR -> pinkstars.add(new Pinkstar(x, y, walkDir, currentHealth, active));
                                    case SHARK -> sharks.add(new Shark(x, y, walkDir, currentHealth, active));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!levelUpdated)
            game.getPlaying().getLevelManager().setLevelIndex(0);

        System.out.println("Level index: " + game.getPlaying().getLevelManager().getLevelIndex());

        if (!playerUpdated) {
            System.out.println("Player data not found, using default values.");
            game.getPlaying().getPlayer().setSpawn(game.getPlaying().getLevelManager().getCurrentLevel().getPlayerSpawn());
        }
            

        System.out.println("Player x: " + game.getPlaying().getPlayer().getHitbox().x);
        System.out.println("Player y: " + game.getPlaying().getPlayer().getHitbox().y);

        game.getPlaying().getEnemyManager().loadEnemies(game.getPlaying().getLevelManager().getCurrentLevel());

        if (enemiesUpdated) {
            // Set the saved enemy data
            game.getPlaying().getEnemyManager().setCrabbies(crabbies);
            game.getPlaying().getEnemyManager().setPinkstars(pinkstars);
            game.getPlaying().getEnemyManager().setSharks(sharks);
        }

        // Always load objects from the current level
        game.getPlaying().getObjectManager().loadObjects(game.getPlaying().getLevelManager().getCurrentLevel());
    }
}