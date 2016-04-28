package fi.tamk.tiko.orion.sleeprunner.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import fi.tamk.tiko.orion.sleeprunner.SleepRunner;
import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.graphics.NightmareMeter;
import fi.tamk.tiko.orion.sleeprunner.graphics.UIText;

/**
 * Draws and handles game's UI.
 */
public class UIStage extends Stage {

    private OrthographicCamera uiCamera;
    private TextButton pauseButton;
    private SleepRunner game;

    private NightmareMeter nightmareMeter;
    private UIText uiText;

    /**
     * Constructor for the UIStage.
     *
     * @param debugFont     Game's debug font.
     * @param uiCamera      Game's UI camera.
     * @param batch         The spritebatch.
     * @param g             Game.
     */
    public UIStage(SleepRunner g, OrthographicCamera uiCamera, BitmapFont debugFont, Batch batch ) {
        super(new ScalingViewport(Scaling.stretch, Constants.APP_WIDTH, Constants.APP_HEIGHT, uiCamera), batch);
        this.game = g;
        this.uiCamera = uiCamera;
    }

    /**
     * Moves Nightmare meter by one step.
     */
    public void moveNightmareMeter( ) {
        nightmareMeter.setValue( nightmareMeter.getValue() + nightmareMeter.getStepSize() );
    }

    /**
     * Setups the stage.
     */
    public void setupUiStage(){
        this.pauseButton = new TextButton(game.translate.get("pause"), game.getSkin());
        this.pauseButton.setBounds(Constants.APP_WIDTH * 0.8f, Constants.APP_HEIGHT * 0.8f, Constants.APP_WIDTH / 9, Constants.APP_HEIGHT / 8);
        this.pauseButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                game.getGameScreen().pause();
            }
        });

        this.nightmareMeter = new NightmareMeter( game.getGameScreen(), game.getSkin() );
        this.uiText = new UIText(game, game.getGameScreen() );

        addActor( pauseButton );
        addActor( nightmareMeter);
        addActor(uiText);
    }

    /**
     * Runs when player collects power up.
     */
    public void collectPowerup( ) {
        uiText.setScore(uiText.getScore() + 50);
    }

    /**
     * Resets user interface for the new game.
     */
    public void reset( ) {
        nightmareMeter.reset();
        uiText.reset();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw() {
        super.draw();
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

    public UIText getUiText(){
        return uiText;
    }
    public NightmareMeter getNightmareMeter( ) { return nightmareMeter; }

}
