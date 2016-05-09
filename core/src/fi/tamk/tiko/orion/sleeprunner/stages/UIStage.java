package fi.tamk.tiko.orion.sleeprunner.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import fi.tamk.tiko.orion.sleeprunner.SleepRunner;
import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.graphics.NightmareMeter;
import fi.tamk.tiko.orion.sleeprunner.graphics.PowerUpBox;
import fi.tamk.tiko.orion.sleeprunner.graphics.UIText;
import fi.tamk.tiko.orion.sleeprunner.objects.PowerUpGameObject;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;

/**
 * Draws and handles game's UI.
 *
 * @author   Eetu "mehxit" Järvinen
 * @author   Joni "steiner3k" Korpisalo
 */
public class UIStage extends Stage {

    private OrthographicCamera uiCamera;
    private PowerUpBox powerUpBox;
    private TextButton pauseButton;
    private GameScreen gameScreen;
    private SleepRunner game;

    private NightmareMeter nightmareMeter;
    private UIText uiText;

    /**
     * Constructor for the UIStage.
     *
     * @param gameScreen    GameScreen reference.
     * @param uiCamera      Game's UI camera.
     * @param batch         The spritebatch.
     */
    public UIStage(GameScreen gameScreen, OrthographicCamera uiCamera, Batch batch ) {
        super(new ScalingViewport(Scaling.stretch, Constants.APP_WIDTH, Constants.APP_HEIGHT, uiCamera), batch);
        this.gameScreen = gameScreen;
        this.game = gameScreen.getGame();
        this.uiCamera = uiCamera;
    }

    /**
     * Setups the stage.
     */
    public void setupUiStage(){
        // Pause button.
        pauseButton = new TextButton(game.translate.get("pause"), game.resources.skin);
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

        nightmareMeter = new NightmareMeter( game.getGameScreen(), game.resources.skin );
        powerUpBox = new PowerUpBox( game, game.getGameScreen() );
        powerUpBox.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Clicking power up box uses collected power up.
                powerUpBox.usePowerUp();
                return true;
            }
        });

        uiText = new UIText(game, game.getGameScreen() );

        addActor(pauseButton);
        addActor(powerUpBox);
        addActor(nightmareMeter);
        addActor(uiText);
    }


    /**
     * Moves nightmare meter up by one step.
     */
    public void addNightmareMeterByStep( ) {
        nightmareMeter.setValue(nightmareMeter.getValue() + nightmareMeter.getStepSize());
    }

    /**
     * Moves nightmare meter back by one step.
     */
    public void minusNightmareMeterByStep( ) {
        nightmareMeter.setValue(nightmareMeter.getValue() - nightmareMeter.getStepSize());
    }

    /**
     * Runs when player collects power up.
     *
     * @param  powerUpGameObject PowerUpGameObject player collected.
     */
    public void collectPowerup( PowerUpGameObject powerUpGameObject ) {
        powerUpBox.setPowerUpPicked( powerUpGameObject );
        uiText.setScore(uiText.getScore() + 100);
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
     * @return nightmare meter.
     */
    public NightmareMeter getNightmareMeter( ) { return nightmareMeter; }

    /**
     * @return power up box.
     */
    public PowerUpBox getPowerUpBox( ) { return powerUpBox; }

    /**
     * @return UI text.
     */
    public UIText getUiText(){
        return uiText;
    }

}
