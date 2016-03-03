package fi.tamk.tiko.orion.sleeprunner.data;

import com.badlogic.gdx.math.Vector2;

/**
 * Data used for enemy and obstacles
 */
public class EnemyUserData extends UserData{

    private Vector2 linearVelocity;
    private String texturePath;

    /**
     * Constructor for enemy user data
     *
     * @param width   Width used in rectangle
     * @param height  Height used in rectangle
     * @param path    Texture imagepath
     */
    public EnemyUserData(float width, float height, String path) {
        super(width, height);
        this.texturePath = path;
        userDataType = UserDataType.ENEMY;
        linearVelocity = Constants.ENEMY_LINEAR_VELOCITY;
    }

    /**
     * Sets linear velocity used in box2d physics
     */
    public void setLinearVelocity(Vector2 linearVelocity){
        this.linearVelocity = linearVelocity;
    }

    /**
     * @return linear velocity
     */
    public Vector2 getLinearVelocity(){
        return linearVelocity;
    }

    /**
     * @return texture imagepath
     */
    public String getTexturePath(){
        return texturePath;
    }
}