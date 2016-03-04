package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;

import fi.tamk.tiko.orion.sleeprunner.data.GroundUserData;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;

/**
 * Ground actor class.
 * Ground texture is split to two textureregions for the endless movement visual effect.
 * Extended from GameObject.
 */
public class Ground extends GameObject {


    public Texture texture;
    private String path;

    public Ground(Body body){
        super(body);
        path = getUserData().getTexturepath();
        texture = new Texture(Gdx.files.internal(path));
    }

    @Override
    public UserData getUserData() {
        return (GroundUserData) userData;
    }

    @Override

    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(texture, (screenRectangle.x - (screenRectangle.width * 0.1f)),
                screenRectangle.y, screenRectangle.width * 1f, screenRectangle.height * 1f);
    }
    @Override
    public void act(float delta){
        super.act(delta);
        body.setLinearVelocity(getUserData().getLinearVelocity());
    }
}