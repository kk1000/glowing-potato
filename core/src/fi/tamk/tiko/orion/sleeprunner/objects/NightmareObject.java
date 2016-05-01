package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;

/**
 * Nightmare is following player throughout the whole game.
 */
public class NightmareObject extends AnimatedGameObject {

    /**
     * Constructor.
     *
     * @param gameScreen     GameScreen reference.
     * @param world          Box2D World
     */
    public NightmareObject( GameScreen gameScreen, World world ) {
        super( gameScreen, world, Constants.NIGHTMARE_START_X, Constants.NIGHTMARE_START_Y,
                800/100f, 400/100f, 0f,
                gameScreen.getGame().resources.assetManager.get( "graphics/tileset_signs.png", Texture.class ),
                BodyDef.BodyType.KinematicBody,
                new UserData("NIGHTMARE"),
                gameScreen.getGame().resources.nightmareAnimation,
                false );
    }

    /**
     * Moves nightmare forward by player's "death".
     */
    public void moveForward( ) {
        float x = body.getPosition().x;
        float y = body.getPosition().y;
        body.setTransform( x + 0.5f, y, body.getAngle() );
        game.resources.nightmareReachSound.play(prefs.getSoundVolume());
    }

    /**
     * Moves nightmare backwards by using power up.
     */
    public void moveBack( ) {
        float x = body.getPosition().x;
        float y = body.getPosition().y;
        if ( ( x - 0.5f ) <= Constants.NIGHTMARE_START_X ) {
            body.setTransform( Constants.NIGHTMARE_START_X, y, body.getAngle() );
        } else {
            body.setTransform( x - 0.5f, y, body.getAngle() );
        }
        game.resources.nightmareBackSound.play( prefs.getSoundVolume() );
    }

}
