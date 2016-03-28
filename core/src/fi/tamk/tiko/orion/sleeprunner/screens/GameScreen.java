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
import fi.tamk.tiko.orion.sleeprunner.stages.PauseStage;
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
    private PauseStage pauseStage;

    private Rectangle screenRightSide;
    private Rectangle screenLeftSide;

    private Vector3 touchPoint;

    private GestureDetector gestureDetector;

    private Array<MapChunk> removalMapChunks = new Array<MapChunk>();
    private Array<MapChunk> mapChunks = new Array<MapChunk>();
    private MapChunk currentMapChunk;
    private PlayerObject player;
    private World world;

    private BitmapFont scoreFont;
    private BitmapFont debugFont;
    private SpriteBatch batch;

    private float deathTimer = 2;
    private float accumulator = 0f;

    private float score = 0;

    private int playTimes;
    private int gameState;

    /**
     * Constructor for GameScreen.
     *
     * @param sleepRunner Reference to SleepRunner.
     */
    public GameScreen(SleepRunner sleepRunner) {
        game = sleepRunner;

        gestureDetector = new GestureDetector(new GestureListener());

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

        pauseStage = new PauseStage(game,uiCamera,batch, scoreFont );

        setupWorld();
        setupTouchControlAreas();

        gameState = Constants.GAME_READY;
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

        Gdx.input.setInputProcessor(gestureDetector);
        
        // Reset chunks.
        for ( MapChunk mapChunk : mapChunks ) {
            mapChunk.destroy();
            removalMapChunks.add( mapChunk );
        }
        removeRemovalMapChunks();
        createMapChunks();
        player = new PlayerObject( world );
        // Reset attributes.
        accumulator = 0f;
        deathTimer = 2;
        score = 0;
        gameState = Constants.GAME_READY;
    }

    /**
     * Updates game on every frame according current game state.
     *
     * @param delta The delta time.
     */
    public void update( float delta ) {
        // Update different game states.
        switch ( gameState ) {
            case Constants.GAME_READY:
                // Character menu?
                updateGameReady();
                break;
            case Constants.GAME_RUNNING:
                updateGameRunning( delta );
                break;
            case Constants.GAME_PAUSED:
                updateGamePaused();
                break;
            case Constants.GAME_OVER:
                updateGameOver();
                break;
        }
    }

    /**
     * Updates when game is ready.
     */
    private void updateGameReady( ) {
        if ( Gdx.input.isTouched() ) {
            gameState = Constants.GAME_RUNNING;
        }
    }

    /**
     * Updates when game is running.
     *
     * @param delta The delta time.
     */
    private void updateGameRunning( float delta) {
        desktopListener();
        backgroundStage.act(delta);
        updateMapChunks(delta);
        updatePlayer(delta);
        score += delta * 10;
        doPhysicsStep(delta);
    }

    /**
     * Updates when game is paused.
     */
    private void updateGamePaused( ) {
        if ( Gdx.input.isTouched() ) {
            gameState = Constants.GAME_RUNNING;
        }
    }

    /**
     *  Runs when game is over.
     */
    private void updateGameOver( ) {
        Gdx.input.setInputProcessor(pauseStage);
        pauseStage.act();
    }

    /**
     * Handles player in every frame.
     *
     * @param delta The delta time.
     */
    private void updatePlayer(float delta) {
        player.update(delta);

        // Check for death.
        if (player.isDead()) {
            deathTimer -= delta;
        }

        if (deathTimer <= 0) {
            gameState = Constants.GAME_OVER;
        }
    }

    /**
     * Updates map chunks (and its game objects) in every frame.
     *
     * @param delta The delta time.
     */
    private void updateMapChunks( float delta ) {
        removeRemovalMapChunks();
        createMapChunks();

        // Update map chunks.
        for (MapChunk mapChunk : mapChunks) {
            mapChunk.update( delta );
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
    private void removeRemovalMapChunks( ) {
        for (MapChunk mapChunk : removalMapChunks) {
            mapChunks.removeValue(mapChunk, true);
        }
        removalMapChunks.clear();
    }

    /**
     * Creates new chunks if needed.
     */
    private void createMapChunks( ) {
        if ( mapChunks.size == 0 ) {
            // There are no map chunks set, set couple to start the world.
            for (int i = 0; i < 2; i++) {
                MapChunk mapChunk;
                if ( i == 0 ) {
                    mapChunk = new MapChunk( null, world, 0, ( i + 1 ) );
                } else {
                    mapChunk = new MapChunk( mapChunks.get( i - 1 ), world, mapChunks.size, ( i + 1 ) );
                }
                mapChunks.add( mapChunk );
            }
            currentMapChunk = mapChunks.first();
        } else if ( mapChunks.size == 1 ) {
            // There is need for one new map chunk.
            int nextChunkNumber = currentMapChunk.getChunkNumber() + 2;
            currentMapChunk = mapChunks.first();
            mapChunks.add(new MapChunk( currentMapChunk, world, mapChunks.size, nextChunkNumber));
        }
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

    /**
     * Draws game elements on every frame according to game state.
     */
    public void draw() {
        // Clear screen.
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // TODO: Draw player without animation if game is not running.
        // TODO: Same for backgrounds!

        // Draw background.
        batch.setProjectionMatrix(backgroundCamera.combined);
        backgroundStage.draw();

        batch.begin();

        // Draw game objects.
        batch.setProjectionMatrix(gameCamera.combined);
        drawMapChunks();


        // Draw UI
        batch.setProjectionMatrix(uiCamera.combined);
        scoreFont.draw(batch, "Score:" + (int) score, Constants.WORLD_TO_SCREEN, Constants.APP_HEIGHT - 10);

        // Debug details.
        if ( Constants.DEBUG ) {
            debugFont.draw(batch, "Chunk number: " + currentMapChunk.getChunkNumber(), Constants.APP_WIDTH - 150, Constants.APP_HEIGHT - 10);
            debugFont.draw(batch, "Max Ground: " + currentMapChunk.getMaxGroundBlocks(), Constants.APP_WIDTH - 150, Constants.APP_HEIGHT - 30);
            debugFont.draw(batch, "Min Ground: " + currentMapChunk.getMinGroundBlocks(), Constants.APP_WIDTH - 150, Constants.APP_HEIGHT - 50);
            debugFont.draw(batch, "Change per chunk: " + Constants.DIFFICULTY_CHANGE_INTERVAL, Constants.APP_WIDTH - 150, Constants.APP_HEIGHT - 70);

            debugFont.draw(batch, "Body count: " + world.getBodyCount(), Constants.APP_WIDTH - 150, Constants.APP_HEIGHT - 100);

            debugFont.draw(batch, "Player X " + player.getBody().getPosition().x, Constants.APP_WIDTH - 150, Constants.APP_HEIGHT - 130);
            debugFont.draw(batch, "Player Y " + player.getBody().getPosition().y, Constants.APP_WIDTH - 150, Constants.APP_HEIGHT - 150);

            debugFont.draw(batch, "Play times: " + playTimes, Constants.APP_WIDTH - 150, Constants.APP_HEIGHT - 180);
        }

        // Draw different game states.
        switch ( gameState ) {
            case Constants.GAME_READY:
                // Character menu?
                drawGameReady();
                break;
            case Constants.GAME_RUNNING:
                drawGameRunning();
                break;
            case Constants.GAME_PAUSED:
                drawGamePaused();
                break;
            case Constants.GAME_OVER:
                drawGameOver();
                break;
        }

        batch.end();

        debugRenderer.render(world, gameCamera.combined);

    }

    /**
     * Draws when game is ready.
     */
    private void drawGameReady( ) {
        scoreFont.draw( batch, "Game is ready!", Constants.APP_WIDTH/2, Constants.APP_HEIGHT/2 );
    }

    /**
     * Draws when game is running.
     */
    private void drawGameRunning( ) {
        batch.setProjectionMatrix(gameCamera.combined);
        player.draw(batch);
    }

    /**
     * Draws when game is paused.
     */
    private void drawGamePaused() {
        scoreFont.draw(batch, "Game is paused!", Constants.APP_WIDTH / 2, Constants.APP_HEIGHT / 2);
    }

    /**
     * Draws when game is over.
     */
    private void drawGameOver() {
        batch.end();
        pauseStage.draw();
        batch.begin();
    }

    /**
     * Draws map chunk's game objects.
     */
    public void drawMapChunks( ) {
        for ( MapChunk mapChunk : mapChunks ) {
            mapChunk.draw( batch );
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(gestureDetector);
        if ( playTimes > 0 ) {
            restartGame();
        }
        playTimes++;
    }

    @Override
    public void render(float delta) {
        update( delta );
        draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        if ( gameState == Constants.GAME_RUNNING ) {
            gameState = Constants.GAME_PAUSED;
        }
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
            Gdx.app.log( "GameScreen", "Hit spikes!" );
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
