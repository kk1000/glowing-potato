package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;

/**
 * Nightmare is following player throughout the whole game.
 */
public class NightmareObject extends GameObject {

    public static final TextureRegion BASE_TEXTURE = new TextureRegion( new Texture( Gdx.files.internal( Constants.NIGHTMARE_BACKGROUND_IMAGE_PATH ) ) );

    /**
     * Constructor.
     *
     * @param world Box2D World
     */
    public NightmareObject( World world ) {
        super(world, Constants.NIGHTMARE_START_X, Constants.NIGHTMARE_START_Y, 800/100f, 400/100f, 0f, BASE_TEXTURE, BodyDef.BodyType.KinematicBody, new UserData("NIGHTMARE"));
    }

    @Override
    public void draw(Batch batch) {
        batch.draw( textureRegion,
                body.getPosition().x - textureWidth/2,
                body.getPosition().y - textureHeight/2,
                textureWidth/2,
                textureHeight/2,
                textureWidth,
                textureHeight,
                1.0f,
                1.0f,
                0);
    }

    @Override
    public void update(float delta) {

    }

}
