package fi.tamk.tiko.orion.sleeprunner.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;

/**
 * Game screen's second background.
 */
public class BackgroundMidLayer extends MovingBackground {

    /**
     * Constructor for mid background.
     */
    public BackgroundMidLayer(){
        super(Constants.MOVING_BACKGROUND_IMAGE_PATH, 2);
    }

}