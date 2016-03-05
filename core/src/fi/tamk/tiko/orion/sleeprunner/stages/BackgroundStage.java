package fi.tamk.tiko.orion.sleeprunner.stages;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import fi.tamk.tiko.orion.sleeprunner.SleepRunner;
import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.graphics.Background;
import fi.tamk.tiko.orion.sleeprunner.graphics.Background2;
import fi.tamk.tiko.orion.sleeprunner.graphics.Background3;

/**
 * Class for backgrounds.
 * Uses its own viewport (non-metric).
 */
public class BackgroundStage extends Stage {

    private SleepRunner game;
    private OrthographicCamera camera;

    /**
     * Constructor.
     * Uses background camera from GameScreen.
     */
    public BackgroundStage(SleepRunner g, OrthographicCamera worldCamera, Batch batch) {
        super(new ScalingViewport(Scaling.stretch, Constants.APP_WIDTH, Constants.APP_HEIGHT, worldCamera), batch);
        game = g;

        setupBackgrounds();
        camera = worldCamera;
    }

    /**
     * Setups all background-layers.
     */
    private void setupBackgrounds(){

        addActor(new Background());
        addActor(new Background2());
        addActor(new Background3());

    }

    /**
     * Methods overwritten from the Stage class.
     */
    @Override
    public boolean touchDown(int x, int y, int pointer, int button) { return true; }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) { return true; }


    @Override
    public void act(float delta) {

        super.act(delta);

    }

    @Override
    public void draw() {

        super.draw();

    }



}