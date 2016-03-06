package fi.tamk.tiko.orion.sleeprunner.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Global constants
 */
public class Constants {

    // World & Screen

    public static final Vector2 WORLD_GRAVITY = new Vector2(0, -10);

    public static final int WORLD_TO_SCREEN = 32;

    public static final float WORLD_WIDTH = 8.0f;
    public static final float WORLD_HEIGHT = 4.8f;

    public static final int APP_WIDTH = 800;
    public static final int APP_HEIGHT = 480;

    // Map chunk

    public static final int CHUNK_MAX_TILES_WIDTH = 50;
    public static final int CHUNK_MAX_TILES_HEIGHT = 15;

    public static final Texture TILESET = new Texture(Gdx.files.internal("tileset.png"));
    public static final TextureRegion[][] TILESET_SPRITES = TextureRegion.split(TILESET, WORLD_TO_SCREEN, WORLD_TO_SCREEN);

    public static final int EMPTY_BLOCK = 0;
    public static final int GROUND_BLOCK = 1;

    // PlayerObject

    public static final float PLAYER_GRAVITY_SCALE = 3f;
    public static final Vector2 PLAYER_JUMPING_LINEAR_IMPULSE = new Vector2(0, 0.50f);
    public static final float PLAYER_HIT_ANGULAR_IMPULSE = 10f;
    public static final float ENEMY_X = 25f;
    public static final float RUNNING_SHORT_ENEMY_Y = 1.5f;
    public static final float RUNNING_LONG_ENEMY_Y = 2f;

    // enemy and obstacles
    public static final float FLYING_ENEMY_Y = 3f;
    public static final float ENEMY_SPEED = -1.0f;
    public static final String BACKGROUND_IMAGE_PATH = "background.png";
    public static final String MOVING_BACKGROUND_IMAGE_PATH = "moving_background.png";
    public static final String BACKGROUND_CLOUDS_IMAGE_PATH = "clouds.png";
    public static final String GROUND_IMAGE_PATH = "ground_texture.png";
    public static final String PLAYER_RUNNING_IMAGE_PATH = "ukkospritesheet.png";


    // paths to images
    public static final String PLAYER_DODGING_IMAGE_PATH = "dodge.png";
    public static final String PLAYER_JUMPING_IMAGE_PATH = "jump.png";
    public static final String PLAYER_HIT_IMAGE_PATH = "hit.png";
    public static final String ENEMY_SMALL_IMAGE_PATH = "enemy_small.png";
    public static final String ENEMY_BIG_IMAGE_PATH = "enemy_big.png";
    public static final String ENEMY_LONG_IMAGE_PATH = "enemy_long.png";
    public static final String ENEMY_WIDE_IMAGE_PATH = "enemy_wide.png";
    public static final String MAINMENU_LOGO_IMAGE_PATH = "mainmenu.png";
    public static final String SKIN_PATH = "uiskin.json";
    public static final String GAME_FONT_PATH = "score.txt";
    public static final String PLAYER_RUN_SOUND_PATH = "runner_run.mp3";
    public static final String GAME_MUSIC_PATH = "play_music.mp3";
    public static float PLAYER_DENSITY = 0.5f;

    // paths to skins and ui
    public static final float ENEMY_DENSITY = PLAYER_DENSITY;
    public static float PLAYER_DODGE_X = 2f;

    // paths to sounds
    public static float PLAYER_DODGE_Y = 1.5f;
    public static Vector2 ENEMY_LINEAR_VELOCITY = new Vector2(-2.5f, 0);

}
