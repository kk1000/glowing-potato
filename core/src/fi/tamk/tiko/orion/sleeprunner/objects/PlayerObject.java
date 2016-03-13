package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;
import fi.tamk.tiko.orion.sleeprunner.utilities.Tools;

/**
 * PlayerObject actor class.
 * Extended from GameObject.
 */
public class PlayerObject extends GameObject {

    private Animation runAnimation;

    private boolean jumping;
    private boolean dodging;
    private boolean dead;
    private boolean hit;

    private Sound runSound;
    private float dodgeTimer;

    /**
     * Constructor for player object.
     *
     * @param world     Box2D World
     */
    public PlayerObject(World world) {
        super(world, Constants.WORLD_TO_SCREEN * 2 / 100f, Constants.WORLD_TO_SCREEN * 2 / 100f,
                Constants.WORLD_TO_SCREEN * 0.85f / 100f, Constants.WORLD_TO_SCREEN * 2 / 100f,
                Constants.PLAYER_DENSITY,
                new Texture(Gdx.files.internal(Constants.PLAYER_RUNNING_IMAGE_PATH)),
                BodyDef.BodyType.DynamicBody,
                new UserData("PLAYER"));

        body.setFixedRotation(true);

        runAnimation = Tools.createAnimation(texture, 6, 1, 1, 6, 1 / 8f);

        currentAnimation = runAnimation;

        runSound = Gdx.audio.newSound(Gdx.files.internal(Constants.PLAYER_RUN_SOUND_PATH));
        runSound.stop();
        runSound.play(prefs.getSoundVolume());
    }

    /**
     * Applies linear impulse to player when it is on ground.
     * Changes jumping state to true.
     */
    public void jump(float velY){
        if(!jumping || dodging || hit){
            if(dodging) stopDodge();
            velY *= 0.0001f;
            Constants.PLAYER_JUMPING_LINEAR_IMPULSE.set(0, 0.1f + velY > 0.40f ? 0.40f : 0.1f + velY);
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
        runSound.play(prefs.getSoundVolume());
    }

    /**
     * Makes player dodge when called.
     * PlayerObject can't dodge while jumping.
     */
    public void dodge(){
        if (!jumping || hit){
            body.setTransform(x, y, (float)(-90f * (Math.PI / 180f)));
            dodging = true;
            runSound.stop();
        }
    }

    /**
     * Stops dodging if player isn't hit by enemy or environment.
     */
    public void stopDodge(){
        runSound.stop();
        runSound.play(prefs.getSoundVolume());

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
        dead = true;
        runSound.stop();
    }

    @Override
    public void update(float delta) {

        runSound.setVolume(1,prefs.getSoundVolume());
        if (dodging) {
            dodgeTimer += Gdx.graphics.getDeltaTime();
            if (dodgeTimer > 1) {
                stopDodge();
                dodgeTimer = 0;
            }
        }
        if (body.getPosition().y < 0 || body.getPosition().x < 0) {
            dead = true;
        }
    }

    /**
     * Getters.
     */

    public boolean isDodging() {
        return dodging;
    }

    public boolean isDead() {
        return dead;
    }
    public boolean isHit() {
        return hit;
    }
    public boolean isJumping(){
        return jumping;
    }

}