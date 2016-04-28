package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.UserData;
import fi.tamk.tiko.orion.sleeprunner.utilities.Tools;

/**
 *  Superclass of every animated game object.
 *  Not designed to construct object from this class.
 */
public abstract class AnimatedGameObject extends GameObject {

    protected Animation currentAnimation;
    protected TextureRegion currentFrame;
    protected boolean animationPaused;
    protected float stateTime;

    /**
     * Constructor for animated game objects.
     * Creates animation in the construct.
     *
     * @param world             Box2D World
     * @param x                 X-position.
     * @param y                 Y-position.
     * @param width             Width of the body.
     * @param height            Height of the body.
     * @param density           Body's density.
     * @param texture           The texture.
     * @param bodyType          Box2D body's type.
     * @param userData          Box2D body's userdata.
     * @param frameCols         Used for making the animation: Amount of frame columns in the sheet.
     * @param frameRows         Used for making the animation: Amount of frame rows in the sheet.
     * @param start             Used for making the animation: Which frame the animation should start.
     * @param length            Used for making the animation: How many frames there should be.
     * @param fps               Used for making the animation: Frames per second, how fast animation runs.
     * @param animationPaused   Is the animation paused immediatly or not.
     */
    public AnimatedGameObject( World world, float x, float y, float width, float height, float density, Texture texture, BodyDef.BodyType bodyType, UserData userData, int frameCols, int frameRows, int start, int length, float fps, boolean animationPaused  ) {
        super( world, x, y, width, height, density, texture, bodyType, userData );
        this.currentAnimation = Tools.createAnimation( texture, frameCols, frameRows, start, length, fps );
        this.currentFrame = this.currentAnimation.getKeyFrame( stateTime, true );
        if ( animationPaused ) {
            this.pauseAnimation();
        } else {
            this.resumeAnimation();
        }
    }

    /**
     * Constructor for animated game objects.
     * Does not create animation immediatly.
     *
     * @param world             Box2D World
     * @param x                 X-position.
     * @param y                 Y-position.
     * @param width             Width of the body.
     * @param height            Height of the body.
     * @param density           Body's density.
     * @param texture           The texture.
     * @param bodyType          Box2D body's type.
     * @param userData          Box2D body's userdata.
     */
    public AnimatedGameObject( World world, float x, float y, float width, float height, float density, Texture texture, BodyDef.BodyType bodyType, UserData userData ) {
        super( world, x, y, width, height, density, texture, bodyType, userData );
        this.animationPaused = true;
    }

    /**
     * Changes current animation's speed.
     *
     * @param fps Frames per second.
     */
    public void changeFPS( float fps ) {
        currentAnimation.setFrameDuration( fps );
    }

    /**
     * Changes current animation to the given one.
     *
     * @param animation Animation.
     */
    public void changeAnimation( Animation animation ) {
        currentAnimation = animation;
        currentFrame = currentAnimation.getKeyFrame( stateTime, true );
        resumeAnimation();
    }

    /**
     * Pauses current animation from playing.
     */
    public void pauseAnimation( ) {
        animationPaused = true;
        currentFrame = currentAnimation.getKeyFrame(stateTime, true);
    }

    /**
     * Resumes current animation to play again.
     */
    public void resumeAnimation( ) {
        animationPaused = false;
    }

    public void reset( ) {
        super.reset();
        if ( animationPaused ) {
            resumeAnimation();
        }
    }

    public void update(float delta) {
        // Remember to call this or implement this for child classes.
        if ( !animationPaused ) {
            stateTime += delta;
            currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        }
    }

    public void draw( Batch batch) {
        batch.draw(currentFrame,
                body.getPosition().x - currentFrame.getRegionWidth() / 100f / 2,
                body.getPosition().y - currentFrame.getRegionHeight() / 100f / 2,
                currentFrame.getRegionWidth() / 2 / 100f,
                currentFrame.getRegionHeight() / 2 / 100f,
                currentFrame.getRegionWidth() / 100f,
                currentFrame.getRegionHeight() / 100f,
                1.0f,
                1.0f,
                body.getTransform().getRotation() * MathUtils.radiansToDegrees);
    }

}
