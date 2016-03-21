package fi.tamk.tiko.orion.sleeprunner.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;

import fi.tamk.tiko.orion.sleeprunner.data.UserData;
import fi.tamk.tiko.orion.sleeprunner.objects.GameObject;


/**
 * Helper methods for checking a body's datatype (for collisions for example).
 */
public class BodyUtils {

    /**
     * Checks if the body is the player.
     *
     * @param body Body used in game.
     * @return boolean
     */
    public static boolean bodyIsPlayer(Body body){
        UserData userData = (UserData) body.getUserData();
        return userData != null && userData.id.equals("PLAYER");
    }

    /**
     * Checks if the body is ground.
     *
     * @param body  Body used in game.
     * @return boolean
     */
    public static boolean bodyIsGround(Body body){
        UserData userData = (UserData) body.getUserData();
        return userData != null && userData.id.equals("GROUND");
    }

    /**
     * Checks if the body is a spikes.
     *
     * @param body Body used in game.
     * @return boolean
     */
    public static boolean bodyIsSpikes(Body body) {
        UserData userData = (UserData) body.getUserData();
        return userData != null && userData.id.equals("SPIKES");
    }

    /**
     * Is the given game object passed the player already.
     *
     * @param gameObject The game object.
     * @return boolean
     */
    public static boolean gameObjectPassed( GameObject gameObject ) {
        float x = gameObject.getBody().getPosition().x + gameObject.getWidth()/2;
        return ( x <= 0 );
    }

}
