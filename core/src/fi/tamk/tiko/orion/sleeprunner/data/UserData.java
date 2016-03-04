package fi.tamk.tiko.orion.sleeprunner.data;

import com.badlogic.gdx.math.Vector2;

/**
 * Super class for defining user data.
 * Not designed to construct object from this class.
 */
public abstract class UserData {

    protected UserDataType userDataType;
    protected float width;
    protected float height;
    private Vector2 linearVelocity;

    /**
     * Constructor for user data
     *
     * @param width   Width used in rectangle
     * @param height  Height used in rectangle
     */
    public UserData(float width, float height){
        this.width = width;
        this.height = height;
    }

    /**
     * @return width
     */
    public float getWidth(){
        return width;
    }

    /**
     * sets width
     */
    public void setWidth(float width){
        this.width = width;
    }

    /**
     * @return height
     */
    public float getHeight() {
        return height;
    }

    /**
     * sets height
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * @return linear velocity
     */
    public Vector2 getLinearVelocity(){
        return linearVelocity;
    }

    /**
     * sets linear velocity used in box2d physics
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

}
