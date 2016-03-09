package fi.tamk.tiko.orion.sleeprunner.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;

// TODO: Horizontal background movement.

/**
 * Every moving backgrounds are extended from this class.
 * Backgrounds are moving in the Scene2D stages as actors.
 */
public abstract class MovingBackground extends Actor {

    /**
     * Constructor for deep background.
     */

    Texture texture;
    int srcX;
    float speed;
    float updateTimer;

    public MovingBackground(String s, float speed){
        this.speed = speed;
        this.texture = new Texture(Gdx.files.internal(s));
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    public void draw(Batch batch, float parentAlpha) {
            batch.draw(texture, 0, 0, srcX, 0, Constants.APP_WIDTH, Constants.APP_HEIGHT);
    }

    public void act(float delta){
        super.act(delta);
        if(updateTimer > speed/60) {
            srcX +=1;
            updateTimer  = 0;
        }
        updateTimer+=Gdx.graphics.getDeltaTime();
    }

}

