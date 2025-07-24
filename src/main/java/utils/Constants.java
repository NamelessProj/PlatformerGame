package utils;

public class Constants {
    public static class Saves {
        public static final String DATA_EQUAL = "=";
        public static final String DATA_SEPARATOR = ";";
        public static final String TYPE_SEPARATOR = "_";
        public static final String ENTITY_SEPARATOR = "-";
        public static final String LEVEL = "level";
        public static final String PLAYER = "player";
        public static final String ENEMIES = "enemies";
        public static final String LEVEL_DECLARATION = LEVEL + DATA_EQUAL;
        public static final String PLAYER_DECLARATION = PLAYER + DATA_EQUAL;
        public static final String ENEMIES_DECLARATION = ENEMIES + DATA_EQUAL;
    }

    public static class Dialogue {
        public static final int QUESTION = 0;
        public static final int EXCLAMATION = 1;

        public static int DIALOGUE_WIDTH = 14;
        public static int DIALOGUE_HEIGHT = 12;

        /**
         * Returns the number of sprites for a given dialogue type.
         * @param type the type of the dialogue (e.g., {@link #QUESTION} or {@link #EXCLAMATION})
         * @return the number of sprites for the dialogue type
         */
        public static int GetSpriteAmount(int type) {
            return switch (type) {
                case QUESTION, EXCLAMATION -> 5;
                default -> 0; // Default for any unknown type
            };
        }
    } 

    public static class Projectiles {
        public static final int CANNON_BALL_DEFAULT_WIDTH = 15;
        public static final int CANNON_BALL_DEFAULT_HEIGHT = 15;
        public static int CANNON_BALL_WIDTH = CANNON_BALL_DEFAULT_WIDTH;
        public static int CANNON_BALL_HEIGHT = CANNON_BALL_DEFAULT_HEIGHT;
        public static float SPEED = 0.75f;
    }

    public static class ObjectConstants {
        public static final float POTION_HOVER_DELTA = (float) 1 / GameConstants.FPS_SET;

        public static final int RED_POTION = 0;
        public static final int BLUE_POTION = 1;
        public static final int BARREL = 2;
        public static final int BOX = 3;
        public static final int SPIKE = 4;
        public static final int CANNON_LEFT = 5;
        public static final int CANNON_RIGHT = 6;

        public static final int BLUE_POTION_ROW_INDEX = 0;
        public static final int RED_POTION_ROW_INDEX = 1;
        public static final int BOX_ROW_INDEX = 0;
        public static final int BARREL_ROW_INDEX = 1;

        public static final int RED_POTION_VALUE = 15;
        public static final int BLUE_POTION_VALUE = 10;

        public static final int CONTAINER_WIDTH_DEFAULT = 40;
        public static final int CONTAINER_HEIGHT_DEFAULT = 30;
        public static int CONTAINER_WIDTH = CONTAINER_WIDTH_DEFAULT;
        public static int CONTAINER_HEIGHT = CONTAINER_HEIGHT_DEFAULT;

        public static final int POTION_WIDTH_DEFAULT = 12;
        public static final int POTION_HEIGHT_DEFAULT = 16;
        public static int POTION_WIDTH = POTION_WIDTH_DEFAULT;
        public static int POTION_HEIGHT = POTION_HEIGHT_DEFAULT;

        public static final int SPIKE_WIDTH_DEFAULT = 32;
        public static final int SPIKE_HEIGHT_DEFAULT = 32;
        public static int SPIKE_WIDTH = SPIKE_WIDTH_DEFAULT;
        public static int SPIKE_HEIGHT = SPIKE_HEIGHT_DEFAULT;

        public static final int CANNON_WIDTH_DEFAULT = 40;
        public static final int CANNON_HEIGHT_DEFAULT = 26;
        public static int CANNON_WIDTH = CANNON_WIDTH_DEFAULT;
        public static int CANNON_HEIGHT = CANNON_HEIGHT_DEFAULT;

        /**
         * Returns the number of sprites for a given object type.
         * @param objectType the type of the object (e.g., {@link #RED_POTION}, {@link #BLUE_POTION}, etc.)
         * @return the number of sprites for the object type
         */
        public static int GetSpriteAmount(int objectType) {
            return switch (objectType) {
                case RED_POTION, BLUE_POTION, CANNON_LEFT, CANNON_RIGHT -> 7;
                case BARREL, BOX -> 8;
                default -> 1;
            };
        }
    }

    public static class EnemyConstants {
        public static final int CRABBY = 0;
        public static final int PINKSTAR = 1;
        public static final int SHARK = 2;

        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;

