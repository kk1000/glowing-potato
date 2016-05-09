package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import fi.tamk.tiko.orion.sleeprunner.SleepRunner;
import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.Preference;
import fi.tamk.tiko.orion.sleeprunner.data.Tile;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;

/**
 * Superclass of every game object.
 * Not designed to construct object from this class.
 *
 * @author   Eetu "mehxit" Järvinen
 * @author   Joni "steiner3k" Korpisalo
 */
public abstract class GameObject {

    protected SleepRunner game;
    protected GameScreen gameScreen;
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

    /**
     * Constructor for game objects with texture region.
     *
     * @param gameScreen    GameScreen reference.
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
    public GameObject( GameScreen gameScreen, World world, float x, float y, float width, float height, float density, TextureRegion textureRegion, BodyDef.BodyType bodyType, UserData userData) {
        this.gameScreen = gameScreen;
        this.game = gameScreen.getGame();
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
     * @param gameScreen        GameScreen reference.
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
    public GameObject( GameScreen gameScreen, World world, float x, float y, float width, float height, float density, Texture texture, BodyDef.BodyType bodyType, UserData userData) {
        this.gameScreen = gameScreen;
        this.game = gameScreen.getGame();
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
            gameObjectsTextureRegions = new TextureRegion[]{
                    game.resources.cloudLeft,
                    game.resources.cloudMiddle,
                    game.resources.cloudCenter,
                    game.resources.cloudRight};
        } else {
            // All others.
            gameObjectsTextureRegions = new TextureRegion[]{
                    textureRegion,
                    textureRegion,
                    textureRegion,
                    textureRegion
            };
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
                        textureRegion = gameObjectsTextureRegions[2];
                    } else if (j == pixelWidth - 32) {
                        // Body's last tile.
                        textureRegion = gameObjectsTextureRegions[2];
                    } else {
                        // Body's middle tile.
                        textureRegion = gameObjectsTextureRegions[2];
                    }
                }
                x = (body.getPosition().x - tileSize/2) - (width/2 - tileSize/2) + ( j/100f );
                y = (body.getPosition().y - tileSize/2 ) - (height/2 - tileSize/2) + ( i/100f );
                tiles.add( new Tile( gameScreen, x, y, textureRegion ) );
            }
        }
    }

    /**
     * Draws game object.
     *
     * @param batch Spritebatch.
     */
    public void draw(Batch batch) {
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

    /**
     * Called in every frame.
     *
     * @param delta Delta time.
     */
    public void update(float delta) {
        body.setLinearVelocity(gameScreen.getCurrentGameSpeed(), 0);
    }

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
     * @return Game object's userdata.
     */
    public UserData getUserData() {
        return userData;
    }

    /**
     * @return Game object's texture region.
     */
    public TextureRegion getTextureRegion( ) { return textureRegion; }

    /**
     * @return Game object's initial height.
     */
    public float getHeight( ) { return height; }

    /**
     * @return Game object's initial width.
     */
    public float getWidth() {
        return width;
    }

    /**
     * @return Game object's initial x position.
     */
    public float getX( ) { return x; }

    /**
     * @return Game object''s initial y position.
     */
    public float getY( ) { return y; }

    /**
     * @return Game object's Box2D body.
     */
    public Body getBody() {
        return body;
    }

}