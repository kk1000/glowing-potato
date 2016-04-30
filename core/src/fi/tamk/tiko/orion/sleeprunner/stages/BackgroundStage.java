package fi.tamk.tiko.orion.sleeprunner.stages;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.scenes.scene2d.Actor;
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
    private MovingBackground deepLayer;
    private MovingBackground midLayer;
    private MovingBackground topLayer;

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
        deepLayer = new MovingBackground(game, game.manager.get("graphics/backgrounds/stars.png",Texture.class), 2.5f, 1);
        addActor(deepLayer);
        midLayer = new MovingBackground(game,game.manager.get("graphics/backgrounds/texture_deep_all.png",Texture.class), 1f,2);
        addActor(midLayer);
        topLayer = new MovingBackground(game, game.manager.get("graphics/backgrounds/clouds.png",Texture.class), 0.5f, 3);
        addActor(topLayer);
        // additional top layer
       //addActor(new MovingBackground(game,Constants.BACKGROUND_CLOUDS_IMAGE_PATH,0.05f,3));
    }

    public void increaseSpeed(){
        if(deepLayer.getSpeed()>1) {
            deepLayer.setSpeed(deepLayer.getSpeed() - 0.2f);
        }
        if(midLayer.getSpeed()>0.3f) {
            midLayer.setSpeed(midLayer.getSpeed() - 0.15f);
        }
        if(topLayer.getSpeed()>0.05f) {
            topLayer.setSpeed(topLayer.getSpeed() - 0.10f);
        }
    }
    public void resetSpeed(){
        deepLayer.setSpeed(2.5f);
        midLayer.setSpeed(1);
        topLayer.setSpeed(0.5f);
    }

    public void setSpikeSpeed(){
        deepLayer.setSpeed(6f);
        midLayer.setSpeed(6f);
        topLayer.setSpeed(2f);
    }

    public void reset(){
        setupBackgrounds();
        resetSpeed();
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
