package fi.tamk.tiko.orion.sleeprunner.data;

import com.badlogic.gdx.math.Vector2;

/**
 * Data used for enemy and obstacles
 */
public class EnemyUserData extends UserData{

    private Vector2 linearVelocity;

    /**
     * Constructor for enemy user data
     *
     * @param width   Width used in rectangle
     * @param height  Height used in rectangle
     */
    public EnemyUserData(float width, float height) {
        super(width, height);
        userDataType = UserDataType.ENEMY;
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

}