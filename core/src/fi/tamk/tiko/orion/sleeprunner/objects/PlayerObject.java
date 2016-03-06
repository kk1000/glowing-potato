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

    private Animation dodgeAnimation;
    private Animation jumpAnimation;
    private Animation runAnimation;

    private boolean jumping;
    private boolean dodging;
    private boolean dead;
    private boolean hit;

    private Sound runSound;

    /**
     * Constructor for player object.
     *
     * @param world     Box2D World
     */
    public PlayerObject(World world) {
        super(world, (Constants.WORLD_TO_SCREEN * 2) / 100f, (Constants.WORLD_TO_SCREEN * 1) / 100f,
                Constants.WORLD_TO_SCREEN / 100f, (Constants.WORLD_TO_SCREEN * 2) / 100f,
                Constants.PLAYER_DENSITY,
                new Texture(Gdx.files.internal(Constants.PLAYER_RUNNING_IMAGE_PATH)),
                BodyDef.BodyType.DynamicBody,
                new UserData("PLAYER"));

        body.setFixedRotation(true);

        // Create animations.
        dodgeAnimation = Tools.createAnimation(texture, 6, 3, 1, 4, 1 / 30);
        jumpAnimation = Tools.createAnimation(texture, 6, 3, 13, 6, 1 / 30);
        runAnimation = Tools.createAnimation(texture, 6, 3, 7, 4, 1 / 30);

        currentAnimation = runAnimation;
        currentFrame = currentAnimation.getKeyFrame(stateTime, true);

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
            currentAnimation = jumpAnimation;
            currentFrame = currentAnimation.getKeyFrame(stateTime, true);
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
        currentAnimation = runAnimation;
        currentFrame = currentAnimation.getKeyFrame(stateTime, true);
    }

    /**
     * Makes player dodge when called.
     * PlayerObject can't dodge while jumping.
     */
    public void dodge(){
        if (!jumping || hit){
            body.setTransform(x, y, (float)(-90f * (Math.PI / 180f)));
            currentAnimation = dodgeAnimation;
            currentFrame = currentAnimation.getKeyFrame(stateTime, true);
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
        currentAnimation = jumpAnimation;
        currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        hit = true;
        dead = true;
        runSound.stop();
    }

    @Override
    public void update(float delta) {
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

}