package fi.tamk.tiko.orion.sleeprunner.stages;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
    private MoveToAction moveToAction;

    public PauseStage(SleepRunner g, OrthographicCamera worldCamera,Batch batch){
        super(new ScalingViewport(Scaling.stretch, Constants.APP_WIDTH, Constants.APP_HEIGHT, worldCamera), batch);
        game = g;

        setupMenu();
        camera = worldCamera;
    }

    private void setupMenu(){

        pauseMenu = new PauseMenu(Constants.APP_WIDTH / 4, Constants.APP_HEIGHT*2);

        moveToAction = new MoveToAction();
        moveToAction.setPosition(Constants.APP_WIDTH / 4, Constants.APP_HEIGHT/4);
        moveToAction.setDuration(0.35f);
        pauseMenu.addAction(moveToAction);

        addActor(pauseMenu);
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
