package utils;

public class Constants {
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
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMP = 2;
        public static final int FALLING = 3;
        public static final int HIT = 4;
        public static final int ATTACK_1 = 5;
        public static final int DEAD = 6;

        public static int GetSpriteAmount(int player_action) {
            return switch (player_action) {
                case DEAD -> 8;
                case RUNNING -> 6;
                case IDLE -> 5;
                case HIT -> 4;
                case JUMP, ATTACK_1 -> 3;
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
    }
}