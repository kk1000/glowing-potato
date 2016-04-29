package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;
import fi.tamk.tiko.orion.sleeprunner.utilities.Tools;

/**
 * PlayerObject actor class.
 * Extended from GameObject.
 */
public class PlayerObject extends AnimatedGameObject {

    public static Sound DEATH_SOUND = Gdx.audio.newSound( Gdx.files.internal( Constants.PLAYER_DEATH_SOUND ) );

    private Animation runAnimation;
    private Animation dodgeAnimation;
    private Animation flyAnimation;

    private boolean jumping;
    private boolean dodging;
    private boolean flying;
    private boolean dead;
    private boolean hit;

    private String deadText;
    private Sound runSound;
    private float dodgeTimer;

    /**
     * Constructor for player object.
     *
     * @param world     Box2D World
     */
    public PlayerObject(World world) {
        super(world, Constants.PLAYER_START_X, Constants.PLAYER_START_Y,
                Constants.WORLD_TO_SCREEN * 0.85f / 100f, Constants.WORLD_TO_SCREEN * 2 / 100f,
                Constants.PLAYER_DENSITY,
                new Texture(Gdx.files.internal(Constants.PLAYER_RUNNING_IMAGE_PATH)),
                BodyDef.BodyType.DynamicBody,
                new UserData("PLAYER"));

        body.setFixedRotation(true);

        runAnimation = Tools.createAnimation( texture, 11, 1, 1, 8, 1/10f );
        dodgeAnimation = Tools.createAnimation( texture, 11, 1, 9, 2, 1/10f );
        flyAnimation = Tools.createAnimation( texture, 11, 1,11, 1, 1/10f );

        this.currentAnimation = runAnimation;
        this.currentFrame = this.currentAnimation.getKeyFrame( stateTime, true );

        runSound = Gdx.audio.newSound(Gdx.files.internal(Constants.PLAYER_RUN_SOUND_PATH));
        runSound.stop();
        runSound.play(prefs.getSoundVolume());
    }

    /**
     * Make player fly in set position.
     */
    public void fly( ) {
        jumping = false;
        flying = true;
        dodging = false;
        dead = false;
        hit = false;
        changeAnimation( flyAnimation );
        body.setLinearVelocity(new Vector2(0, 0));
        body.setAngularVelocity(0);
        body.setTransform(new Vector2(Constants.PLAYER_FLY_X, Constants.PLAYER_FLY_Y), body.getAngle());
        body.setGravityScale(0.0f);
        runSound.stop();
    }

    /**
     * Stops player's flight.
     */
    public void stopFly() {
        flying = false;
        changeAnimation(runAnimation);
        body.setGravityScale(1.0f);
        jump(100);
    }

    /**
     * Applies linear impulse to player when it is on ground.
     * Changes jumping state to true.
     */
    public void jump(float velY){
        if(!jumping || dodging || hit && !dead){
            if(dodging) stopDodge();
            velY *= 0.0002f;
            Constants.PLAYER_JUMPING_LINEAR_IMPULSE.set(0, 0.1f + velY > 0.50f ? 0.50f : 0.1f + velY);
            body.applyLinearImpulse(Constants.PLAYER_JUMPING_LINEAR_IMPULSE, body.getWorldCenter(), true);
            changeFPS( 1/3f );
            jumping = true;
        }
        runSound.stop();
    }

    /**
     * Changes player jumping-state to false when player hits ground.
     */
    public void landed(){
        jumping = false;
        changeFPS( 1/10f );
        runSound.stop();
        runSound.play(prefs.getSoundVolume());
    }

    /**
     * Makes player dodge when called.
     * PlayerObject can't dodge while jumping.
     */
    public void dodge(){
        if (!jumping || hit && !dead){
            body.setTransform(x, y, (float) (90f * (Math.PI / 180f)));
            dodging = true;
            changeAnimation( dodgeAnimation );
            runSound.stop();
        }
    }

    /**
     * Stops dodging if player isn't hit by spikes.
     */
    public void stopDodge(){
        runSound.stop();
        runSound.play(prefs.getSoundVolume());

        dodging = false;
        changeAnimation( runAnimation );
        //body.setTransform(x, y, 0f);

        if (!hit){
            body.setTransform(x, y, 0f);
        }
    }

    /**
     * Hit method.
     * Applies angular impulse to the player's body when called.
     */
    public void hit(){
        //body.applyAngularImpulse(Constants.PLAYER_HIT_ANGULAR_IMPULSE, true);
        pauseAnimation();
        hit = true;
        if ( !dead ) {
            deadText = "player_death_spikes";
            DEATH_SOUND.play( 0.8f );
            dead = true;
        }
        runSound.stop();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        runSound.setVolume(1, prefs.getSoundVolume());
        if (dodging) {
            dodgeTimer += delta;
            if (dodgeTimer > 1) {
                stopDodge();
                dodgeTimer = 0;
            }
        }
        if ( !dead ) {
            if (body.getPosition().y < 0 || body.getPosition().x < 0) {
                deadText = "player_death_fall";
                DEATH_SOUND.play( 0.8f );
                dead = true;
            }
        }
    }

    @Override
    public void reset( ) {
        super.reset();
        stopDodge();
        stopFly();
        landed();
        dead = false;
        hit = false;
    }

    /**
     * Getters.
     */

    public boolean isDodging() {
        return dodging;
    }

    public boolean isFlying( ) {
        return flying;
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
    public String getDeadText() { return deadText; }

}