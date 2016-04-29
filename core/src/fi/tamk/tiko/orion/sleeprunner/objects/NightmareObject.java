package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;

/**
 * Nightmare is following player throughout the whole game.
 */
public class NightmareObject extends AnimatedGameObject {

    public static final Texture BASE_TEXTURE = new Texture( Gdx.files.internal( Constants.NIGHTMARE_BACKGROUND_IMAGE_PATH ) );
    public static final Sound NIGHTMARE_SOUND = Gdx.audio.newSound( Gdx.files.internal( Constants.NIGHTMARE_SOUND_PATH ) );

    /**
     * Constructor.
     *
     * @param world Box2D World
     */
    public NightmareObject( World world ) {
        super(world, Constants.NIGHTMARE_START_X, Constants.NIGHTMARE_START_Y,
                800/100f, 400/100f, 0f, BASE_TEXTURE, BodyDef.BodyType.KinematicBody, new UserData("NIGHTMARE"),
                4, 1, 1, 4, 1/5f, false );
    }

    /**
     * Moves nightmare forward by player's "death".
     */
    public void moveForward( ) {
        float x = body.getPosition().x;
        float y = body.getPosition().y;
        body.setTransform( x + 0.5f, y, body.getAngle() );
        NIGHTMARE_SOUND.play( 1.0f );
    }

}
