package fi.tamk.tiko.orion.sleeprunner.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;

/**
 * Game screen's background.
 */
public class BackgroundDeepLayer extends MovingBackground {

    /**
     * Constructor for deep background.
     */
    public BackgroundDeepLayer(){
        super(new Texture(Gdx.files.internal(Constants.BACKGROUND_IMAGE_PATH)), 10, 2);
    }

}