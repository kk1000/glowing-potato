package fi.tamk.tiko.orion.sleeprunner.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import fi.tamk.tiko.orion.sleeprunner.SleepRunner;
import fi.tamk.tiko.orion.sleeprunner.data.Constants;

// TODO: Horizontal background movement.

/**
 * Class for backgrounds.
 * Backgrounds are moving in the Scene2D stages as actors.
 */
public class MovingBackground extends Actor {

    private Texture texture;
    private float updateTimer;
    private float speed;
    private int srcX;
    // 1 = deep 2 = mid 3 = top
    private int layer;
    // 1 = REM 2 = deep
    private int sleepPhase;
    private SleepRunner game;

    public MovingBackground(SleepRunner g,String s, float speed, int i){
        this.game = g;
        this.speed = speed;
        this.layer = i;
        this.texture = new Texture(Gdx.files.internal(s));
        this.sleepPhase = 2;
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }





    public void changePhase(){
        if(layer == 1) {
            if (game.getGameScreen().getCurrentMapChunk().getSleepStage().equals("DEEP") && sleepPhase == 1) {
                texture = new Texture(Gdx.files.internal(Constants.BACKGROUND_IMAGE_PATH));
                texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
                sleepPhase = 2;
            }
            if (game.getGameScreen().getCurrentMapChunk().getSleepStage().equals("REM") && sleepPhase == 2) {
                texture = new Texture(Gdx.files.internal(Constants.REM_SKY_IMAGE_PATH));
                texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
                sleepPhase = 1;
            }
        }

        if(layer == 2) {
            if (game.getGameScreen().getCurrentMapChunk().getSleepStage().equals("DEEP") && sleepPhase == 1) {
                texture = new Texture(Gdx.files.internal(Constants.DEEP_BACKGROUND_IMAGE_PATH));
                texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
                sleepPhase = 2;
            }
            if (game.getGameScreen().getCurrentMapChunk().getSleepStage().equals("REM") && sleepPhase == 2) {
                texture = new Texture(Gdx.files.internal(Constants.REM_BACKGROUND_IMAGE_PATH));
                texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
                sleepPhase = 1;
            }
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, 0, 0, srcX, 0, Constants.APP_WIDTH, Constants.APP_HEIGHT);
    }

    @Override
    public void act(float delta){
        super.act(delta);
        if(updateTimer > speed/60) {
            if(speed < 0.1f){
                srcX += 5;
            }
            else if(speed < 0.2){
                srcX += 4;
            }
            else if(speed < 0.3){
                srcX += 3;
            }
            else if(speed < 0.5f){
                srcX += 2;
            }
            else{srcX += 1;}
            updateTimer = 0;
        }
            changePhase();

        updateTimer += delta;

    }

}

