package fi.tamk.tiko.orion.sleeprunner.data;

/**
 * Box2D Body's specific details.
 */
public class UserData {

    public String id; // PLAYER, GROUND, SPIKES

    /**
     * Constructor for user data
     *
     * @param id User data's identifier.
     */
    public UserData(String id) {
        this.id = id;
    }

}
