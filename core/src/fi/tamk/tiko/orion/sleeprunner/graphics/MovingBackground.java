package fi.tamk.tiko.orion.sleeprunner.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;

// TODO: Horizontal background movement.

/**
 * Every moving backgrounds are extended from this class.
 * Backgrounds are moving in the Scene2D stages as actors.
 */
public abstract class MovingBackground extends Actor {

    private Array<float[]> positions = new Array<float[]>();
    private float startY = Constants.APP_HEIGHT;
    private float startX = Constants.APP_WIDTH;

    private Texture texture;
    private int movingSpeed;
    private int repeat;

    /**
     * Constructor for MovingBackground.
     *
     * @param texture     Background texture.
     * @param movingSpeed Background's moving speed.
     * @param repeat      How many times the background texture is repeated.
     */
    public MovingBackground(Texture texture, int movingSpeed, int repeat) {
        this.texture = texture;
        this.setMovingSpeed(movingSpeed);
        this.setRepeat(repeat);
        this.setPositions();
    }

    @Override
    public void act(float delta) {
        // Move every repeated background textures left until they reach the end of screen.
        // then return them back to the starting position and start again.
        for (float[] position : positions) {
            float x = position[0];
            x -= (delta * movingSpeed);
            if ((x + texture.getWidth()) - (delta * movingSpeed) <= 0) {
                // Reached the left.
                x = startX;
            }
            position[0] = x;
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        // Draw every repeat of the background texture.
        for (float[] position : positions) {
            float x = position[0];
            float y = position[1];
            batch.draw(texture, x, y, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        }
    }

    /**
     * Setters.
     */

    public void setMovingSpeed(int movingSpeed) {
        this.movingSpeed = movingSpeed;
    }

    public void setPositions() {
        for (int i = 0; i < this.repeat; i++) {
            positions.add(new float[]{i * Constants.APP_WIDTH, 0,});
        }
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
        // Minimum of two.
        if (this.repeat < 2) {
            this.repeat = 2;
        }
    }

}