        public static final int CRABBY_WIDTH_DEFAULT = 72;
        public static final int CRABBY_HEIGHT_DEFAULT = 32;
        public static int CRABBY_WIDTH = CRABBY_WIDTH_DEFAULT;
        public static int CRABBY_HEIGHT = CRABBY_HEIGHT_DEFAULT;
        public static int CRABBY_DRAWOFFSET_X = 26;
        public static int CRABBY_DRAWOFFSET_Y = 9;

        public static final int PINKSTAR_WIDTH_DEFAULT = 34;
        public static final int PINKSTAR_HEIGHT_DEFAULT = 30;
        public static int PINKSTAR_WIDTH = PINKSTAR_WIDTH_DEFAULT;
        public static int PINKSTAR_HEIGHT = PINKSTAR_HEIGHT_DEFAULT;
        public static int PINKSTAR_DRAWOFFSET_X = 9;
        public static int PINKSTAR_DRAWOFFSET_Y = 7;

        public static final int SHARK_WIDTH_DEFAULT = 34;
        public static final int SHARK_HEIGHT_DEFAULT = 30;
        public static int SHARK_WIDTH = SHARK_WIDTH_DEFAULT;
        public static int SHARK_HEIGHT = SHARK_HEIGHT_DEFAULT;
        public static int SHARK_DRAWOFFSET_X = 8;
        public static int SHARK_DRAWOFFSET_Y = 6;

        /**
         * Returns the number of sprites for a given enemy type and state.
         * @param enemyType the type of the enemy (e.g., {@link #CRABBY}, {@link #PINKSTAR}, etc.)
         * @param enemyState the state of the enemy (e.g., {@link #IDLE}, {@link #RUNNING}, etc.)
         * @return the number of sprites for the enemy type and state
         */
        public static int GetSpriteAmount(int enemyType, int enemyState) {
            return switch (enemyState) {
                case IDLE -> switch (enemyType) {
                    case CRABBY -> 9;
                    default -> 8;
                };
                case RUNNING -> 6;
                case ATTACK -> switch (enemyType) {
                    case SHARK -> 8;
                    default -> 7;
                };
                case HIT -> 4;
                case DEAD -> 5;
                default -> 0;
            };
        }

        /**
         * Returns the maximum health for a given enemy type.
         * @param enemyType the type of the enemy (e.g., {@link #CRABBY}, {@link #PINKSTAR}, etc.)
         * @return the maximum health for the enemy type
         */
        public static int GetMaxHealth(int enemyType) {
            return switch (enemyType) {
                case CRABBY -> 50;
                case PINKSTAR, SHARK -> 20;
                default -> 1; // Default for any unknown enemy type
            };
        }

        /**
         * Returns the damage dealt by a given enemy type.
         * @param enemyType the type of the enemy (e.g., {@link #CRABBY}, {@link #PINKSTAR}, etc.)
         * @return the damage dealt by the enemy type
         */
        public static int GetEnemyDamage(int enemyType) {
            return switch (enemyType) {
                case CRABBY -> 15;
                case PINKSTAR -> 20;
                case SHARK -> 25;
                default -> 0; // Default for any unknown enemy type
            };
        }
    }

    public static class Environment {
        public static final int BIG_CLOUD_WIDTH_DEFAULT = 448;
        public static final int BIG_CLOUD_HEIGHT_DEFAULT = 101;
        public static int BIG_CLOUD_WIDTH = BIG_CLOUD_WIDTH_DEFAULT;
        public static int BIG_CLOUD_HEIGHT = BIG_CLOUD_HEIGHT_DEFAULT;

        public static final int SMALL_CLOUD_WIDTH_DEFAULT = 74;
        public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 24;
        public static int SMALL_CLOUD_WIDTH = SMALL_CLOUD_WIDTH_DEFAULT;
        public static int SMALL_CLOUD_HEIGHT = SMALL_CLOUD_HEIGHT_DEFAULT;
    }

    public static class UI {
        public static class Buttons {
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static int B_WIDTH = B_WIDTH_DEFAULT;
            public static int B_HEIGHT = B_HEIGHT_DEFAULT;
        }

        public static class PauseButtons {
            public static final int SOUND_SIZE_DEFAULT = 42;
            public static int SOUND_SIZE = SOUND_SIZE_DEFAULT;
        }

        public static class URMButtons {
            public static final int URM_DEFAULT_SIZE = 56;
            public static int URM_SIZE = URM_DEFAULT_SIZE;
        }

        public static class VolumeButtons {
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;
            public static int VOLUME_WIDTH = VOLUME_DEFAULT_WIDTH;
            public static int VOLUME_HEIGHT = VOLUME_DEFAULT_HEIGHT;
            public static int SLIDER_WIDTH = SLIDER_DEFAULT_WIDTH;
        }
    }

