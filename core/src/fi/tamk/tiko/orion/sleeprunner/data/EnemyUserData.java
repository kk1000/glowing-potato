package fi.tamk.tiko.orion.sleeprunner.data;

import com.badlogic.gdx.math.Vector2;

/**
 * Data used for enemy and obstacles
 */
public class EnemyUserData extends UserData{

    private Vector2 linearVelocity;
    private String texturepath;

    /**
     * Constructor for enemy user data
     * @param width = width used in rectangle
     * @param height = height used in rectangle
     * @param path = texture imagepath
     */
    public EnemyUserData(float width, float height, String path){

        super(width, height);
        userDataType = UserDataType.ENEMY;
        linearVelocity = Constants.ENEMY_LINEAR_VELOCITY;
        this.texturepath = path;

    }
    /**
     * sets linear velocity used in box2d physics
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
    public String getTexturepath(){
        return texturepath;
    }
}