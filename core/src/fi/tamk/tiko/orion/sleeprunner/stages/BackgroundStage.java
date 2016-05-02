package fi.tamk.tiko.orion.sleeprunner.stages;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import fi.tamk.tiko.orion.sleeprunner.SleepRunner;
import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.graphics.MovingBackground;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;

/**
 * Class for backgrounds.
 * Uses its own viewport (non-metric).
 */
public class BackgroundStage extends Stage {

    private GameScreen gameScreen;
    private SleepRunner game;
    private MovingBackground deepLayer;
    private MovingBackground midLayer;
    private MovingBackground topLayer;
    private MovingBackground itemLayer;

    /**
     * Constructor.
     *
     * @param gameScreen  GameScreen reference.
     * @param worldCamera GameScreen's uiCamera.
     * @param batch       Batch.
     */
    public BackgroundStage(GameScreen gameScreen, OrthographicCamera worldCamera, Batch batch) {
        super(new ScalingViewport(Scaling.stretch,
                Constants.APP_WIDTH,
                Constants.APP_HEIGHT,
                worldCamera), batch);
        this.gameScreen = gameScreen;
        this.game = gameScreen.getGame();
        setupBackgrounds();
    }

    /**
     * Setups all background-layers.
     */
    private void setupBackgrounds(){
        deepLayer = new MovingBackground(game, game.resources.assetManager.get("graphics/backgrounds/stars.png",Texture.class), 2.5f, 1);
        addActor(deepLayer);
        itemLayer = new MovingBackground(game,game.resources.assetManager.get("graphics/backgrounds/items.png", Texture.class),1.5f,3);
        addActor(itemLayer);
        midLayer = new MovingBackground(game,game.resources.assetManager.get("graphics/backgrounds/texture_deep_all.png",Texture.class), 1f,2);
        addActor(midLayer);
        topLayer = new MovingBackground(game, game.resources.assetManager.get("graphics/backgrounds/clouds.png",Texture.class), 0.5f,4);
        addActor(topLayer);

    }

    public void increaseSpeed(){
        if(deepLayer.getSpeed()>1) {
            deepLayer.setSpeed(deepLayer.getSpeed() - 0.2f);
        }
        if(itemLayer.getSpeed()>1f) {
            itemLayer.setSpeed(itemLayer.getSpeed() - 0.2f);
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
        itemLayer.setSpeed(1.5f);
        midLayer.setSpeed(1);
        topLayer.setSpeed(0.5f);
    }

    public void setSpikeSpeed(){
        deepLayer.setSpeed(6f);
        itemLayer.setSpeed(5f);
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
