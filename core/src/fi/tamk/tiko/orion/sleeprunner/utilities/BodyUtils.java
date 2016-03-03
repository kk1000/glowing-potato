package fi.tamk.tiko.orion.sleeprunner.utilities;

import com.badlogic.gdx.physics.box2d.Body;

import fi.tamk.tiko.orion.sleeprunner.data.UserData;
import fi.tamk.tiko.orion.sleeprunner.data.UserDataType;


/**
 * Helper methods for checking a body's datatype (for collisions for example).
 */
public class BodyUtils {

    /**
     * Checks if the body is the player.
     *
     * @param body = body used in game.
     * @return boolean
     */
    public static boolean bodyIsPlayer(Body body){

        UserData userData = (UserData) body.getUserData();

        return userData != null && userData.getUserDataType() == UserDataType.PLAYER;
    }

    /**
     * Checks if the body is ground.
     *
     * @param body = body used in game.
     * @return boolean
     */
    public static boolean bodyIsGround(Body body){

        UserData userData = (UserData) body.getUserData();

        return userData != null && userData.getUserDataType() == UserDataType.GROUND;

    }

    /**
     * Checks if the body is inside the screen.
     * Checks only player and enemy.
     *
     * @param body = body used in game.
     * @return boolean
     */
    public static boolean bodyInBounds(Body body){

        UserData userData = (UserData) body.getUserData();

        switch (userData.getUserDataType()){

            case PLAYER:
            case ENEMY:
                return body.getPosition().x + userData.getWidth()   / 2 > 0 ;

        }

        return true;
    }

    /**
     * Checks if the body is an enemy or obstacle.
     *
     * @param body = body used in game.
     * @return boolean
     */
    public static boolean bodyIsEnemy(Body body){

        UserData userData = (UserData) body.getUserData();

        return userData != null && userData.getUserDataType() == UserDataType.ENEMY;
    }

}
