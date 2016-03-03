package fi.tamk.tiko.orion.sleeprunner.objects;

/**
 * Created by joni on 21/02/2016.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.PlayerUserData;
import fi.tamk.tiko.orion.sleeprunner.objects.GameObject;
import fi.tamk.tiko.orion.sleeprunner.utilities.Tools;

/**
 * Player actor class.
 * Extended from GameObject.
 */

public class Player extends GameObject {

    private float stateTime;

    private boolean jumping;
    private boolean dodging;
    private boolean hit;
    private Animation runAnimation;
    private Texture run;
    private Texture jump;
    private Texture dodge;
    private TextureRegion hitTexture;
    private Sound runSound;

    /**
     * Constructor for Player.
     *
     * @param body = body of player object
     */
    public Player(Body body){
        super(body);
        runSound = Gdx.audio.newSound(Gdx.files.internal(Constants.PLAYER_RUN_SOUND_PATH));
        run = new Texture(Gdx.files.internal(Constants.PLAYER_RUNNING_IMAGE_PATH));
        jump = new Texture(Gdx.files.internal(Constants.PLAYER_JUMPING_IMAGE_PATH));
        dodge = new Texture(Gdx.files.internal(Constants.PLAYER_DODGING_IMAGE_PATH));
        hitTexture = new TextureRegion(new Texture(Gdx.files.internal(Constants.PLAYER_HIT_IMAGE_PATH)));
        stateTime = 0f;
        createRunAnimation();
        runSound.stop();
        runSound.play(0.3f);
    }


    /**
     * @return   player user data
     */
    @Override
    public PlayerUserData getUserData() {
        return (PlayerUserData) userData;
    }

    /**
     * Applies linear impulse to player when it is on ground.
     * Changes jumping state to true.
     */
    public void jump(){

        if(!jumping || dodging || hit){
            body.applyLinearImpulse(getUserData().getJumpingLinearImpulse(),body.getWorldCenter(), true);
            jumping = true;
        }
        runSound.stop();

    }

    /**
     * Creates the animation from player's texture.
     * Uses toTextureArray-method from Tools.
     */

    private void createRunAnimation() {
        final int FRAME_COLS = 5;
        final int FRAME_ROWS = 1;

        int tileWidth = run.getWidth() / FRAME_COLS;
        int tileHeight = run.getHeight() / FRAME_ROWS;

        TextureRegion[][] temp = TextureRegion.split(run, tileWidth,tileHeight);

        TextureRegion[] runFrames = Tools.toTextureArray(temp, FRAME_COLS, FRAME_ROWS);

        runAnimation = new Animation(3 / 60f, runFrames);

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
            body.setTransform(getUserData().getDodgePosition(),getUserData().getDodgeAngle());
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
        body.setTransform(getUserData().getRunningPosition(),0f);

        if (!hit){
            body.setTransform(getUserData().getRunningPosition(),0f);
        }
    }

    /**
     * Checks if player is dodging.
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
        body.applyAngularImpulse(getUserData().getHitAngularImpulse(),true);
        hit = true;
        runSound.stop();
    }

    public boolean isHit(){
        return hit;
    }

    /**
     * Draw method.
     *
     * @param batch = sprite batch used in game
     * @param parentAlpha = global alpha level
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (dodging) {
            batch.draw(dodge, screenRectangle.x, screenRectangle.y + screenRectangle.height / 4, screenRectangle.width,
                    screenRectangle.height / 2);
        } else if (hit) {
            // When he's hit we also want to apply rotation if the body has been rotated
            batch.draw(hitTexture, screenRectangle.x, screenRectangle.y, screenRectangle.width * 0.5f,
                    screenRectangle.height * 0.5f, screenRectangle.width, screenRectangle.height, 1f, 1f,
                    (float) Math.toDegrees(body.getAngle()));
        } else if (jumping) {
            
            batch.draw(jump, screenRectangle.x, screenRectangle.y, screenRectangle.width,
                    screenRectangle.height);
        } else {
            
            // Running
            
            stateTime += Gdx.graphics.getDeltaTime();
            batch.draw(runAnimation.getKeyFrame(stateTime, true), screenRectangle.x, screenRectangle.y,
                    screenRectangle.getWidth(), screenRectangle.getHeight());
        }
    }

}
