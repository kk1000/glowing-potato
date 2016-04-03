package fi.tamk.tiko.orion.sleeprunner.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.objects.NightmareObject;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;

/**
 * Meter for displaying nightmare's position.
 */
public class NightmareMeter extends Actor {

    private NightmareObject nightmare;
    private ProgressBar progressBar;
    private GameScreen gameScreen;

    /**
     * Constructor.
     */
    public NightmareMeter( GameScreen gameScreen ) {
        this.setX( Constants.APP_WIDTH - 150 );
        this.setY( Constants.APP_HEIGHT - 10 );
        this.gameScreen = gameScreen;
        this.progressBar = new ProgressBar( 0, 10, 1, false, new ProgressBar.ProgressBarStyle() );
        this.nightmare = this.gameScreen.getNightmare();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

}
