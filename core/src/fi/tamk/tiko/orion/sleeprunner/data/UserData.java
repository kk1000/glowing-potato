package fi.tamk.tiko.orion.sleeprunner.data;

import com.badlogic.gdx.math.Vector2;

/**
 * Box2D Body's specific details.
 */
public class UserData {

    private UserDataType userDataType;
    private Vector2 linearVelocity;

    /**
     * Constructor for user data
     *
     * @param userDataType User data's type.
     */
    public UserData(UserDataType userDataType) {
        this.userDataType = userDataType;
    }

    /**
     * @return linear velocity
     */
    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    /**
     * Sets linear velocity used in Box2D physics
     */
    public void setLinearVelocity(Vector2 linearVelocity) {
        this.linearVelocity = linearVelocity;
    }

    /**
     * @return user datatype
     */
    public UserDataType getUserDataType() {
        return userDataType;
    }

    /**
     * UserData tags/ids.
     */
    public enum UserDataType {
        GROUND_DATA, PLAYER_DATA, ENEMY_DATA
    }

}
