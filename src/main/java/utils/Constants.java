package utils;

public class Constants {
    public static class PlayerConstants {
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMP = 2;
        public static final int FALLING = 3;
        public static final int GROUND = 4;
        public static final int HIT = 5;
        public static final int ATTACK_1 = 6;
        public static final int ATTACK_JUMP_1 = 7;
        public static final int ATTACK_JUMP_2 = 8;

        public static int GetSpriteAmount(int player_action) {
            return switch (player_action) {
                case RUNNING -> 6;
                case IDLE -> 5;
                case HIT -> 4;
                case JUMP, ATTACK_1, ATTACK_JUMP_1, ATTACK_JUMP_2 -> 3;
                case GROUND -> 2;
                case FALLING -> 1;
                default -> 1;
            };
        }

        public static final int IMAGE_WIDTH = 64;
        public static final int IMAGE_HEIGHT = 40;

        public static final int NUM_ANIMATIONS = 9;
        public static final int MAX_NUM_SPRITES = 6;
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
        public static final int WINDOW_WIDTH = 1280;
        public static final int WINDOW_HEIGHT = 800;
        public static final int IMAGE_WIDTH = 256;
        public static final int IMAGE_HEIGHT = 160;
    }
}