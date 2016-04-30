package fi.tamk.tiko.orion.sleeprunner.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Global constants
 */
public class Constants {

    public static final boolean DEBUG = true;

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

    public static Vector2 INITIAL_GAME_SPEED = new Vector2( -3.0f, 0 );
    public static Vector2 PLAYER_DEATH_GAME_SPEED = new Vector2( -2.0f, 0 );
    public static Vector2 STOP_GAME_SPEED = new Vector2( 0, 0 );

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

    public static final Texture TILESET = new Texture(Gdx.files.internal("graphics/tileset.png"));
    public static final TextureRegion[][] TILESET_SPRITES = TextureRegion.split(TILESET, WORLD_TO_SCREEN, WORLD_TO_SCREEN);

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
     * Sign stuff
     */

    public static final int SIGN_WIDTH = 64;
    public static final int SIGN_HEIGHT = 96;

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

    // Skins
    public static final String SKIN_PATH = "ui/uiskin.json";

    /**
     * Texture image paths
     */


    // Player
    public static final String PLAYER_RUNNING_IMAGE_PATH = "graphics/player.png";

    // Nightmare
    public static final String NIGHTMARE_BACKGROUND_IMAGE_PATH = "graphics/nightmare.png";

    // UI
    public static final String MAINMENU_LOGO_IMAGE_PATH = "graphics/mainmenu.png";
    public static final String PAUSEMENU_IMAGE_PATH = "graphics/pausescreen.png";

    /**
     * Sound paths
     */

    public static final String PLAYER_RUN_SOUND_PATH = "sounds/sfx/steps.mp3";
    public static final String GAME_MUSIC_PATH = "sounds/music/main.mp3";
    public static final String NIGHTMARE_SOUND_PATH = "sounds/sfx/nightmare.mp3";
    public static final String POWERUP_SOUND_PATH = "sounds/sfx/powerup.mp3";
    public static final String PLAYER_DEATH_SOUND = "sounds/sfx/death.mp3";

    public static float PLAYER_DENSITY = 0.5f;

}
