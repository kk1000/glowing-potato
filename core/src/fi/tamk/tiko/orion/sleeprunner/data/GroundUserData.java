package fi.tamk.tiko.orion.sleeprunner.data;

import com.badlogic.gdx.math.Vector2;

/**
 * User data for ground objects
 */
public class GroundUserData extends UserData{

    private Vector2 linearVelocity;
    private String texturePath;

    /**
     * Constructor for ground user data
     *
     * @param width    Width used in rectangle
     * @param height   Height used in rectangle
     */
    public GroundUserData(float width, float height) {
        super(width,height);
        this.texturePath = Constants.GROUND_IMAGE_PATH;
        userDataType = UserDataType.GROUND;
        linearVelocity = Constants.ENEMY_LINEAR_VELOCITY;
    }

    /**
     * @return linear velocity
     */
    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    /**
     * Sets linear velocity used in box2d physics
     */
    public void setLinearVelocity(Vector2 linearVelocity) {
        this.linearVelocity = linearVelocity;
    }

    /**
     * @return texture imagepath
     */
    public String getTexturepath(){
        return texturePath;
    }

}
