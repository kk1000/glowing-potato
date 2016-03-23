package fi.tamk.tiko.orion.sleeprunner.stages;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import fi.tamk.tiko.orion.sleeprunner.SleepRunner;
import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.graphics.MovingBackground;

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
        super(new ScalingViewport(Scaling.stretch,
                Constants.APP_WIDTH,
                Constants.APP_HEIGHT,
                worldCamera), batch);
        game = g;

        setupBackgrounds();
        camera = worldCamera;
    }

    /**
     * Setups all background-layers.
     */
    private void setupBackgrounds(){

        // deep layer
        addActor(new MovingBackground(Constants.BACKGROUND_IMAGE_PATH, 2));
        // mid layer
        addActor(new MovingBackground(Constants.MOVING_BACKGROUND_IMAGE_PATH, 1));
        // top layer
        addActor(new MovingBackground(Constants.BACKGROUND_CLOUDS_IMAGE_PATH,0.5f));

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
