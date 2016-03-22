package fi.tamk.tiko.orion.sleeprunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
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
import fi.tamk.tiko.orion.sleeprunner.objects.PlayerObject;
import fi.tamk.tiko.orion.sleeprunner.stages.BackgroundStage;
import fi.tamk.tiko.orion.sleeprunner.utilities.BodyUtils;
import fi.tamk.tiko.orion.sleeprunner.utilities.MapChunk;

/**
 * Screen for the game.
 * Uses different stages for the game.
 */

public class GameScreen extends InputAdapter implements Screen, ContactListener {

    private final float TIME_STEP = 1 / 300f;

    private SleepRunner game;


    private Box2DDebugRenderer debugRenderer;

    private OrthographicCamera backgroundCamera;
    private OrthographicCamera gameCamera;
    private OrthographicCamera uiCamera;

    private BackgroundStage backgroundStage;

    private Rectangle screenRightSide;
    private Rectangle screenLeftSide;

    private Vector3 touchPoint;

    private GestureDetector gd;

    private Array<MapChunk> removalMapChunks = new Array<MapChunk>();
    private Array<MapChunk> mapChunks = new Array<MapChunk>();
    private int currentMapChunk = 0;
    private PlayerObject player;
    private World world;

    private BitmapFont scoreFont;
    private BitmapFont debugFont;
    private SpriteBatch batch;

    private float deathTimer = 2;
    private float accumulator = 0f;

    private float score = 0;

    private int playTimes;

    /**
     * Constructor for GameScreen.
     *
     * @param sleepRunner Reference to SleepRunner.
     */
    public GameScreen(SleepRunner sleepRunner) {
        game = sleepRunner;

        gd = new GestureDetector(new GestureListener());

        batch = game.getBatch();

        debugRenderer = new Box2DDebugRenderer();

        backgroundCamera = new OrthographicCamera();
        backgroundCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundStage = new BackgroundStage(game,backgroundCamera,batch);

        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Constants.APP_WIDTH, Constants.APP_HEIGHT);

        scoreFont = new BitmapFont(Gdx.files.internal(Constants.GAME_FONT_PATH));
        debugFont = new BitmapFont();

