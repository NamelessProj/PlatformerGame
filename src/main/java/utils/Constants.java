package utils;

public class Constants {
    public static class Projectiles {
        public static final int CANNON_BALL_DEFAULT_WIDTH = 15;
        public static final int CANNON_BALL_DEFAULT_HEIGHT = 15;
        public static final int CANNON_BALL_WIDTH = (int) (CANNON_BALL_DEFAULT_WIDTH * GameConstants.SCALE);
        public static final int CANNON_BALL_HEIGHT = (int) (CANNON_BALL_DEFAULT_HEIGHT * GameConstants.SCALE);
        public static final float SPEED = 0.5f * GameConstants.SCALE;
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
        public static final int CONTAINER_WIDTH = (int) (CONTAINER_WIDTH_DEFAULT * GameConstants.SCALE);
        public static final int CONTAINER_HEIGHT = (int) (CONTAINER_HEIGHT_DEFAULT * GameConstants.SCALE);

        public static final int POTION_WIDTH_DEFAULT = 12;
        public static final int POTION_HEIGHT_DEFAULT = 16;
        public static final int POTION_WIDTH = (int) (POTION_WIDTH_DEFAULT * GameConstants.SCALE);
        public static final int POTION_HEIGHT = (int) (POTION_HEIGHT_DEFAULT * GameConstants.SCALE);

        public static final int SPIKE_WIDTH_DEFAULT = 32;
        public static final int SPIKE_HEIGHT_DEFAULT = 32;
        public static final int SPIKE_WIDTH = (int) (SPIKE_WIDTH_DEFAULT * GameConstants.SCALE);
        public static final int SPIKE_HEIGHT = (int) (SPIKE_HEIGHT_DEFAULT * GameConstants.SCALE);

        public static final int CANNON_WIDTH_DEFAULT = 40;
        public static final int CANNON_HEIGHT_DEFAULT = 26;
        public static final int CANNON_WIDTH = (int) (CANNON_WIDTH_DEFAULT * GameConstants.SCALE);
        public static final int CANNON_HEIGHT = (int) (CANNON_HEIGHT_DEFAULT * GameConstants.SCALE);

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

        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;

        public static final int CRABBY_WIDTH_DEFAULT = 72;
        public static final int CRABBY_HEIGHT_DEFAULT = 32;
        public static final int CRABBY_WIDTH = (int) (CRABBY_WIDTH_DEFAULT * GameConstants.SCALE);
        public static final int CRABBY_HEIGHT = (int) (CRABBY_HEIGHT_DEFAULT * GameConstants.SCALE);

        public static final int CRABBY_DRAWOFFSET_X = (int) (26 * GameConstants.SCALE);
        public static final int CRABBY_DRAWOFFSET_Y = (int) (9 * GameConstants.SCALE);

        public static int GetSpriteAmount(int enemyType, int enemyState) {
            return switch (enemyType) {
                case CRABBY -> switch (enemyState) {
                    case IDLE -> 9;
                    case RUNNING -> 6;
                    case ATTACK -> 7;
                    case HIT -> 4;
                    case DEAD -> 5;
                    default -> 0;
                };
                default -> 0; // Default for any unknown enemy type
            };
        }

        public static int GetMaxHealth(int enemyType) {
            return switch (enemyType) {
                case CRABBY -> 10;
                default -> 1; // Default for any unknown enemy type
            };
        }

        public static int GetEnemyDamage(int enemyType) {
            return switch (enemyType) {
                case CRABBY -> 15;
                default -> 0; // Default for any unknown enemy type
            };
        }
    }

    public static class Environment {
        public static final int BIG_CLOUD_WIDTH_DEFAULT = 448;
        public static final int BIG_CLOUD_HEIGHT_DEFAULT = 101;
        public static final int BIG_CLOUD_WIDTH = (int) (BIG_CLOUD_WIDTH_DEFAULT * GameConstants.SCALE);
        public static final int BIG_CLOUD_HEIGHT = (int) (BIG_CLOUD_HEIGHT_DEFAULT * GameConstants.SCALE);

        public static final int SMALL_CLOUD_WIDTH_DEFAULT = 74;
        public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 24;
        public static final int SMALL_CLOUD_WIDTH = (int) (SMALL_CLOUD_WIDTH_DEFAULT * GameConstants.SCALE);
        public static final int SMALL_CLOUD_HEIGHT = (int) (SMALL_CLOUD_HEIGHT_DEFAULT * GameConstants.SCALE);
    }

    public static class UI {
        public static class Buttons {
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * GameConstants.SCALE);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * GameConstants.SCALE);
        }

        public static class PauseButtons {
            public static final int SOUND_SIZE_DEFAULT = 42;
            public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * GameConstants.SCALE);
        }

        public static class URMButtons {
            public static final int URM_DEFAULT_SIZE = 56;
            public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * GameConstants.SCALE);
        }

        public static class VolumeButtons {
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;
            public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * GameConstants.SCALE);
            public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * GameConstants.SCALE);
            public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * GameConstants.SCALE);
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
        public static final float SCALE = 2f;
        public static final int TILES_IN_WIDTH = 26;
        public static final int TILES_IN_HEIGHT = 14;
        public static final int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
        public static final int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
        public static final int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
        public static final float GRAVITY = 0.04f * SCALE;
        public static final int ANIMATION_SPEED = 25;
    }
}