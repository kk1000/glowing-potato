package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;
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

    protected UserData userData;
    protected TextureRegion textureRegion;
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
     * @param userData  Box2D body's userdata.
     */
    public GameObject(World world, float x, float y, float width, float height, float density, TextureRegion textureRegion, BodyDef.BodyType bodyType, UserData userData) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.density = density;
        this.textureRegion = textureRegion;
        this.textureWidth = this.textureRegion.getRegionWidth() / 100f;
        this.textureHeight = this.textureRegion.getRegionHeight() / 100f;
        this.width = width;
        this.height = height;
        this.userData = userData;
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
     * @param userData  Box2D body's userdata.
     */
    public GameObject(World world, float x, float y, float width, float height, float density, Texture texture, int frameCols, int frameRows, float fps, BodyDef.BodyType bodyType, UserData userData) {
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
        this.userData = userData;
        hasAnimation = true;
        createAnimation();
        createBody(bodyType);
    }

    /**
     * Creates Box2D body to the world.
     */
    protected void createBody(BodyDef.BodyType bodyType) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(new Vector2(this.x, this.y));

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(this.width / 2, this.height / 2);

        Body body = this.world.createBody(bodyDef);

        // Body's creation differs by game objects.
        if (userData.id.equals("PLAYER")) {
            // Dynamic body has more specific details.
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.restitution = 0.005f;
            fixtureDef.friction = 0.0f;
            fixtureDef.density = this.density;
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        } else if (userData.id.equals("GROUND")) {
            body.createFixture(shape, this.density);
        } else if (userData.id.equals("MIDPOINTBLOCK")) {
            // MidPointBlockObject is a sensor.
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.isSensor = true;
            body.createFixture(fixtureDef);
        }

        body.resetMassData();
        body.setUserData(userData);

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
    public void draw(Batch batch, float parentAlpha) {
        if (hasAnimation) {
            stateTime += Gdx.graphics.getDeltaTime();
            currentFrame = animation.getKeyFrame(stateTime, true);
            batch.draw(currentFrame,
                    body.getPosition().x - currentFrame.getRegionWidth() / 100f / 2,
                    body.getPosition().y - currentFrame.getRegionHeight() / 100f / 2,
                    Constants.WORLD_TO_SCREEN / 2,
                    Constants.WORLD_TO_SCREEN / 2,
                    currentFrame.getRegionWidth() / 100f,
                    currentFrame.getRegionHeight() / 100f,
                    1.0f,
                    1.0f,
                    body.getTransform().getRotation() * MathUtils.radiansToDegrees);
        } else {
            float tileSize = Constants.WORLD_TO_SCREEN / 100f;
            for (int i = 0; i < width * 100f; i += 32) {
                float x = (body.getPosition().x - tileSize / 2) - (width / 2 - tileSize / 2) + i / 100f;
                float y = body.getPosition().y - tileSize / 2;
                batch.draw(textureRegion,
                        x,
                        y,
                        Constants.WORLD_TO_SCREEN / 2,
                        Constants.WORLD_TO_SCREEN / 2,
                        tileSize,
                        tileSize,
                        1.0f,
                        1.0f,
                        0);
            }
        }
    }

    /**
     * Getters.
     */

    public UserData getUserData() {
        return userData;
    }

    public float getWidth() {
        return width;
    }

    public Body getBody() {
        return body;
    }

}