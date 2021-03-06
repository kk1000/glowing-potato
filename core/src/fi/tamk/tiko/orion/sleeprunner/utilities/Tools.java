package fi.tamk.tiko.orion.sleeprunner.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.I18NBundle;

/**
 * Helper methods for animating a textureregion.
 *
 * @author   Eetu "mehxit" Järvinen
 * @author   Joni "steiner3k" Korpisalo
 */
public class Tools {

    /**
     * Creates animation from the given sheet (texture).
     *
     * @param texture   Animation sheet Texture.
     * @param frameCols Amount of frame columns in the sheet.
     * @param frameRows Amount of frame rows in the sheet.
     * @param start     Which frame the animation should start.
     * @param length    How many frames there should be.
     * @param fps       Frames per second, how fast animation runs.
     * @return Animation.
     */
    public static Animation createAnimation(Texture texture, int frameCols, int frameRows, int start, int length, float fps) {
        TextureRegion[][] temporary = TextureRegion.split(texture, texture.getWidth() / frameCols, texture.getHeight() / frameRows);
        TextureRegion[] frames = new TextureRegion[length];
        int index = 0;
        int count = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                if ((i + j) >= (start - 1) && count <= (length - 1)) {
                    frames[index++] = temporary[i][j];
                    count++;
                }
            }
        }
        return new Animation(fps, frames);
    }

    /**
     * Go all the TextureRegion through and flip them.
     *
     * @param animation the animation which frames will be flipped.
     */
    public static void flip(Animation animation) {
        TextureRegion[] regions = animation.getKeyFrames();
        for (TextureRegion r : regions) {
            r.flip(true, false);
        }
    }

    /**
     * Creates preferences-file when launching first time.
     */
    public static void createPreferences(Preferences prefs) {
        Gdx.app.log("Tools", "Created preference-file!");

        prefs.putBoolean("isCreated", true);
        prefs.putBoolean("isMuted", false);
        prefs.putFloat("soundVolume", 0.7f);
        prefs.putFloat( "gameMusicVolume", 0.4f );
        prefs.putFloat( "menuMusicVolume", 0.7f );

        for (int i = 1; i <= 5; i++) {
            prefs.putInteger("highscore" + i, 0);
        }

        prefs.flush();
    }

    /**
     * Loads everything to our game's asset manager.
     *
     * @param  manager Game's AssetManager.
     * @return         Loaded AssetManager.
     */
    public static AssetManager loadAssets(AssetManager manager) {
        //Sounds
        manager.load("sounds/sfx/steps.mp3", Sound.class);
        manager.load("sounds/sfx/nightmarereach.mp3", Sound.class);
        manager.load("sounds/sfx/nightmareback.mp3", Sound.class );
        manager.load("sounds/sfx/powerup.mp3", Sound.class);
        manager.load("sounds/sfx/death.mp3", Sound.class);
        manager.load("sounds/sfx/fly.mp3", Sound.class );
        manager.load("sounds/music/main.mp3", Music.class);
        manager.load("sounds/music/mainmenu.mp3", Music.class);
        manager.load("sounds/sfx/success.mp3", Sound.class );
        manager.load("sounds/sfx/mistake.mp3", Sound.class );
        manager.load("sounds/sfx/shield.mp3", Sound.class );

        // Graphics

        // backgrounds
        // backgrounds-deep
        manager.load("graphics/backgrounds/texture_deep_all.png", Texture.class);
        manager.load("graphics/backgrounds/stars.png", Texture.class);
        manager.load("graphics/backgrounds/clouds.png", Texture.class);
        manager.load("graphics/backgrounds/items.png", Texture.class);

        // Backgrounds-rem
        manager.load("graphics/backgrounds/texture_rem_all.png", Texture.class);
        manager.load("graphics/backgrounds/sky_green2.png", Texture.class);
        manager.load("graphics/backgrounds/items2.png", Texture.class);

        // Player
        manager.load("graphics/player.png", Texture.class);

        // Nightmare
        manager.load("graphics/nightmare.png", Texture.class);

        // UI
        manager.load("graphics/mainmenu.png", Texture.class);
        manager.load( "graphics/powerupboxbackground.png", Texture.class );
        manager.load( "graphics/guide.png", Texture.class );
        manager.load("graphics/flags/english.png", Texture.class);
        manager.load("graphics/flags/finnish.png", Texture.class);
        manager.load("graphics/mutebutton.png", Texture.class);
        manager.load("graphics/mutedbutton.png", Texture.class);

        // Logos
        manager.load("graphics/logos/tamk_eng_vaaka_RGB.png", Texture.class);
        manager.load("graphics/logos/tiko_musta_eng.png", Texture.class);
        manager.load("graphics/logos/ukkinstitute.png", Texture.class);
        manager.load("graphics/logos/PETO-logo.png", Texture.class);

        // Tileset.
        manager.load("graphics/tileset_new.png", Texture.class);
        manager.load("graphics/tileset_signs.png", Texture.class);
        manager.finishLoading();

        // Languages (finnish and english)
        manager.load("localization/languages_en_EN", I18NBundle.class);
        manager.load("localization/languages_fi_FI", I18NBundle.class);

        manager.finishLoading();
        Gdx.app.log("LaunchScreen", "manager loaded!");
        return manager;
    }
}