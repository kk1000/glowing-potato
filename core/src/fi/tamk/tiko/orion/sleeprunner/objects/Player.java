package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.PlayerUserData;

/**
 * Player actor class.
 * Extended from GameObject.
 */
public class Player extends GameObject {

    private boolean jumping;
    private boolean dodging;
    private boolean hit;

    private PlayerUserData userData;

    private Sound runSound;

    /**
     * Constructor for Player gameobject.
     *
     * @param world     Box2D World
     */
    public Player(World world) {
        super(world,
                Constants.PLAYER_X,
                Constants.PLAYER_Y,
                Constants.PLAYER_WIDTH,
                Constants.PLAYER_HEIGHT,
                Constants.PLAYER_DENSITY,
                new Texture(Gdx.files.internal("running.png")),
                1,
                5,
                1 / 60, BodyDef.BodyType.DynamicBody);
        userData = new PlayerUserData(width, height);
        body.setUserData(userData);
        runSound = Gdx.audio.newSound(Gdx.files.internal(Constants.PLAYER_RUN_SOUND_PATH));
        runSound.stop();
        runSound.play(0.3f);
        Gdx.app.log("Player", "Created player to " + x + ", " + y);
    }

    /**
     * Applies linear impulse to player when it is on ground.
     * Changes jumping state to true.
     */
    public void jump(){
        if(!jumping || dodging || hit){
            body.applyLinearImpulse(userData.getJumpingLinearImpulse(), body.getWorldCenter(), true);
            jumping = true;
        }
        runSound.stop();
    }

    /**
     * Changes player jumping-state to false when player hits ground.
     */
    public void landed(){
        jumping = false;
        runSound.stop();
        runSound.play(0.3f);
    }

    /**
     * Makes player dodge when called.
     * Player can't dodge while jumping.
     */
    public void dodge(){
        if (!jumping || hit){
            body.setTransform(userData.getDodgePosition(), userData.getDodgeAngle());
            dodging = true;
            runSound.stop();
        }
    }

    /**
     * Stops dodging if player isn't hit by enemy or environment.
     */
    public void stopDodge(){
        runSound.stop();
        runSound.play(0.3f);

        dodging = false;
        body.setTransform(userData.getRunningPosition(), 0f);

        if (!hit){
            body.setTransform(userData.getRunningPosition(), 0f);
        }
    }

    /**
     * Checks if player is dodging.
     *
     * @return boolean
     */
    public boolean isDodging(){
        return dodging;
    }

    /**
     * Hit method.
     * Applies angular impulse to the player's body when called.
     */
    public void hit(){
        body.applyAngularImpulse(userData.getHitAngularImpulse(), true);
        hit = true;
        runSound.stop();
    }

    public boolean isHit(){
        return hit;
    }

}
