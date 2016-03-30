package fi.tamk.tiko.orion.sleeprunner.utilities;

import com.badlogic.gdx.physics.box2d.Body;

import fi.tamk.tiko.orion.sleeprunner.data.UserData;
import fi.tamk.tiko.orion.sleeprunner.objects.GameObject;


/**
 * Helper methods for checking a body's datatype (for collisions for example).
 */
public class BodyUtils {

    /**
     * Checks if the body has given id in its userdata.
     *
     * @param body      Body used in game.
     * @param id        UserData ID to check.
     * @return          Boolean.
     */
    public static boolean bodyHasID( Body body, String id ) {
        UserData userData = (UserData) body.getUserData();
        return userData != null && userData.id.equals( id );
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
