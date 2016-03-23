package fi.tamk.tiko.orion.sleeprunner.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;

/**
 * Class for pause menu
 */
public class PauseMenu extends Actor {

    private Texture texture;

    public PauseMenu (){
        this.texture = new Texture(Gdx.files.internal(Constants.PAUSEMENU_IMAGE_PATH));
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture,Constants.APP_WIDTH/4,-Constants.APP_HEIGHT/4,0,0, Constants.APP_WIDTH, Constants.APP_HEIGHT);
    }

    @Override
    public void act(float delta){
        super.act(delta);
    }

}
