package fi.tamk.tiko.orion.sleeprunner.graphics;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.objects.NightmareObject;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;

/**
 * Meter for displaying nightmare's position.
 *
 * @author   Eetu "mehxit" Järvinen
 * @author   Joni "steiner3k" Korpisalo
 */
public class NightmareMeter extends ProgressBar {

    private NightmareObject nightmare;
    private GameScreen gameScreen;
    private Skin skin;

    /**
     * Constructor.
     *
     * @param gameScreen GameScreen reference.
     * @param skin       UI Skim.
     */
    public NightmareMeter( GameScreen gameScreen, Skin skin ) {
        super( 0, 3, 1, false, skin );
        this.gameScreen = gameScreen;
        this.skin = skin;
        this.nightmare = this.gameScreen.getNightmare();
        setWidth( getPrefWidth());
        setHeight(getPrefHeight());
        setPosition( Constants.WORLD_TO_SCREEN, Constants.APP_HEIGHT - getHeight() - 80 );
    }

    /**
     * Resets meter.
     */
    public void reset( ) {
        setValue( 0 );
    }

}
