package fi.tamk.tiko.orion.sleeprunner.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;


import fi.tamk.tiko.orion.sleeprunner.SleepRunner;
import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.graphics.PauseMenu;

/**
 * Class for pause stage, which uses pauseMenu as actor.
 */
public class PauseStage extends Stage {

    private SleepRunner game;
    private OrthographicCamera camera;

    private PauseMenu pauseMenu;

    private Actions actions;

    private Actor newGameButton;
    private Actor mainMenuButton;

    private Batch batch;

    private boolean isNewGameButtonTouched = false;
    private boolean isMainMenuButtonTouched = false;

    public PauseStage(SleepRunner g, OrthographicCamera worldCamera,Batch batch){
        super(new ScalingViewport(Scaling.stretch, Constants.APP_WIDTH, Constants.APP_HEIGHT, worldCamera), batch);
        game = g;
        this.batch = batch;


        setupMenu();

        camera = worldCamera;
    }

    private void setupMenu(){

        pauseMenu = new PauseMenu(Constants.APP_WIDTH / 4, Constants.APP_HEIGHT / 4);
        setupAnimation();
        setupButtons();
        addActor(pauseMenu);
        addActor(newGameButton);
        addActor(mainMenuButton);

        mainMenuButton.debug();
        newGameButton.debug();
    }

    private void setupButtons(){

        newGameButton = new Actor();
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


        mainMenuButton = new Actor();
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

    private void setupAnimation(){

        this.getRoot().addAction(Actions.sequence(Actions.fadeIn(5)));


    }

    public boolean isNewGameButtonTouched(){
        return isNewGameButtonTouched;
    }

    public boolean isMainMenuButtonTouched(){
        return isMainMenuButtonTouched;
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
