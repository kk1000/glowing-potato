package fi.tamk.tiko.orion.sleeprunner.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

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

    public MovingBackground(String s, float speed){
        this.speed = speed;
        this.texture = new Texture(Gdx.files.internal(s));
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, 0, 0, srcX, 0, Constants.APP_WIDTH, Constants.APP_HEIGHT);
    }

    @Override
    public void act(float delta){
        super.act(delta);
        if(updateTimer > speed/60) {
            srcX += 1;
            updateTimer = 0;
        }
        updateTimer += Gdx.graphics.getDeltaTime();
    }

}

