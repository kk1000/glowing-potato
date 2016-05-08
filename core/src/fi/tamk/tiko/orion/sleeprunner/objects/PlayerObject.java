package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;

/**
 * PlayerObject
 */
public class PlayerObject extends AnimatedGameObject {

    private boolean shielded;
    private boolean jumping;
    private boolean dodging;
    private boolean flying;
    private boolean dead;
    private boolean hit;

    private String deadText;
    private float dodgeTimer;

    /**
     * Constructor for player object.
     *
     * @param gameScreen     GameScreen reference.
     * @param world          Box2D World
     */
    public PlayerObject( GameScreen gameScreen, World world) {
        super( gameScreen, world, Constants.PLAYER_START_X, Constants.PLAYER_START_Y,
                Constants.WORLD_TO_SCREEN * 0.85f / 100f, Constants.WORLD_TO_SCREEN * 2 / 100f,
                0.5f,
                gameScreen.getGame().resources.assetManager.get( "graphics/player.png", Texture.class ),
                BodyDef.BodyType.DynamicBody,
                new UserData("PLAYER"));

        body.setFixedRotation(true);

        this.currentAnimation = game.resources.playerRunAnimation;
        this.currentFrame = this.currentAnimation.getKeyFrame( stateTime, true );

        game.resources.playerRunSound.play(prefs.getSoundVolume());
    }

    /**
     * Does the power up functionality.
     *
     * @param powerUpGameObject PowerUpGameObject instance to use.
     */
    public void usePowerUp( PowerUpGameObject powerUpGameObject) {
        powerUpGameObject.action(this);
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
        body.setLinearVelocity(new Vector2(0, 0));
        body.setAngularVelocity(0);
        body.setTransform(new Vector2(Constants.PLAYER_FLY_X, Constants.PLAYER_FLY_Y), body.getAngle());
        body.setGravityScale(0.0f);
        changeAnimation( game.resources.playerFlyAnimation );
        game.resources.playerRunSound.stop();
    }

    /**
     * Stops player's flight.
     */
    public void stopFly() {
        flying = false;
        body.setGravityScale(1.0f);
        jump(100);
        changeAnimation(game.resources.playerRunAnimation);
        game.resources.powerUpFlySound.stop();
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
        game.resources.playerRunSound.stop();
    }

    /**
     * Changes player jumping-state to false when player hits ground.
     */
    public void landed(){
        jumping = false;
        changeFPS(1 / 10f);
        game.resources.playerRunSound.stop();
        game.resources.playerRunSound.play( prefs.getSoundVolume() );
    }

    /**
     * Makes player dodge when called.
     * PlayerObject can't dodge while jumping.
     */
    public void dodge(){
        if (!jumping || hit && !dead){
            body.setTransform(x, y, (float) (90f * (Math.PI / 180f)));
            dodging = true;
            changeAnimation(game.resources.playerDodgeAnimation);
            game.resources.playerRunSound.stop();
        }
    }

    /**
     * Stops dodging if player isn't hit by spikes.
     */
    public void stopDodge(){
        body.setTransform(x, y, 0f);
        dodging = false;
        changeAnimation(game.resources.playerRunAnimation);
        game.resources.playerRunSound.stop();
        game.resources.playerRunSound.play( prefs.getSoundVolume() );
    }

    /**
     * Hit method.
     * Applies angular impulse to the player's body when called.
     */
    public void hit() {
        if ( !shielded ) {
            body.applyAngularImpulse(10f, true);
            pauseAnimation();
            hit = true;
            if (!dead) {
                deadText = "player_death_spikes";
                game.resources.playerDeathSound.play( prefs.getSoundVolume() );
                dead = true;
            }
            game.resources.playerRunSound.stop();
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (dodging) {
            dodgeTimer += delta;
            if (dodgeTimer > 1) {
                stopDodge();
                dodgeTimer = 0;
            }
        }
        if ( !dead ) {
            if (body.getPosition().y < 0 || body.getPosition().x < 0) {
                if ( shielded ) {
                    deadText = "player_death_shielded";
                } else {
                    deadText = "player_death_fall";
                }
                game.resources.playerDeathSound.play( prefs.getSoundVolume() );
                dead = true;
            }
        }
    }

    @Override
    public void draw( Batch batch ) {
        if (shielded) { batch.setColor( Color.BLUE ); }
        batch.draw(currentFrame,
                body.getPosition().x - currentFrame.getRegionWidth() / 100f / 2,
                body.getPosition().y -currentFrame.getRegionHeight() / 100f / 2,
                currentFrame.getRegionWidth() / 2 / 100f,
                currentFrame.getRegionHeight() / 2 / 100f,
                currentFrame.getRegionWidth() / 100f,
                currentFrame.getRegionHeight() / 100f,
                1.0f,
                1.0f,
                body.getTransform().getRotation() * MathUtils.radiansToDegrees);
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
     * @param shielded Is the player shielded by power up.
     */
    public void setShielded( boolean shielded ) { this.shielded = shielded; }

    /**
     * @return Is player shielded by power up.
     */
    public boolean isShielded( ) { return shielded; }

    /**
     * @return Is player dodging.
     */
    public boolean isDodging() {
        return dodging;
    }

    /**
     * @return Is player flying.
     */
    public boolean isFlying( ) {
        return flying;
    }

    /**
     * @return Is player dead.
     */
    public boolean isDead() {
        return dead;
    }

    /**
     * @return Is player hit by spikes.
     */
    public boolean isHit() {
        return hit;
    }

    /**
     * @return Is player jumping.
     */
    public boolean isJumping(){
        return jumping;
    }

    /**
     * @return How player died text.
     */
    public String getDeadText() { return deadText; }

}