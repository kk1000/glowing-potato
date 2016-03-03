package fi.tamk.tiko.orion.sleeprunner.data;

/**
 * User data for ground objects
 */
public class GroundUserData extends UserData{

    /**
     * Constructor for ground user data
     *
     * @param width    Width used in rectangle
     * @param height   Height used in rectangle
     */
    public GroundUserData(float width, float height) {
        super(width,height);
        userDataType = UserDataType.GROUND;
    }

}
