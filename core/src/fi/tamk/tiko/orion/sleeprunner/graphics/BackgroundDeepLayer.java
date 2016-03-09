package fi.tamk.tiko.orion.sleeprunner.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;

/**
 * Game screen's background.
 */
public class BackgroundDeepLayer extends MovingBackground {

    /**
     * Constructor for deep background.
     */

    Texture texture;
    /**
     * Constructor for mid background.
     */
    public BackgroundDeepLayer(){
        super(Constants.BACKGROUND_IMAGE_PATH, 2);
    }

    }
