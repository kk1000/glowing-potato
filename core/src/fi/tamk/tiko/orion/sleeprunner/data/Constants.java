package fi.tamk.tiko.orion.sleeprunner.data;

import com.badlogic.gdx.math.Vector2;

/**
 * Global constants
 */
public class Constants {


    // screen

    public static final int APP_WIDTH = 800;
    public static final int APP_HEIGHT = 480;
    public static final float WORLD_TO_SCREEN = 32;

    // ground

    public static final Vector2 WORLD_GRAVITY = new Vector2(0, -10);
    public static final float GROUND_X = 0;
    public static final float GROUND_Y = 0;

    public static final float GROUND_WIDTH = 50f;
    public static final float GROUND_HEIGHT = 2f;
    public static final float GROUND_DENSITY = 0f;

    // player

    public static final float PLAYER_X = 2;
    public static final float PLAYER_Y = GROUND_Y + GROUND_HEIGHT;
    public static final float PLAYER_WIDTH = 1f;
    public static final float PLAYER_HEIGHT = 2f;
    public static float PLAYER_DENSITY = 0.5f;
    public static final float PLAYER_GRAVITY_SCALE = 3f;
    public static final Vector2 PLAYER_JUMPING_LINEAR_IMPULSE = new Vector2(0,13f);
    public static final float PLAYER_HIT_ANGULAR_IMPULSE = 10f;
    public static float PLAYER_DODGE_X = 2f;
    public static float PLAYER_DODGE_Y = 1.5f;

    // enemy and obstacles

    public static final float ENEMY_X = 25f;
    public static final float ENEMY_DENSITY = PLAYER_DENSITY;
    public static final float RUNNING_SHORT_ENEMY_Y = 1.5f;
    public static final float RUNNING_LONG_ENEMY_Y = 2f;
    public static final float FLYING_ENEMY_Y = 3f;
    public static  Vector2 ENEMY_LINEAR_VELOCITY = new Vector2(-10f, 0);
    public static final float ENEMY_SPEED = 0.8f;


    // paths to images

    public static final String BACKGROUND_IMAGE_PATH = "background.png";
    public static final String MOVING_BACKGROUND_IMAGE_PATH = "moving_background.png";
    public static final String BACKGROUND_CLOUDS_IMAGE_PATH = "clouds.png";
    public static final String GROUND_IMAGE_PATH = "ground_texture.png";

    public static final String RUNNER_RUNNING_IMAGE_PATH = "running.png";
    public static final String RUNNER_DODGING_IMAGE_PATH = "dodge.png";
    public static final String RUNNER_JUMPING_IMAGE_PATH = "jump.png";
    public static final String RUNNER_HIT_IMAGE_PATH = "hit.png";

    public static final String ENEMY_SMALL_IMAGE_PATH = "enemy_small.png";
    public static final String ENEMY_BIG_IMAGE_PATH = "enemy_big.png";
    public static final String ENEMY_LONG_IMAGE_PATH = "enemy_long.png";
    public static final String ENEMY_WIDE_IMAGE_PATH = "enemy_wide.png";

    public static final String MAINMENU_LOGO_IMAGE_PATH = "mainmenu.png";

    // paths to skins and ui

    public static final String SKIN_PATH = "uiskin.json";
    public static final String GAME_FONT_PATH = "score.txt";

    // paths to sounds

    public static final String PLAYER_RUN_SOUND_PATH = "runner_run.mp3";
    public static final String GAME_MUSIC_PATH = "play_music.mp3";


}
