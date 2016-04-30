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
    // 1 = REM 2 = deep 3 = start
    private int sleepPhase;
    private SleepRunner game;

    public MovingBackground(SleepRunner g,Texture t, float speed, int i){
        this.game = g;
        this.speed = speed;
        this.layer = i;
        this.texture = t;
        this.sleepPhase = 1;
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    public void changePhase(){
        if(layer == 1) {
            if (game.getGameScreen().getCurrentMapChunk().getSleepStage().equals("DEEP") && sleepPhase == 1 ) {
                texture = game.manager.get("graphics/backgrounds/stars.png", Texture.class);
                texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
                sleepPhase = 2;
            }
            if (game.getGameScreen().getCurrentMapChunk().getSleepStage().equals("REM") && sleepPhase == 2) {
                texture = game.manager.get("graphics/backgrounds/sky_green2.png", Texture.class);
                texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
                sleepPhase = 1;
            }
        }

        if(layer == 2) {
            if (game.getGameScreen().getCurrentMapChunk().getSleepStage().equals("DEEP") && sleepPhase == 1) {
                texture = game.manager.get("graphics/backgrounds/texture_deep_all.png", Texture.class);
                texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
                sleepPhase = 2;
            }
            if (game.getGameScreen().getCurrentMapChunk().getSleepStage().equals("REM") && sleepPhase == 2) {
                texture = game.manager.get("graphics/backgrounds/texture_rem_all.png", Texture.class);
                texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
                sleepPhase = 1;
            }
        }

    }

    public void setSleepPhase(int i){
        i = sleepPhase;
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
            else if (speed < 5){
                srcX += 1;
            }
            else {srcX += 0;}
            updateTimer = 0;
              }
            changePhase();

        updateTimer += delta;

    }

    /**
     * Setters & getters
     */

    public void setSpeed(float s){
        speed = s;
    }
    public float getSpeed(){
        return speed;
    }
}

