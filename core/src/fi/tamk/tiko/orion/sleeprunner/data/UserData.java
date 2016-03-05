package fi.tamk.tiko.orion.sleeprunner.data;

import com.badlogic.gdx.math.Vector2;

/**
 * Box2D Body's specific details.
 */
public class UserData {

    public String id; // PLAYER, GROUND, SPIKES, MIDPOINTBLOCK
    private Vector2 linearVelocity;

    /**
     * Constructor for user data
     *
     * @param id User data's identifier.
     */
    public UserData(String id) {
        this.id = id;
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

}
