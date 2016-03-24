package fi.tamk.tiko.orion.sleeprunner.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;

/**
 * Class for pause menu
 */
public class PauseMenu extends Actor {

    private Texture texture;

    public PauseMenu (float x, float y){
        this.texture = new Texture(Gdx.files.internal(Constants.PAUSEMENU_IMAGE_PATH));
        this.setX(x);
        this.setY(y);
    } 

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture,getX(),getY());
    }

    @Override
    public void act(float delta){
        super.act(delta);
    }

}
