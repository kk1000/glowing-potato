package fi.tamk.tiko.orion.sleeprunner.graphics;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;

/**
 * Game screen's second background.
 */
public class BackgroundMidLayer extends MovingBackground {

    /**
     * Constructor for mid background.
     */
    public BackgroundMidLayer(){
        super(Constants.MOVING_BACKGROUND_IMAGE_PATH, 1);
    }

}