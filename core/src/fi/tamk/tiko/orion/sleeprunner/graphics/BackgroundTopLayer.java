package fi.tamk.tiko.orion.sleeprunner.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;

/**
 * Game screen's second background.
 *
 */
public class BackgroundTopLayer extends MovingBackground {

    /**
     * Constructor for background top layer.
     */
    public BackgroundTopLayer(){
        super(new Texture(Gdx.files.internal(Constants.BACKGROUND_CLOUDS_IMAGE_PATH)), 110, 2);
    }


}