package fi.tamk.tiko.orion.sleeprunner.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.sun.glass.events.TouchEvent;


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

    private Batch batch;
    private boolean isTouched;

    private float timer;

    /**
     * Constructor for the pause stage.
     *
     * @param g             Reference to SleepRunner class.
     * @param worldCamera   Game's UI camera.
     * @param batch         The spritebatch.
     */
    public PauseStage(SleepRunner g, OrthographicCamera worldCamera,Batch batch ){
        super(new ScalingViewport(Scaling.stretch, Constants.APP_WIDTH, Constants.APP_HEIGHT, worldCamera), batch);
        this.game = g;
        this.batch = batch;
        this.camera = worldCamera;
        this.timer = 0;
    }

    /**
     * Setups menu wholly.
     */
    public void setupMenu(){
        if(game.getGameScreen().getGameState() == Constants.GAME_OVER) {
            pauseMenu = new PauseMenu(Constants.APP_WIDTH / 4, Constants.APP_HEIGHT / 4, game.getTitleFont(), game.translate.get("game_over"));
            setupNewGameButton();
            setupMainMenuButton();
            addActor(pauseMenu);
            addActor(button1);
            addActor(button2);
        }
        if(game.getGameScreen().getGameState() == Constants.GAME_PAUSED){
            pauseMenu = new PauseMenu(Constants.APP_WIDTH / 4, Constants.APP_HEIGHT / 4, game.getTitleFont(), game.translate.get("game_paused"));
            setupContinueButton();
            setupMainMenuButton();
            addActor(pauseMenu);
            addActor(button1);
            addActor(button2);
        }
        if(game.getGameScreen().getGameState() == Constants.GAME_INFO_SCREEN){
            pauseMenu = new PauseMenu(Constants.APP_WIDTH / 4, Constants.APP_HEIGHT / 4, game.getTextFont(),
                                    game.translate.get("random_fact"+Integer.toString( MathUtils.random(1,15))));
            addActor(pauseMenu);
        }
    }

    /**
     * Setups menu's buttons.
     */

    private void setupNewGameButton(){
        button1 = new TextButton(game.translate.get("play_again"), game.getSkin());
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
        button2 = new TextButton(game.translate.get("main_menu"), game.getSkin());
        button2.setBounds(Constants.APP_WIDTH / 2.5f,
                Constants.APP_HEIGHT / 3.42f,
                Constants.APP_WIDTH / 5f,
                Constants.APP_HEIGHT / 9.6f);
        button2.setTouchable(Touchable.enabled);
        button2.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                game.switchToMainMenuMusic();
                game.setMainMenuScreen();
                return true;
            }
        });
    }

    private void setupContinueButton(){
        button1 = new TextButton(game.translate.get("continue"), game.getSkin());
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


    @Override
    public void act(float delta) {
        super.act(delta);
        timer += Gdx.graphics.getDeltaTime();
        if (game.getGameScreen().getGameState() == Constants.GAME_INFO_SCREEN && Gdx.input.isTouched() && timer > 0.4f) {
            game.getGameScreen().setGameState(Constants.GAME_RUNNING);
            game.getGameScreen().getPlayer().resumeAnimation();
            game.getGameScreen().getNightmare().resumeAnimation();
            game.getGameScreen().setInputProcessor(1);
            timer = 0;
        }
    }

    @Override
    public void draw() {
        super.draw();
    }




}
