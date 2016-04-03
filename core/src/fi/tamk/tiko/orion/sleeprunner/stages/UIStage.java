package fi.tamk.tiko.orion.sleeprunner.stages;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.graphics.NightmareMeter;
import fi.tamk.tiko.orion.sleeprunner.graphics.UIText;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;

/**
 * Draws and handles game's UI.
 */
public class UIStage extends Stage {

    private OrthographicCamera uiCamera;
    private GameScreen gameScreen;
    private BitmapFont debugFont;
    private BitmapFont font;

    private NightmareMeter nightmareMeter;
    private UIText uiText;

    /**
     * Constructor for the UIStage.
     *
     * @param gameScreen    Reference to SleepRunner class.
     * @param debugFont     Game's debug font.
     * @param uiCamera      Game's UI camera.
     * @param font          Game's general font.
     * @param batch         The spritebatch.
     */
    public UIStage(GameScreen gameScreen, OrthographicCamera uiCamera, BitmapFont debugFont, BitmapFont font, Batch batch ) {
        super(new ScalingViewport(Scaling.stretch, Constants.APP_WIDTH, Constants.APP_HEIGHT, uiCamera), batch);
        this.gameScreen = gameScreen;
        this.uiCamera = uiCamera;
        this.debugFont = debugFont;
        this.font = font;

        this.nightmareMeter = new NightmareMeter( this.gameScreen );
        this.uiText = new UIText( this.gameScreen, debugFont, this.font );
        addActor( nightmareMeter );
        addActor( uiText );
    }

    /**
     * Resets user interface for the new game.
     */
    public void reset( ) {
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

}
