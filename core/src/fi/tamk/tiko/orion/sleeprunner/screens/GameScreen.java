package fi.tamk.tiko.orion.sleeprunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import fi.tamk.tiko.orion.sleeprunner.SleepRunner;
import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.objects.GameObject;
import fi.tamk.tiko.orion.sleeprunner.objects.Player;
import fi.tamk.tiko.orion.sleeprunner.utilities.BodyUtils;
import fi.tamk.tiko.orion.sleeprunner.utilities.MapGenerator;
import fi.tamk.tiko.orion.sleeprunner.utilities.WorldUtilities;

/**
 * Screen for the game.
 * Uses different stages for the game.
 */

public class GameScreen implements Screen, ContactListener {

    private final float TIME_STEP = 1 / 300f;
    private SleepRunner game;
    private SpriteBatch batch;
    private World world;
    private Player player;
    private float accumulator = 0f;

    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;

    private Array<GameObject> gameObjects = new Array<GameObject>();

    /**
     * Constructor for the game stage.
     *
     * @param g Game created from the SleepRunner main class
     */
    public GameScreen(SleepRunner g) {
        debugRenderer = new Box2DDebugRenderer();
        game = g;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

        batch = game.getBatch();
        setupWorld();
    }

    /**
     * Setups all objects used in game.
     */
    private void setupWorld() {
        world = new World(Constants.WORLD_GRAVITY, true);
        world.setContactListener(this);

        int[][] chunkGrid = MapGenerator.createIntervalMapChunkGrid();
        // Create ground.
        WorldUtilities.createObjectsToBodies(gameObjects, world, MapGenerator.generateObjects(chunkGrid, Constants.GROUND_BLOCK, "ground-object"));
        player = new Player(world);
    }

    @Override
    public void show() {

    }

    /**
     * Render method.
     *
     * @param delta Delta timer (1/60)
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Draw game objects.
        drawGameObjects();
        player.draw(batch);

        moveGameObjects();

        // Fixed timestep
        accumulator += delta;

        while (accumulator >= delta) {
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }

        batch.end();

        debugRenderer.render(world, camera.combined);
    }

    /**
     * Draws every game objects (except player).
     */
    public void drawGameObjects() {
        for (GameObject gameObject : gameObjects) {
            gameObject.draw(batch);
        }
    }

    /**
     * Moves every game object (except player)
     * to create moving illusion.
     */
    public void moveGameObjects() {
        for (GameObject gameObject : gameObjects) {
            Body body = gameObject.getBody();
            body.setLinearVelocity(new Vector2(-0.5f, 0));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    /**
     * Begins the contact-event.
     *
     * @param contact
     */
    @Override
    public void beginContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        if ((BodyUtils.bodyIsPlayer(a) && BodyUtils.bodyIsEnemy(b)) || (BodyUtils.bodyIsPlayer(b) && BodyUtils.bodyIsEnemy(a))) {
            player.hit();
            //Constants.ENEMY_LINEAR_VELOCITY.set(-enemyMove,0);
        } else if ((BodyUtils.bodyIsPlayer(a) && BodyUtils.bodyIsGround(b)) || (BodyUtils.bodyIsGround(a) && BodyUtils.bodyIsPlayer(b))) {
            player.landed();
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

}