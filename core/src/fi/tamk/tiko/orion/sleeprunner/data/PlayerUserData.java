package fi.tamk.tiko.orion.sleeprunner.data;

import com.badlogic.gdx.math.Vector2;

/**
 * User data for player
 */

public class PlayerUserData extends UserData {

    // linear impulse used for jumping in box2d effects

    private Vector2 jumpingLinearImpulse;

    // player position while in normal (running) state

    private final Vector2 runningPosition = new Vector2(Constants.PLAYER_X,Constants.PLAYER_Y);

    // player position while dodging

    private final Vector2 dodgePosition = new Vector2(Constants.PLAYER_DODGE_X, Constants.PLAYER_DODGE_Y);

    /**
     * Constructor for player user data
     *
     * @param width    Width used in rectangle
     * @param height   Height used in rectangle
     */
    public PlayerUserData(float width, float height) {
        super(width,height);
        jumpingLinearImpulse = Constants.PLAYER_JUMPING_LINEAR_IMPULSE;
        userDataType = UserDataType.PLAYER;
    }

    /**
     * @return jumping linear impulse
     */
    public Vector2 getJumpingLinearImpulse(){
        return jumpingLinearImpulse;
    }

    /**
     * Set linear impulse strength
     *
     * @param jumpingLinearImpulse from constructor
     */
    public void setJumpingLinearImpulse(Vector2 jumpingLinearImpulse){
        this.jumpingLinearImpulse = jumpingLinearImpulse;
    }

    /**
     * Angular impulse when player hits an obstacle
     *
     * @return angular impulse from Constants
     */
    public float getHitAngularImpulse(){
        return Constants.PLAYER_HIT_ANGULAR_IMPULSE;
    }

    /**
     *  Angle for player's rectangle when player dodges
     *
     * @return dodge angle
     */
    public float getDodgeAngle(){
        return (float) (-90f * (Math.PI / 180f));
    }

    /**
     * @return running position
     */
    public Vector2 getRunningPosition(){
        return runningPosition;
    }

    /**
     * @return dodging position
     */
    public Vector2 getDodgePosition(){
        return dodgePosition;
    }
}
