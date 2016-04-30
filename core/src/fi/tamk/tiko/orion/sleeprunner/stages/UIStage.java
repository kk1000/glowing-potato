package fi.tamk.tiko.orion.sleeprunner.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import fi.tamk.tiko.orion.sleeprunner.SleepRunner;
import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.graphics.NightmareMeter;
import fi.tamk.tiko.orion.sleeprunner.graphics.PowerUpBox;
import fi.tamk.tiko.orion.sleeprunner.graphics.UIText;
import fi.tamk.tiko.orion.sleeprunner.objects.PowerUpGameObject;

/**
 * Draws and handles game's UI.
 */
public class UIStage extends Stage {

    private OrthographicCamera uiCamera;
    private PowerUpBox powerUpBox;
    private TextButton pauseButton;
    private SleepRunner game;

    private NightmareMeter nightmareMeter;
    private UIText uiText;

    /**
     * Constructor for the UIStage.
     *
     * @param uiCamera      Game's UI camera.
     * @param batch         The spritebatch.
     * @param g             Game.
     */
    public UIStage(SleepRunner g, OrthographicCamera uiCamera, Batch batch ) {
        super(new ScalingViewport(Scaling.stretch, Constants.APP_WIDTH, Constants.APP_HEIGHT, uiCamera), batch);
        this.game = g;
        this.uiCamera = uiCamera;
    }

    /**
     * Moves Nightmare meter by one step.
     */
    public void moveNightmareMeter( ) {
        nightmareMeter.setValue(nightmareMeter.getValue() + nightmareMeter.getStepSize());
    }

    /**
     * Setups the stage.
     */
    public void setupUiStage(){
        // Pause button.
        pauseButton = new TextButton(game.translate.get("pause"), game.getSkin());
        pauseButton.setWidth(100f);
        pauseButton.setHeight(75f);
        pauseButton.setPosition(Constants.APP_WIDTH - pauseButton.getWidth(), Constants.APP_HEIGHT - pauseButton.getHeight());
        pauseButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Pause button sets game to pause state.
                game.getGameScreen().setGameState(Constants.GAME_PAUSED);
                return true;
            }
        });

        nightmareMeter = new NightmareMeter( game.getGameScreen(), game.getSkin() );
        powerUpBox = new PowerUpBox( game, game.getGameScreen() );
        uiText = new UIText(game, game.getGameScreen() );

        addActor( pauseButton );
        addActor( powerUpBox );
        addActor(nightmareMeter);
        addActor(uiText);
    }

    /**
     * Runs when player collects power up.
     *
     * @param  powerUpGameObject PowerUpGameObject player collected.
     */
    public void collectPowerup( PowerUpGameObject powerUpGameObject ) {
        powerUpBox.setPowerUpPicked( powerUpGameObject );
        uiText.setScore(uiText.getScore() + 50);
    }

    /**
     * Resets user interface for the new game.
     */
    public void reset( ) {
        nightmareMeter.reset();
        powerUpBox.reset();
        uiText.reset();
    }

    /**
     * Checks has the nightmare reached the player.
     *
     * @return boolean Is the nightmare meter full.
     */
    public boolean hasNightmareReached( ) {
        return ( nightmareMeter.getValue() == nightmareMeter.getMaxValue() );
    }

    /**
     * Getters
     */

    public NightmareMeter getNightmareMeter( ) { return nightmareMeter; }
    public PowerUpBox getPowerUpBox( ) { return powerUpBox; }
    public UIText getUiText(){
        return uiText;
    }

}
