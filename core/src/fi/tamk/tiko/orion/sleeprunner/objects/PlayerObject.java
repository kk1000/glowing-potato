package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;

/**
 * PlayerObject actor class.
 * Extended from GameObject.
 */
public class PlayerObject extends GameObject {

    private boolean jumping;
    private boolean dodging;
    private boolean hit;

    private Sound runSound;

    /**
     * Constructor for player object.
     *
     * @param world     Box2D World
     */
    public PlayerObject(World world) {
        super(world, (Constants.WORLD_TO_SCREEN * 2) / 100f, (Constants.WORLD_TO_SCREEN * 4) / 100f,
                Constants.WORLD_TO_SCREEN / 100f, (Constants.WORLD_TO_SCREEN * 2) / 100f,
                Constants.PLAYER_DENSITY,
                new Texture(Gdx.files.internal(Constants.PLAYER_RUNNING_IMAGE_PATH)),
                4, 1, 1 / 10f, BodyDef.BodyType.DynamicBody,
                new UserData("PLAYER"));

        body.setFixedRotation(true);

        //jump = new Texture(Gdx.files.internal(Constants.PLAYER_JUMPING_IMAGE_PATH));
        //dodge = new Texture(Gdx.files.internal(Constants.PLAYER_DODGING_IMAGE_PATH));
        //hitTexture = new TextureRegion(new Texture(Gdx.files.internal(Constants.PLAYER_HIT_IMAGE_PATH)));
        runSound = Gdx.audio.newSound(Gdx.files.internal(Constants.PLAYER_RUN_SOUND_PATH));
        runSound.stop();
        runSound.play(0.3f);
    }

    /**
     * Applies linear impulse to player when it is on ground.
     * Changes jumping state to true.
     */
    public void jump(){
        if(!jumping || dodging || hit){
            body.applyLinearImpulse(Constants.PLAYER_JUMPING_LINEAR_IMPULSE, body.getWorldCenter(), true);
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
     * PlayerObject can't dodge while jumping.
     */
    public void dodge(){
        if (!jumping || hit){
            body.setTransform(Constants.PLAYER_DODGE_X, Constants.PLAYER_DODGE_Y, 90);
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
        body.setTransform(x, y, 0f);

        if (!hit){
            body.setTransform(x, y, 0f);
        }
    }

    /**
     * Hit method.
     * Applies angular impulse to the player's body when called.
     */
    public void hit(){
        body.applyAngularImpulse(Constants.PLAYER_HIT_ANGULAR_IMPULSE, true);
        hit = true;
        runSound.stop();
    }

    public void act(float delta) {
        super.act(delta);
    }

    /**
     * Getters.
     */

    public boolean isDodging() {
        return dodging;
    }
    public boolean isHit() {
        return hit;
    }

}