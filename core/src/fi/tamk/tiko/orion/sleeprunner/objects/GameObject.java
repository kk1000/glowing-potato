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

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;
import fi.tamk.tiko.orion.sleeprunner.utilities.MapGenerator;

/**
 * Superclass of every game object.
 * Not designed to construct object from this class.
 */
public abstract class GameObject {

    protected int[][] mapChunkGrid;
    protected World world;

    protected float x;
    protected float y;
    protected float density;
    protected float width;
    protected float height;

    protected Texture texture;

    protected UserData userData;
    protected TextureRegion textureRegion;
    protected Body body;
    protected float textureWidth;
    protected float textureHeight;

    protected boolean hasAnimation = false;
    protected Animation currentAnimation; // THIS NEEDS TO MANUALLY SET ATM.
    protected TextureRegion currentFrame;
    protected float stateTime;

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
     * @param userData      Box2D body's userdata.
     * @param mapChunkGrid  Map chunk's grid. Used to calculate right tile to body.
     */
    public GameObject(World world, float x, float y, float width, float height, float density, TextureRegion textureRegion, BodyDef.BodyType bodyType, UserData userData, int[][] mapChunkGrid) {
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
        this.mapChunkGrid = mapChunkGrid;
        createBody(bodyType);
    }

    /**
     * Constructor for animated game objects.
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
    public GameObject(World world, float x, float y, float width, float height, float density, Texture texture, BodyDef.BodyType bodyType, UserData userData) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.density = density;
        this.texture = texture;
        this.textureWidth = this.texture.getWidth() / 100f;
        this.textureHeight = this.texture.getHeight() / 100f;
        this.width = width;
        this.height = height;
        this.userData = userData;
        this.mapChunkGrid = mapChunkGrid;
        hasAnimation = true;

        // IMPORTANT!
        // Remember to set animations and current frame!
        // There should be AnimatedGameObject (TODO)
        // to properly handle animated game objects and their animations.

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
            fixtureDef.restitution = 0.0f;
            fixtureDef.friction = 0.0f;
            fixtureDef.density = this.density;
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        } else if (userData.id.equals("GROUND")) {
            body.createFixture(shape, this.density);
        }

        body.resetMassData();
        body.setUserData(userData);

        shape.dispose();
        this.body = body;
    }

    /**
     * Draws game object.
     *
     * @param batch Spritebatch.
     */
    public void draw(Batch batch) {
        if (hasAnimation) {
            stateTime += Gdx.graphics.getDeltaTime();
            currentFrame = currentAnimation.getKeyFrame(stateTime, true);
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
                // If we're drawing ground...
                TextureRegion tile = MapGenerator.calculateGroundTile(mapChunkGrid, i / 32, 0);
                float x = (body.getPosition().x - tileSize / 2) - (width / 2 - tileSize / 2) + i / 100f;
                float y = body.getPosition().y - tileSize / 2;
                batch.draw(tile,
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
     * Called in every frame, every game object needs to own one.
     *
     * @param delta Delta time.
     */
    public abstract void update(float delta);

    /**
     * Destroys game object.
     */
    public void dispose() {
        userData = null;
        body.setUserData(null);
        world.destroyBody(body);
        Gdx.app.log("GameObject", "Deleted body, there are total of " + world.getBodyCount());
    }

    /**
     * Setters.
     */

    // This is moved in the future.
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
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