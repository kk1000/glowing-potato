package fi.tamk.tiko.orion.sleeprunner.stages;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;


import fi.tamk.tiko.orion.sleeprunner.SleepRunner;
import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.graphics.MovingBackground;
import fi.tamk.tiko.orion.sleeprunner.graphics.PauseMenu;

/**
 * Class for pause stage, which uses pauseMenu as actor.
 */
public class PauseStage extends Stage {

    private SleepRunner game;
    private OrthographicCamera camera;

    public PauseStage(SleepRunner g, OrthographicCamera worldCamera,Batch batch){
        super(new ScalingViewport(Scaling.stretch, Constants.APP_WIDTH, Constants.APP_HEIGHT, worldCamera), batch);
        game = g;

        setupMenu();
        camera = worldCamera;
    }

    private void setupMenu(){
        addActor(new PauseMenu());
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
