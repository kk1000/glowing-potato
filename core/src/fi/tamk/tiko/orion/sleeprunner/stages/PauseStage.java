package fi.tamk.tiko.orion.sleeprunner.stages;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;


import fi.tamk.tiko.orion.sleeprunner.SleepRunner;
import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.graphics.PauseMenu;

/**
 * Class for pause stage, which uses pauseMenu as actor.
 */
public class PauseStage extends Stage {

    private OrthographicCamera camera;
    private SleepRunner game;

    private PauseMenu pauseMenu;

    private Actor newGameButton;
    private Actor mainMenuButton;

    private BitmapFont font;
    private Batch batch;

    private boolean isNewGameButtonTouched = false;
    private boolean isMainMenuButtonTouched = false;

    /**
     * Constructor for the pause stage.
     *
     * @param g             Reference to SleepRunner class. (why?)
     * @param worldCamera   Game's UI camera. (world camera?)
     * @param batch         The spritebatch.
     * @param font          The game's general font.
     */
    public PauseStage(SleepRunner g, OrthographicCamera worldCamera,Batch batch, BitmapFont font ){
        super(new ScalingViewport(Scaling.stretch, Constants.APP_WIDTH, Constants.APP_HEIGHT, worldCamera), batch);
        this.game = g;
        this.batch = batch;
        this.camera = worldCamera;
        this.font = font;
        setupMenu();
    }

    /**
     * Setups menu wholly.
     */
    private void setupMenu(){
        pauseMenu = new PauseMenu(Constants.APP_WIDTH / 4, Constants.APP_HEIGHT / 4, font, "Game Over!");
        setupAnimation();
        setupButtons();
        addActor(pauseMenu);
        addActor(newGameButton);
        addActor(mainMenuButton);
    }

    /**
     * Setups menu's buttons.
     */
    private void setupButtons(){
        newGameButton = new TextButton("Play Again", game.getSkin());
        newGameButton.setBounds(Constants.APP_WIDTH / 2.5f,
                Constants.APP_HEIGHT / 2.4f,
                Constants.APP_WIDTH / 5f,
                Constants.APP_HEIGHT / 9.6f);
        newGameButton.setTouchable(Touchable.enabled);
        newGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                isNewGameButtonTouched = true;
                return true;
            }
        });

        mainMenuButton = new TextButton("Main Menu", game.getSkin());
        mainMenuButton.setBounds(Constants.APP_WIDTH / 2.5f,
                Constants.APP_HEIGHT / 3.42f,
                Constants.APP_WIDTH / 5f,
                Constants.APP_HEIGHT / 9.6f);
        mainMenuButton.setTouchable(Touchable.enabled);
        mainMenuButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                isMainMenuButtonTouched = true;
                return true;
            }
        });

    }

    /**
     * Setups pause menu's fade animation.
     */
    private void setupAnimation() {
        this.getRoot().addAction(Actions.sequence(Actions.fadeIn(5)));
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
     * Getters.
     */

    public boolean isNewGameButtonTouched(){
        return isNewGameButtonTouched;
    }

    public boolean isMainMenuButtonTouched(){
        return isMainMenuButtonTouched;
    }

}
