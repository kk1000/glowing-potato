package fi.tamk.tiko.orion.sleeprunner.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;

/**
 * Game screen's background.
 */
public class BackgroundDeepLayer extends Actor {

    /**
     * Constructor for deep background.
     */

    Texture texture;
    int srcX;

    public BackgroundDeepLayer(){
        texture = new Texture(Gdx.files.internal(Constants.BACKGROUND_IMAGE_PATH));
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    public void draw(Batch batch, float parentAlpha) {

            batch.draw(texture, 0, 0, srcX, 0, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        }
    public void act(float delta){
        super.act(delta);
        srcX+=1;
    }

    }
