package fi.tamk.tiko.orion.sleeprunner.utilities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
                if (index >= (start - 1) && count <= (length - 1)) {
                    frames[index++] = temporary[i][j];
                    count++;
                }
            }
        }
        Gdx.app.log("Tools", "Created animation with " + frames.length + " frames.");
        return new Animation(fps, frames);
    }

    /**
     * Go all the TextureRegion through and flip them.
     *
     * @param animation the animation which frames will be flipped.
     */
    public static void flip(Animation animation) {
        TextureRegion[] regions = animation.getKeyFrames();
        for(TextureRegion r : regions) {
            r.flip(true, false);
        }
    }

    /**
     * Creates preferences-file when launching first time.
     */
    public static void createPreferences(Preferences prefs){
        prefs.putBoolean("isMuted",false);
        prefs.putFloat("soundVolume",1f);
        prefs.putFloat("musicVolume",1f);
        prefs.flush();
    }
}