    public static class PlayerConstants {
        public static final int PLAYER_SPAWN_ID = 100;

        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMP = 2;
        public static final int FALLING = 3;
        public static final int ATTACK = 4;
        public static final int HIT = 5;
        public static final int DEAD = 6;

        /**
         * Returns the number of sprites for a given player action.
         * @param player_action the action of the player (e.g., {@link #IDLE}, {@link #RUNNING}, etc.)
         * @return the number of sprites for the player action
         */
        public static int GetSpriteAmount(int player_action) {
            return switch (player_action) {
                case DEAD -> 8;
                case RUNNING -> 6;
                case IDLE -> 5;
                case HIT -> 4;
                case JUMP, ATTACK -> 3;
                case FALLING -> 1;
                default -> 1;
            };
        }

        public static final int IMAGE_WIDTH = 64;
        public static final int IMAGE_HEIGHT = 40;

        public static final int PLAYER_WIDTH = 20;
        public static final int PLAYER_HEIGHT = 27;

        public static final int NUM_ANIMATIONS = 7;
        public static final int MAX_NUM_SPRITES = 8;
    }

    public static class Directions {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class GameConstants {
        public static final int FPS_SET = 120;
        public static final int UPS_SET = 200;
        public static final int TILES_DEFAULT_SIZE = 32;
        public static float SCALE = 2f;
        public static final int TILES_IN_WIDTH = 26;
        public static final int TILES_IN_HEIGHT = 14;
        public static int TILES_SIZE = TILES_DEFAULT_SIZE;
        public static int GAME_WIDTH = TILES_IN_WIDTH;
        public static int GAME_HEIGHT = TILES_IN_HEIGHT;
        public static float GRAVITY = 0.04f;
        public static final int ANIMATION_SPEED = 25;
    }

    /**
     * Updates the game constants based on the provided scale.
     * @param scale the scale factor to apply
     */
    public static void UpdateConstantsDependingOnScale(float scale) {
        GameConstants.SCALE = scale;
        GameConstants.TILES_SIZE *= scale;
        GameConstants.GAME_WIDTH *= GameConstants.TILES_SIZE;
        GameConstants.GAME_HEIGHT *= GameConstants.TILES_SIZE;
        GameConstants.GRAVITY *= scale;

        UI.VolumeButtons.VOLUME_HEIGHT *= scale;
        UI.VolumeButtons.VOLUME_WIDTH *= scale;
        UI.VolumeButtons.SLIDER_WIDTH *= scale;

        UI.URMButtons.URM_SIZE *= scale;

        UI.PauseButtons.SOUND_SIZE *= scale;

        UI.Buttons.B_HEIGHT *= scale;
        UI.Buttons.B_WIDTH *= scale;

        Environment.BIG_CLOUD_HEIGHT *= scale;
        Environment.BIG_CLOUD_WIDTH *= scale;
        Environment.SMALL_CLOUD_HEIGHT *= scale;
        Environment.SMALL_CLOUD_WIDTH *= scale;

        EnemyConstants.CRABBY_WIDTH *= scale;
        EnemyConstants.CRABBY_HEIGHT *= scale;
        EnemyConstants.CRABBY_DRAWOFFSET_X *= scale;
        EnemyConstants.CRABBY_DRAWOFFSET_Y *= scale;

        EnemyConstants.PINKSTAR_WIDTH *= scale;
        EnemyConstants.PINKSTAR_HEIGHT *= scale;
        EnemyConstants.PINKSTAR_DRAWOFFSET_X *= scale;
        EnemyConstants.PINKSTAR_DRAWOFFSET_Y *= scale;

        EnemyConstants.SHARK_WIDTH *= scale;
        EnemyConstants.SHARK_HEIGHT *= scale;
        EnemyConstants.SHARK_DRAWOFFSET_X *= scale;
        EnemyConstants.SHARK_DRAWOFFSET_Y *= scale;

        ObjectConstants.CONTAINER_HEIGHT *= scale;
        ObjectConstants.CONTAINER_WIDTH *= scale;

        ObjectConstants.POTION_HEIGHT *= scale;
        ObjectConstants.POTION_WIDTH *= scale;

        ObjectConstants.SPIKE_HEIGHT *= scale;
        ObjectConstants.SPIKE_WIDTH *= scale;

        ObjectConstants.CANNON_HEIGHT *= scale;
        ObjectConstants.CANNON_WIDTH *= scale;

        Projectiles.CANNON_BALL_HEIGHT *= scale;
        Projectiles.CANNON_BALL_WIDTH *= scale;
        Projectiles.SPEED *= scale;

        Dialogue.DIALOGUE_HEIGHT *= scale;
        Dialogue.DIALOGUE_WIDTH *= scale;
    }
}