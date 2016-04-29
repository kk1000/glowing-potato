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
import com.badlogic.gdx.utils.Array;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.Preference;
import fi.tamk.tiko.orion.sleeprunner.data.Tile;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;

/**
 * Superclass of every game object.
 * Not designed to construct object from this class.
 */
public abstract class GameObject {

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

    protected Array<Tile> tiles = new Array<Tile>();

    protected Preference prefs;

    protected boolean hidden = false;

    /**
     * Constructor for game objects with texture region.
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
        createTiles();
        prefs = new Preference();
    }

    /**
     * Constructor for game objects with texture.
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
        createBody(bodyType);
        prefs = new Preference();
    }

    /**
     * Creates Box2D body to the world.
     */
    protected void createBody(BodyDef.BodyType bodyType) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(new Vector2( this.x, this.y ) );

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
        } else {
            body.createFixture(shape, this.density);
        }

        body.resetMassData();
        body.setUserData(userData);

        shape.dispose();
        this.body = body;
    }

    /**
     * Creates game object's tiles (textures).
     */
    public void createTiles() {
        TextureRegion[] gameObjectsTextureRegions;
        if (userData.id.equals("GROUND")) {
            gameObjectsTextureRegions = new TextureRegion[]{GroundObject.LEFT_TEXTURE, GroundObject.MIDDLETOP_TEXTURE, GroundObject.MIDDLE_TEXTURE, GroundObject.RIGHT_TEXTURE};
        } else {
            // All others.
            gameObjectsTextureRegions = new TextureRegion[]{textureRegion, textureRegion, textureRegion, textureRegion};
        }
        float tileSize = Constants.WORLD_TO_SCREEN / 100f;
        int pixelHeight = (int) (height * 100f);
        int pixelWidth = (int) (width * 100f);
        float x;
        float y;
        for (int i = 0; i < pixelHeight; i += 32) {
            for ( int j = 0; j < pixelWidth; j += 32 ) {
                TextureRegion textureRegion;
                if ( i >= pixelHeight - 32 ) {
                    // One of the game object's top tile.
                    if (j == 0 && pixelWidth == 32) {
                        // Body is just one tile length.
                        textureRegion = gameObjectsTextureRegions[1];
                    } else if (j == 0 && pixelWidth > 32) {
                        // Body's first tile and it's larger than one tile length.
                        textureRegion = gameObjectsTextureRegions[0];
                    } else if (j == pixelWidth - 32) {
                        // Body's last tile.
                        textureRegion = gameObjectsTextureRegions[3];
                    } else {
                        // Body's middle tile.
                        textureRegion = gameObjectsTextureRegions[1];
                    }
                } else {
                    // Not one of the game object's top tile.
                    if ( j == 0 && pixelWidth == 32 ) {
                        // Body is just one tile length.
                        textureRegion = gameObjectsTextureRegions[2];
                    } else if (j == 0 && pixelWidth > 32) {
                        // Body's first tile and it's larger than one tile length.
                        textureRegion = gameObjectsTextureRegions[0];
                    } else if (j == pixelWidth - 32) {
                        // Body's last tile.
                        textureRegion = gameObjectsTextureRegions[3];
                    } else {
                        // Body's middle tile.
                        textureRegion = gameObjectsTextureRegions[2];
                    }
                }
                x = (body.getPosition().x - tileSize/2) - (width/2 - tileSize/2) + ( j/100f );
                y = (body.getPosition().y - tileSize/2 ) - (height/2 - tileSize/2) + ( i/100f );
                tiles.add( new Tile( x, y, textureRegion ) );
            }
        }
    }

    /**
     * Draws game object.
     *
     * @param batch Spritebatch.
     */
    public void draw(Batch batch) {
        if ( !hidden ) {
            for (Tile tile : tiles) {
                batch.draw(tile.textureRegion,
                        tile.x,
                        tile.y,
                        Constants.WORLD_TO_SCREEN / 2,
                        Constants.WORLD_TO_SCREEN / 2,
                        Constants.WORLD_TO_SCREEN / 100f,
                        Constants.WORLD_TO_SCREEN / 100f,
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
     * Updates game object's tile positions.
     *
     * @param delta Delta time.
     */
    public void updateTiles( float delta ) {
        for ( Tile tile : tiles ) {
            tile.update(delta);
        }
    }

    /**
     * Resets game object.
     */
    public void reset( ) {
        Gdx.app.log( "GameObject", "Resetting, " + userData.id );
        body.setTransform(new Vector2(this.x, this.y), body.getAngle());
        body.setLinearVelocity( new Vector2( 0, 0 ) );
        body.setAngularVelocity( 0 );
    }

    /**
     * Destroys game object.
     */
    public void dispose() {
        userData = null;
        body.setUserData(null);
        world.destroyBody(body);
    }

    /**
     * Setters.
     */

    public void setHidden( boolean hidden ) { this.hidden = hidden; }

    /**
     * Getters.
     */

    public UserData getUserData() {
        return userData;
    }

    public float getHeight( ) { return height; }
    public float getWidth() {
        return width;
    }

    public float getX( ) { return x; }
    public float getY( ) { return y; }

    public Body getBody() {
        return body;
    }

}