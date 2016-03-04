package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.utilities.Tools;

/**
 * Superclass of every game object.
 * Not designed to construct object from this class.
 */
public abstract class GameObject extends Actor {

    protected World world;
    protected float x;
    protected float y;
    protected float density;
    protected float width;
    protected float height;

    protected TextureRegion textureRegion;
    protected Rectangle screenRectangle;
    protected Body body;
    protected float textureWidth;
    protected float textureHeight;

    protected boolean hasAnimation = false; // Default false.
    protected Texture texture;
    protected TextureRegion currentFrame;
    protected Animation animation;
    protected int frameCols;
    protected int frameRows;
    protected float stateTime;
    protected float fps;

    /**
     * Constructor for game objects which got no animation.
     *
     * @param world         Box2D World
     * @param x             X-position.
     * @param y             Y-position.
     * @param width         Width of the body.
     * @param height        Height of the body.
     * @param density       Body's density.
     * @param textureRegion The texture.
     * @param bodyType      Box2D body's type.
     */
    public GameObject(World world, float x, float y, float width, float height, float density, TextureRegion textureRegion, BodyDef.BodyType bodyType) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.density = density;
        this.textureRegion = textureRegion;
        this.textureWidth = this.textureRegion.getRegionWidth() / 100f;
        this.textureHeight = this.textureRegion.getRegionHeight() / 100f;
        this.width = width;
        this.height = height;
        screenRectangle = new Rectangle();
        createBody(bodyType);
    }

    /**
     * Constructor for animated game objects.
     *
     * @param world     Box2D World
     * @param x         X-position.
     * @param y         Y-position.
     * @param width     Width of the body.
     * @param height    Height of the body.
     * @param density   Body's density.
     * @param texture   The texture.
     * @param frameCols Amount of frames in a column.
     * @param frameRows Amount of frames in a row.
     * @param fps       Animation's speed (frame per second)
     * @param bodyType  Box2D body's type.
     */
    public GameObject(World world, float x, float y, float width, float height, float density, Texture texture, int frameCols, int frameRows, float fps, BodyDef.BodyType bodyType) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.density = density;
        this.texture = texture;
        this.frameCols = frameCols;
        this.frameRows = frameRows;
        this.fps = fps;
        this.textureWidth = this.texture.getWidth() / 100f;
        this.textureHeight = this.texture.getHeight() / 100f;
        this.width = width;
        this.height = height;
        screenRectangle = new Rectangle();
        hasAnimation = true;
        createAnimation();
        createBody(bodyType);
    }

    /**
     * Act method, updates body's rectangle if it's not null (outside of screen).
     * Removes the body if null.
     *
     * @param delta Delta timer (1/60)
     */
    @Override
    public void act(float delta) {
        super.act(delta);
    }

    /**
     * Creates Box2D body to the world.
     */
    protected void createBody(BodyDef.BodyType bodyType) {
        // Create game object body.
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(new Vector2(this.x, this.y));

        PolygonShape shape = new PolygonShape();

        shape.setAsBox(this.width, this.height);

        Body body = this.world.createBody(bodyDef);
        body.createFixture(shape, this.density);
        body.resetMassData();

        // Remember to set body's userdata!
        // This is pretty badly designed...

        shape.dispose();
        this.body = body;
    }

    /**
     * Creates the animation from the texture.
     * Uses toTextureArray-method from Tools.
     */
    public void createAnimation() {
        int tileWidth = texture.getWidth() / this.frameCols;
        int tileHeight = texture.getHeight() / this.frameRows;

        TextureRegion[][] temp = TextureRegion.split(texture, tileWidth, tileHeight);
        TextureRegion[] frames = Tools.toTextureArray(temp, this.frameCols, this.frameRows);

        animation = new Animation(this.fps, frames);
        currentFrame = animation.getKeyFrame(stateTime, true);
    }

    /**
     * Draws game object.
     */
    public void draw(Batch batch) {
        if (hasAnimation) {
            stateTime += Gdx.graphics.getDeltaTime();
            currentFrame = animation.getKeyFrame(stateTime, true);
            batch.draw(currentFrame.getTexture(),
                    body.getPosition().x - width / 2,
                    body.getPosition().y - height / 2,
                    width / 2,
                    height / 2,
                    width,
                    height,
                    1.0f,
                    1.0f,
                    body.getTransform().getRotation() * MathUtils.radiansToDegrees,
                    0,
                    0,
                    currentFrame.getRegionWidth(),
                    currentFrame.getRegionHeight(),
                    false,
                    false);
        } else {
            float tileSize = Constants.WORLD_TO_SCREEN / 100f;
            batch.draw(textureRegion,
                    body.getPosition().x - tileSize / 2,
                    body.getPosition().y - tileSize / 2,
                    Constants.WORLD_TO_SCREEN / 2,
                    Constants.WORLD_TO_SCREEN / 2,
                    tileSize,
                    tileSize,
                    1.0f,
                    1.0f,
                    0);
        }
    }

    /**
     * Getters.
     */

    public Body getBody() {
        return body;
    }

}