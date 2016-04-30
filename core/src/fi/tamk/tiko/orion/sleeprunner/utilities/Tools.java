package fi.tamk.tiko.orion.sleeprunner.utilities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;

/**
 * Helper methods for animating a textureregion.
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
     * Split Tileset-texture to textureregion.
     */
    public static TextureRegion[][] splitTileset(Texture t,int width,int height){
        TextureRegion[][]tileset = TextureRegion.split(t, width, height);
        return tileset;
    }
    
    /**
     * Creates preferences-file when launching first time.
     * <p/>
     * TODO: Overrides earlier preference-file at the moment
     */
    public static void createPreferences(Preferences prefs) {
        Gdx.app.log("Tools", "Created preference-file!");
        prefs.putBoolean("isCreated", true);
        prefs.putBoolean("isMuted", false);
        prefs.putFloat("soundVolume", 0.5f);
        prefs.putFloat("musicVolume", 0.5f);

        for (int i = 1; i <= 5; i++) {
            prefs.putInteger("highscore" + i, 0);
        }

        prefs.flush();
    }

    /**
     * Loads everything to our games assetmanager.
     * @param manager game Assetmanager
     * @return loaded Assetmanager
     */

    public static AssetManager loadAssets(AssetManager manager) {

        //sounds
        manager.load("sounds/sfx/steps.mp3", Sound.class);
        manager.load("sounds/sfx/nightmare.mp3", Sound.class);
        manager.load("sounds/sfx/powerup.mp3", Sound.class);
        manager.load("sounds/sfx/death.mp3", Sound.class);
        manager.load("sounds/music/main.mp3", Music.class);
        manager.load("sounds/music/mainmenu.mp3", Music.class);
        // graphics
        // backgrounds
        // backgrounds-deep
        manager.load("graphics/backgrounds/texture_deep_all.png", Texture.class);
        manager.load("graphics/backgrounds/stars.png", Texture.class);
        manager.load("graphics/backgrounds/clouds.png", Texture.class);
        // backgrounds-rem
        manager.load("graphics/backgrounds/texture_rem_all.png", Texture.class);
        manager.load("graphics/backgrounds/sky_green2.png", Texture.class);

        // player
        manager.load("graphics/player.png", Texture.class);
        // nightmare
        manager.load("graphics/nightmare.png", Texture.class);
        // ui
        manager.load("graphics/mainmenu.png", Texture.class);
        manager.load("graphics/pausescreen.png", Texture.class);
        // logos
        manager.load("graphics/logos/tamk_eng_vaaka_RGB.png", Texture.class);
        manager.load("graphics/logos/tiko_musta_eng.png", Texture.class);
        manager.load("graphics/logos/ukkinstitute.png", Texture.class);
        manager.load("graphics/logos/PETO-logo.png", Texture.class);
        // tileset
        manager.load("graphics/tileset_new.png", Texture.class);
        manager.load("graphics/tileset_sign.png",Texture.class);
        manager.finishLoading();
        Gdx.app.log("LaunchScreen", "manager loaded!");

        return manager;
    }
}