        setupWorld();
        setupTouchControlAreas();
    }

    /**
     * Creates rectangles for both halves of the screen used for controls.
     */
    private void setupTouchControlAreas() {
        touchPoint = new Vector3();
        // TODO: Fix this?
        screenRightSide = new Rectangle(Constants.APP_WIDTH / 2f, 0f, Constants.APP_WIDTH / 2f, Constants.APP_HEIGHT);
        screenLeftSide = new Rectangle(0, 0, Constants.APP_WIDTH / 2, Constants.APP_HEIGHT);
    }

    /**
     * Listener for desktop debugging.
     */
    private void desktopListener() {
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            player.jump(5000);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN))
            player.dodge();
    }

    /**
     * Setups all objects used in game.
     */
    private void setupWorld() {
        world = new World(Constants.WORLD_GRAVITY, true);
        world.setContactListener(this);

        createMapChunks();
        currentMapChunk++;

        player = new PlayerObject(world);
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        translateScreenToWorldCoordinates(x, y);

        if (rightSideTouched(touchPoint.x, touchPoint.y)) {

        } else if (leftSideTouched(touchPoint.x, touchPoint.y)) {

        }
        return super.touchDown(x, y, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (player.isDodging()) {
            player.stopDodge();
        }

        return super.touchUp(screenX, screenY, pointer, button);
    }

    /**
     * Checks if right side of screen is touched.
     *
     * @param x X-position.
     * @param y Y-position.
     * @return boolean
     */
    private boolean rightSideTouched(float x, float y) {
        return screenRightSide.contains(x, y);
    }

    /**
     * Checks if left side of screen is touched.
     *
     * @param x X-position.
     * @param y Y-position.
     * @return boolean
     */
    private boolean leftSideTouched(float x, float y) {
        return screenLeftSide.contains(x, y);
    }

    /**
     * Translates the x and y position of touch to world coordinates.
     *
     * @param x  X-position of touch
     * @param y  Y-position of touch
     */
    private void translateScreenToWorldCoordinates(int x, int y) {
        // TODO: Is this broken?
        uiCamera.unproject(touchPoint.set(x, y, 0));
    }

    /**
     * Restarts the game to start new one.
     */
    public void restartGame( ) {
        Gdx.app.log("GameScreen", "Restarting the game.");
        // Reset chunks.
        for ( MapChunk mapChunk : mapChunks ) {
            mapChunk.destroy();
            removalMapChunks.add( mapChunk );
        }
        removeRemovalMapChunks();
        player = new PlayerObject( world );
        // Reset attributes.
        currentMapChunk = 0;
        accumulator = 0f;
        deathTimer = 2;
        score = 0;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(gd);
        if ( playTimes > 0 ) {
            restartGame();
        }
        playTimes++;
    }

    @Override
    public void render(float delta) {
        // Clear screen.
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw background.
        batch.setProjectionMatrix(backgroundCamera.combined);
        backgroundStage.act(delta);
        backgroundStage.draw();

        batch.begin();

        // Draw game objects.
        batch.setProjectionMatrix(gameCamera.combined);
        updateMapChunks();
        updatePlayer(delta);

        // Draw UI
        batch.setProjectionMatrix(uiCamera.combined);
        score += delta * 10;
        scoreFont.draw( batch, "Score:" + (int) score, Constants.WORLD_TO_SCREEN, Constants.APP_HEIGHT - 10);

        // Debug details.
        debugFont.draw( batch, "Current map chunk: " + currentMapChunk, Constants.APP_WIDTH - 200, Constants.APP_HEIGHT - 10 );
        debugFont.draw( batch, "Map chunks: " + mapChunks.size, Constants.APP_WIDTH - 200, Constants.APP_HEIGHT - 30 );
        debugFont.draw( batch, "Play times: " + playTimes, Constants.APP_WIDTH - 200, Constants.APP_HEIGHT - 50 );

        debugFont.draw( batch, "Body count: " + world.getBodyCount(), Constants.APP_WIDTH - 200, Constants.APP_HEIGHT - 80 );

        debugFont.draw( batch, "Player X " + player.getBody().getPosition().x, Constants.APP_WIDTH - 200, Constants.APP_HEIGHT - 100 );
        debugFont.draw( batch, "Player Y " + player.getBody().getPosition().y, Constants.APP_WIDTH - 200, Constants.APP_HEIGHT - 120 );

        batch.end();

        debugRenderer.render(world, gameCamera.combined);

        desktopListener();
        doPhysicsStep(delta);
    }

    /**
     * Handles player in every frame.
     *
     * @param delta The delta time.
     */
    public void updatePlayer(float delta) {
        player.update(delta);
        player.draw(batch);

        // Check for death.
        if (player.isDead()) {
            deathTimer -= delta;
        }
        if (deathTimer <= 0) {
            game.setMainMenuScreen();
        }
    }

    /**
     * Creates new chunks if needed.
     */
    public void createMapChunks( ) {
        if ( mapChunks.size == 0 ) {
            // There are no map chunks set, set couple to start the world.
            for (int i = 0; i < 2; i++) {
                MapChunk mapChunk;
                if (i == 0) {
                    // First map chunk is only ground.
                    mapChunk = new MapChunk(world, mapChunks.size, 0, 0, Constants.CHUNK_MAX_TILES_WIDTH, Constants.CHUNK_MAX_TILES_WIDTH);
                } else {
                    mapChunk = new MapChunk(world, mapChunks.size, 2, 4, 2, 10);
                }
                mapChunks.add(mapChunk);
            }
        } else if ( mapChunks.size == 1 ) {
            // There is need for one new map chunk.
            mapChunks.add( new MapChunk( world, mapChunks.size, 2, 4, 2, 10 ) );
            // Player has gone through one map chunk.
            currentMapChunk++;
        }
    }

    /**
     * Handles map chunks in every frame.
     * This also moves and draws the map chunks game objects.
     */
    public void updateMapChunks() {
        removeRemovalMapChunks();
        createMapChunks();

        // Update map chunks.
        for (MapChunk mapChunk : mapChunks) {
            mapChunk.update(batch);
        }

        // Check for empty map chunks, they will be removed.
        for (MapChunk mapChunk : mapChunks) {
            if (mapChunk.isEmpty()) {
                removalMapChunks.add(mapChunk);
            }
        }
    }

    /**
     * Removes map chunks which are on the queue.
     */
    public void removeRemovalMapChunks( ) {
        for (MapChunk mapChunk : removalMapChunks) {
            mapChunks.removeValue(mapChunk, true);
        }
        removalMapChunks.clear();
    }

    /**
     * Create fixed timestep for world step.
     *
     * @param delta Delta time.
     */
    private void doPhysicsStep(float delta) {
        float frameTime = delta;

        // If it took ages (over 4 fps, then use 4 fps)
        // Avoid of "spiral of death"
        if (delta > 1 / 4f) {
            frameTime = 1 / 4f;
        }

        accumulator += frameTime;

        while (accumulator >= TIME_STEP) {
            // It's a fixed time step!
            world.step(TIME_STEP, 8, 3);
            accumulator -= TIME_STEP;
        }
    }



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

    @Override
    public void beginContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        if ((BodyUtils.bodyIsPlayer(a) && BodyUtils.bodyIsSpikes(b)) || (BodyUtils.bodyIsPlayer(b) && BodyUtils.bodyIsSpikes(a))) {
            player.hit();
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

    private class GestureListener extends GestureDetector.GestureAdapter {

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            if (velocityY < -1) {
                player.dodge();
            }
            if (velocityY > 1) {
                player.jump(velocityY);
            }
            return true;
        }
    }

}
