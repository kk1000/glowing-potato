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
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;

/**
 * Class for pause stage, which uses pauseMenu as actor.
 */
public class PauseStage extends Stage {

    private OrthographicCamera camera;
    private SleepRunner game;

    private PauseMenu pauseMenu;

    private Actor button1;
    private Actor button2;

    private BitmapFont font;
    private Batch batch;

    /**
     * Constructor for the pause stage.
     *
     * @param g             Reference to SleepRunner class.
     * @param worldCamera   Game's UI camera.
     * @param batch         The spritebatch.
     * @param font          The game's general font.
     */
    public PauseStage(SleepRunner g, OrthographicCamera worldCamera,Batch batch, BitmapFont font ){
        super(new ScalingViewport(Scaling.stretch, Constants.APP_WIDTH, Constants.APP_HEIGHT, worldCamera), batch);
        this.game = g;
        this.batch = batch;
        this.camera = worldCamera;
        this.font = font;
    }

    /**
     * Setups menu wholly.
     */
    public void setupMenu(){
        if(game.getGameScreen().getGameState() == Constants.GAME_OVER) {
            pauseMenu = new PauseMenu(Constants.APP_WIDTH / 4, Constants.APP_HEIGHT / 4, font, "Game Over!");
            setupAnimation();
            setupNewGameButton();
            setupMainMenuButton();
            addActor(pauseMenu);
            addActor(button1);
            addActor(button2);
        }
        if(game.getGameScreen().getGameState() == Constants.GAME_PAUSED){
            pauseMenu = new PauseMenu(Constants.APP_WIDTH / 4, Constants.APP_HEIGHT / 4, font, "Game Paused!");
            setupAnimation();
            setupContinueButton();
            setupMainMenuButton();
            addActor(pauseMenu);
            addActor(button1);
            addActor(button2);
        }
    }

    /**
     * Setups menu's buttons.
     */

    private void setupNewGameButton(){
        button1 = new TextButton("Play Again", game.getSkin());
        button1.setBounds(Constants.APP_WIDTH / 2.5f,
                Constants.APP_HEIGHT / 2.4f,
                Constants.APP_WIDTH / 5f,
                Constants.APP_HEIGHT / 9.6f);
        button1.setTouchable(Touchable.enabled);
        button1.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                game.getGameScreen().restartGame();
                return true;
            }
        });
    }

    private void setupMainMenuButton(){
        button2 = new TextButton("Main Menu", game.getSkin());
        button2.setBounds(Constants.APP_WIDTH / 2.5f,
                Constants.APP_HEIGHT / 3.42f,
                Constants.APP_WIDTH / 5f,
                Constants.APP_HEIGHT / 9.6f);
        button2.setTouchable(Touchable.enabled);
        button2.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                game.setMainMenuScreen();
                return true;
            }
        });
    }

    private void setupContinueButton(){
        button1 = new TextButton("Continue", game.getSkin());
        button1.setBounds(Constants.APP_WIDTH / 2.5f,
                Constants.APP_HEIGHT / 2.4f,
                Constants.APP_WIDTH / 5f,
                Constants.APP_HEIGHT / 9.6f);
        button1.setTouchable(Touchable.enabled);
        button1.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                GameScreen gameScreen = game.getGameScreen();
                gameScreen.setGameState(Constants.GAME_RUNNING);
                gameScreen.getPlayer().resumeAnimation();
                gameScreen.getNightmare().resumeAnimation();
                gameScreen.setInputProcessor(1);
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




}
