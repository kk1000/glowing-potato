package fi.tamk.tiko.orion.sleeprunner.data;

import com.badlogic.gdx.math.Vector2;

/**
 * Global constants
 */
public class Constants {

    public static final boolean FAIR_VERSION = true;
    public static final boolean DEBUG = false;

    /**
     * World & Screen
     */

    public static final int WORLD_TO_SCREEN = 32;

    public static final float WORLD_WIDTH = 8.0f;
    public static final float WORLD_HEIGHT = 4.8f;

    public static final int APP_WIDTH = 800;
    public static final int APP_HEIGHT = 480;

    public static final Vector2 WORLD_GRAVITY = new Vector2(0, -10);

    /**
     * Game speed.
     */

    public static final float INITIAL_GAME_SPEED = -3.0f;
    public static final float PLAYER_DEATH_GAME_SPEED = -2.0f;
    public static final float STOP_GAME_SPEED = 0f;

    /**
     * Game states
     */

    public static final int GAME_READY = 0;
    public static final int GAME_RUNNING = 1;
    public static final int GAME_PAUSED = 2;
    public static final int GAME_PLAYER_DEATH = 3;
    public static final int GAME_OVER = 4;
    public static final int GAME_INFO_SCREEN = 5;

    /**
     * Map generation
     */

    public static final int CHUNK_MAX_TILES_WIDTH = 50;
    public static final int CHUNK_MAX_TILES_HEIGHT = 15;

    public static final int CHUNK_HEIGHT_PIXELS = CHUNK_MAX_TILES_HEIGHT * WORLD_TO_SCREEN;
    public static final int CHUNK_WIDTH_PIXELS = CHUNK_MAX_TILES_WIDTH * WORLD_TO_SCREEN;

    public static final int EMPTY_BLOCK = 0;
    public static final int GROUND_BLOCK = 1;
    public static final int SPIKES_BLOCK = 2;
    public static final int SIGN_BLOCK = 3;
    public static final int POWERUP_SHIELD_BLOCK = 4;
    public static final int POWERUP_FLY_BLOCK = 5;
    public static final int POWERUP_MASK_BLOCK = 6;
    public static final int FLYING_SPIKES_BLOCK = 7;

    public static final int DIFFICULTY_CHANGE_INTERVAL = 6;

    public static final int START_MIN_EMPTY_BLOCKS = 2;
    public static final int START_MAX_EMPTY_BLOCKS = 4;
    public static final int START_MIN_GROUND_BLOCKS = 8;
    public static final int START_MAX_GROUND_BLOCKS = 48;

    /**
     * Nightmare stuff.
     */

    public static final float NIGHTMARE_START_X = -4f;
    public static final float NIGHTMARE_START_Y = 2.4f;

    /**
     * Question stuff.
     */

    public static final int QUESTION_AMOUNT = 15;

    /**
     * Player physics
     */

    public static final Vector2 PLAYER_JUMPING_LINEAR_IMPULSE = new Vector2(0, 0.20f);
    public static final float PLAYER_START_X = Constants.WORLD_TO_SCREEN * 3 / 100f;
    public static final float PLAYER_START_Y = Constants.WORLD_TO_SCREEN * 3 / 100f;
    public static final float PLAYER_FLY_X = Constants.WORLD_TO_SCREEN * 3 / 100f;
    public static final float PLAYER_FLY_Y = Constants.WORLD_TO_SCREEN * 7 / 100f;

    /**
     * UI
     */

    public static final String SKIN_PATH = "ui/uiskin.json";

}
