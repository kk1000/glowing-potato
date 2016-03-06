package fi.tamk.tiko.orion.sleeprunner.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;

/**
 * Game screen's background.
 * Note drawing uses pixels!
 */
public class GameScreenBackground {

    private Texture baseBackground;
    private Texture cloudsBackground;
    private Texture movingBackground;

    private float movingBackgroundX = 0;

    private int speed = 10;

    /**
     * Constructor for background.
     */
    public GameScreenBackground() {
        baseBackground = new Texture(Gdx.files.internal(Constants.BACKGROUND_IMAGE_PATH));
        cloudsBackground = new Texture(Gdx.files.internal(Constants.BACKGROUND_CLOUDS_IMAGE_PATH));
        movingBackground = new Texture(Gdx.files.internal(Constants.MOVING_BACKGROUND_IMAGE_PATH));
    }

    /**
     * Sets value for speed.
     *
     * @param speed Value for speed
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Moves mountain and cloud backgrounds until they reach the end of screen.
     *
     * @param delta Delta time.
     */
    public void move(float delta) {
        // TODO: Background moving.
    }

    /**
     * Draws all three backgrounds.
     *
     * @param batch The Spritebatch
     */
    public void draw(Batch batch) {
        batch.draw(baseBackground, 0, 0, baseBackground.getWidth(), baseBackground.getHeight());

        batch.draw(movingBackground, movingBackgroundX, 0, movingBackground.getWidth(), movingBackground.getHeight());

        batch.draw(cloudsBackground, 0, 0, cloudsBackground.getWidth(), cloudsBackground.getHeight());
    }

    /**
     * Disposes all the backgrounds.
     */
    public void dispose() {
        baseBackground.dispose();
        cloudsBackground.dispose();
        movingBackground.dispose();
    }

}
