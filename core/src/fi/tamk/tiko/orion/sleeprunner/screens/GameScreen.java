package fi.tamk.tiko.orion.sleeprunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
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
import fi.tamk.tiko.orion.sleeprunner.data.Preference;
import fi.tamk.tiko.orion.sleeprunner.objects.GameObject;
import fi.tamk.tiko.orion.sleeprunner.objects.NightmareObject;
import fi.tamk.tiko.orion.sleeprunner.objects.PlayerObject;
import fi.tamk.tiko.orion.sleeprunner.objects.SignObject;
import fi.tamk.tiko.orion.sleeprunner.stages.BackgroundStage;
import fi.tamk.tiko.orion.sleeprunner.stages.PauseStage;
import fi.tamk.tiko.orion.sleeprunner.stages.UIStage;
import fi.tamk.tiko.orion.sleeprunner.utilities.BodyUtils;
import fi.tamk.tiko.orion.sleeprunner.utilities.MapChunk;

/**
 * Screen for the gameplay.
 */
public class GameScreen extends InputAdapter implements Screen, ContactListener {

    public static Vector2 CURRENT_GAME_SPEED = Constants.INITIAL_GAME_SPEED;

    private final float TIME_STEP = 1 / 300f;

    private SleepRunner game;

    private Box2DDebugRenderer debugRenderer;

    private OrthographicCamera backgroundCamera;
    private OrthographicCamera gameCamera;
    private OrthographicCamera uiCamera;

    private Preference prefs;

    private BackgroundStage backgroundStage;
    private PauseStage pauseStage;
    private UIStage uiStage;

    private Vector3 touchPoint;

    private GestureDetector gestureDetector;
    private InputMultiplexer im;
    private Array<MapChunk> removalMapChunks = new Array<MapChunk>();
    private Array<MapChunk> mapChunks = new Array<MapChunk>();
    private MapChunk currentMapChunk;
    private NightmareObject nightmare;
    private PlayerObject player;
    private World world;

    private BitmapFont titleFont;
    private BitmapFont textFont;
    private BitmapFont debugFont;

    private SpriteBatch batch;

    private boolean setupReady = false;

    private boolean highscoreSaved = false;
    private float deathTimer = 0;
    private float accumulator = 0f;

    private int playTimes;
    private int previousGameState;
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

        prefs = new Preference();

        debugRenderer = new Box2DDebugRenderer();

        backgroundCamera = new OrthographicCamera();
        backgroundCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundStage = new BackgroundStage(game,backgroundCamera,batch);

        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Constants.APP_WIDTH, Constants.APP_HEIGHT);

        titleFont = sleepRunner.getTitleFont();
        textFont = sleepRunner.getTextFont();
        debugFont = sleepRunner.getDebugFont();

        pauseStage = new PauseStage(game,uiCamera,batch);

        setupWorld();
        touchPoint = new Vector3();

        uiStage = new UIStage(game, uiCamera, debugFont, batch);

        im = new InputMultiplexer();
        im.addProcessor(gestureDetector);
        im.addProcessor(uiStage);
        im.addProcessor(this);

        Gdx.input.setInputProcessor(im);
        setGameState(Constants.GAME_READY );
    }

    /**
     * Listener for desktop debugging.
     */
    private void desktopListener() {
        if ( !player.isFlying() && gameState == Constants.GAME_RUNNING ) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                player.jump(5000);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN))
                player.dodge();
        }
    }

    /**
     * Setups all objects used in game.
     */
    private void setupWorld() {
        world = new World(Constants.WORLD_GRAVITY, true);
        world.setContactListener(this);

        createMapChunks();

        nightmare = new NightmareObject(world);
        player = new PlayerObject(world);
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        if ( gameState == Constants.GAME_RUNNING ) {
            touchPoint.set(x / 100f, Constants.WORLD_HEIGHT - y / 100f, 0);
            // Check is the sign game object touched.
            for (MapChunk mapChunk : mapChunks) {
                Array<GameObject> mapChunkGameObjects = mapChunk.getGameObjects();
                for (GameObject mapChunkGameObject : mapChunkGameObjects) {
                    if (mapChunkGameObject instanceof SignObject) {
                        SignObject signObject = (SignObject) mapChunkGameObject;
                        if (!signObject.isClicked()) {
                            float signHeight = mapChunkGameObject.getHeight();
                            float signWidth = mapChunkGameObject.getWidth();
                            float signX = mapChunkGameObject.getBody().getPosition().x - signWidth / 2;
                            float signY = mapChunkGameObject.getBody().getPosition().y - signHeight / 2;
                            if ((touchPoint.x <= ((signX + signWidth) * 1.3f) && touchPoint.x >= (signX * 0.7f)) &&
                                    (touchPoint.y <= ((signY + signHeight) * 1.3f) && touchPoint.y >= (signY * 0.7f))) {
                                // Touch position is inside the sign object.
                                setGameState(Constants.GAME_INFO_SCREEN);
                                signObject.click();
                            }
                        }
                    }
                }
            }
        }
        return super.touchDown(x, y, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if ( gameState == Constants.GAME_RUNNING ) {
            if (player.isDodging()) {
                player.stopDodge();
            }
        }
        return super.touchUp(screenX, screenY, pointer, button);
    }

    /**
     * Restarts the game to start new one.
     */
    public void restartGame( ) {
        Gdx.app.log("GameScreen", "Restarting the game.");
        Gdx.input.setInputProcessor(im);
        // Reset chunks.
        for ( MapChunk mapChunk : mapChunks ) {
            mapChunk.destroy();
            removalMapChunks.add( mapChunk );
        }
        removeRemovalMapChunks();
        createMapChunks();
        nightmare.reset();
        player.reset();
        uiStage.reset();
        // Reset attributes.
        accumulator = 0f;
        deathTimer = 2;
        highscoreSaved = false;
        setGameState( Constants.GAME_READY );
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
            case Constants.GAME_PLAYER_DEATH:
                updateGameRunning( delta );
                updateGamePlayerDeath();
                break;
            case Constants.GAME_PAUSED:
                updateGamePaused();
                break;
            case Constants.GAME_OVER:
                updateGameOver();
                break;
            case Constants.GAME_INFO_SCREEN:
                updateGameInfoScreen();
                break;
        }
    }

    /**
     * Updates when game is ready.
     */
    private void updateGameReady( ) {
        if(!setupReady){
            uiStage.setupUiStage();
            setupReady = true;
        }
        if ( Gdx.input.isTouched() ) {
            setGameState(Constants.GAME_RUNNING);
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
        nightmare.update(delta);
        uiStage.act(delta);
        doPhysicsStep(delta);
    }

    /**
     * Updates when player has died but game is running.
     */
    private void updateGamePlayerDeath( ) {
        if ( Gdx.input.isTouched() ) {
            setGameState(Constants.GAME_RUNNING);
        }
    }

    /**
     * Updates when game is paused.
     */
    private void updateGamePaused( ) {
        Gdx.input.setInputProcessor(pauseStage);
        pauseStage.act();
    }

    /**
     *  Runs when game is over.
     */
    private void updateGameOver( ) {
        Gdx.input.setInputProcessor(pauseStage);
        if(!highscoreSaved) {
            prefs.putHighscore(uiStage.getUiText().getScore());
            highscoreSaved = true;
        }
        pauseStage.act();
    }
    /**
     * Runs when info screen is on.
     */
    private void updateGameInfoScreen(){
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
            // Is it TOTAL Game over or just player death?
            if ( uiStage.hasNightmareReached() ) {
                setGameState( Constants.GAME_OVER );
                pauseStage.setupMenu();
            } else {
                setGameState(Constants.GAME_PLAYER_DEATH);
            }
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
                    mapChunk = new MapChunk( this, null, world, 0, ( i + 1 ) );
                } else {
                    mapChunk = new MapChunk( this, mapChunks.get( i - 1 ), world, mapChunks.size, ( i + 1 ) );
                }
                mapChunks.add( mapChunk );
            }
            currentMapChunk = mapChunks.first();
        } else if ( mapChunks.size == 1 ) {
            // There is need for one new map chunk.
            int nextChunkNumber = currentMapChunk.getChunkNumber() + 2;
            currentMapChunk = mapChunks.first();
            mapChunks.add(new MapChunk( this, currentMapChunk, world, mapChunks.size, nextChunkNumber));
            // Update game speed every 3 map chunk if the game is not in player death state.
            if ( currentMapChunk.getChunkNumber() % 3 == 0 && gameState != Constants.GAME_PLAYER_DEATH ) {
                CURRENT_GAME_SPEED = CURRENT_GAME_SPEED.add( -0.2f, 0 );
                backgroundStage.increaseSpeed();
                Gdx.app.log( "GameScreen", "Current speed: " + CURRENT_GAME_SPEED.x );
            }
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

        // Draw background.
        batch.setProjectionMatrix(backgroundCamera.combined);
        backgroundStage.draw();

        batch.begin();

        // Draw game objects.
        batch.setProjectionMatrix(gameCamera.combined);
        nightmare.draw(batch);
        drawMapChunks();
        player.draw(batch);

        // Draw UI.
        batch.setProjectionMatrix(uiCamera.combined);
        batch.end();
        uiStage.draw();
        batch.begin();

        // Draw different game states.
        switch ( gameState ) {
            case Constants.GAME_READY:
                // Character menu?
                drawGameReady();
                break;
            case Constants.GAME_RUNNING:
                drawGameRunning();
                break;
            case Constants.GAME_PLAYER_DEATH:
                drawGameRunning();
                drawGamePlayerDeath();
                break;
            case Constants.GAME_PAUSED:
                drawGamePaused();
                break;
            case Constants.GAME_OVER:
                drawGameOver();
                break;
            case Constants.GAME_INFO_SCREEN:
                drawGameGameInfoScreen();
                break;
        }

        batch.end();

        if ( Constants.DEBUG ) {
            debugRenderer.render(world, gameCamera.combined);
        }

    }

    /**
     * Draws when game is ready.
     */
    private void drawGameReady( ) {
        titleFont.draw(batch, "Tap anywhere to start!", Constants.APP_WIDTH / 2 - 200, Constants.APP_HEIGHT / 2);
    }

    /**
     * Draws when game is running.
     */
    private void drawGameRunning( ) {
    }

    /**
     * Draws when player has died and game is running.
     */
    private void drawGamePlayerDeath( ) {
        titleFont.draw(batch, game.translate.get(player.getDeadText()), Constants.APP_WIDTH / 2 - 200, Constants.APP_HEIGHT - 200);
        titleFont.draw( batch, game.translate.get( "death_info" ), Constants.APP_WIDTH/2-200, Constants.APP_HEIGHT - 240 );
        titleFont.draw( batch, game.translate.get( "death_info2" ), Constants.APP_WIDTH/2-200, Constants.APP_HEIGHT - 280 );
    }

    /**
     * Draws when game is paused.
     */
    private void drawGamePaused() {
        batch.end();
        pauseStage.draw();
        batch.begin();
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
     * Draws when info screen is on.
     */
    private void drawGameGameInfoScreen() {
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
        Gdx.input.setInputProcessor(im);
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
        setGameState(Constants.GAME_PAUSED);

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        debugRenderer.dispose();
        backgroundStage.dispose();
        pauseStage.dispose();
        uiStage.dispose();
        world.dispose();
        nightmare.dispose();
        player.dispose();
    }

    @Override
    public void beginContact(Contact contact) {
        if ( !player.isFlying() ) {
            Body a = contact.getFixtureA().getBody();
            Body b = contact.getFixtureB().getBody();

            // Player to spikes collision check.
            if ((BodyUtils.bodyHasID(a, "PLAYER") && BodyUtils.bodyHasID(b, "SPIKES")) ||
                    (BodyUtils.bodyHasID(b, "PLAYER") && BodyUtils.bodyHasID(a, "SPIKES"))) {
                player.hit();
            }

            // Player to flying spikes collision check.
            if ((BodyUtils.bodyHasID(a, "PLAYER") && BodyUtils.bodyHasID(b, "FLYING_SPIKES")) ||
                    (BodyUtils.bodyHasID(b, "PLAYER") && BodyUtils.bodyHasID(a, "FLYING_SPIKES"))) {
                player.hit();
            }

            // Player to ground collision check.
            if ((BodyUtils.bodyHasID(a, "PLAYER") && BodyUtils.bodyHasID(b, "GROUND")) ||
                    (BodyUtils.bodyHasID(b, "PLAYER") && BodyUtils.bodyHasID(a, "GROUND"))) {
                player.landed();
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        // If the collision is sign or nightmare, ignore it!
        if ( (BodyUtils.bodyHasID(a, "PLAYER") && BodyUtils.bodyHasID(b, "SIGN")) ||
                (BodyUtils.bodyHasID(b, "PLAYER") && BodyUtils.bodyHasID(a, "SIGN")) ||
                (BodyUtils.bodyHasID(a, "PLAYER") && BodyUtils.bodyHasID(b, "NIGHTMARE")) ||
                (BodyUtils.bodyHasID(b, "PLAYER") && BodyUtils.bodyHasID(a, "NIGHTMARE")) ) {
            contact.setEnabled(false);
        }

        // Shield power up collision.
        if ( (BodyUtils.bodyHasID(a, "PLAYER") && BodyUtils.bodyHasID(b, "POWERUP_SHIELD")) ||
                (BodyUtils.bodyHasID(b, "PLAYER") && BodyUtils.bodyHasID(a, "POWERUP_SHIELD")) ) {
            contact.setEnabled(false);
            if ( !player.isFlying() ) {
                currentMapChunk.clearGameObject(uiStage, "POWERUP_SHIELD");
            }
        }

        // Fly power up collision.
        if ( (BodyUtils.bodyHasID(a, "PLAYER") && BodyUtils.bodyHasID(b, "POWERUP_FLY")) ||
                (BodyUtils.bodyHasID(b, "PLAYER") && BodyUtils.bodyHasID(a, "POWERUP_FLY")) ) {
            contact.setEnabled(false);
            if ( !player.isFlying() ) {
                currentMapChunk.clearGameObject(uiStage, "POWERUP_FLY");
            }
        }

        // Mask power up collision.
        if ( (BodyUtils.bodyHasID(a, "PLAYER") && BodyUtils.bodyHasID(b, "POWERUP_MASK")) ||
                (BodyUtils.bodyHasID(b, "PLAYER") && BodyUtils.bodyHasID(a, "POWERUP_MASK")) ) {
            contact.setEnabled(false);
            if ( !player.isFlying() ) {
                currentMapChunk.clearGameObject(uiStage, "POWERUP_MASK");
            }
        }

        // Player to flying spikes collision check.
        if ((BodyUtils.bodyHasID(a, "PLAYER") && BodyUtils.bodyHasID(b, "FLYING_SPIKES")) ||
                (BodyUtils.bodyHasID(b, "PLAYER") && BodyUtils.bodyHasID(a, "FLYING_SPIKES")) &&
                player.isFlying() ) {
            contact.setEnabled( false );
        }

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private class GestureListener extends GestureDetector.GestureAdapter {

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            if ( !player.isFlying() && gameState == Constants.GAME_RUNNING ) {
                if (velocityY > 1) {
                    player.dodge();
                }
                if (velocityY < -1) {
                    player.jump(Math.abs(velocityY));
                }
            }
            return true;
        }
    }

    /**
     * Setters.
     */

    /**
     * Sets gamestate, and setup its logic.
     */
    public void setGameState(int state) {
        previousGameState = gameState;
        gameState = state;
        switch ( gameState ) {
            case Constants.GAME_RUNNING:
                if ( previousGameState == Constants.GAME_PLAYER_DEATH ) {
                    // Changing to GAME_RUNNING state from GAME_PLAYER_DEATH state.
                    player.stopFly();
                    CURRENT_GAME_SPEED = Constants.INITIAL_GAME_SPEED;
                    backgroundStage.resetSpeed();
                    nightmare.moveForward();
                    uiStage.moveNightmareMeter();
                }
                nightmare.resumeAnimation();
                player.resumeAnimation();
                break;
            case Constants.GAME_PAUSED:
                nightmare.pauseAnimation();
                player.pauseAnimation();
                pauseStage.setupMenu();
                break;
            case Constants.GAME_PLAYER_DEATH:
                CURRENT_GAME_SPEED = Constants.PLAYER_DEATH_GAME_SPEED;
                player.fly();
                break;
            case Constants.GAME_INFO_SCREEN:
                pauseStage.setupMenu();
                nightmare.pauseAnimation();
                player.pauseAnimation();
                break;
        }
    }

    public void setInputProcessor(int input){
        if(input == 1){
            Gdx.input.setInputProcessor(im);
        }
        if(input == 2){
            Gdx.input.setInputProcessor(pauseStage);
        }
    }

    /**
     * Getters.
     */

    public int getPreviousGameState( ) { return previousGameState; }
    public MapChunk getCurrentMapChunk( ) { return currentMapChunk; }
    public PauseStage getPauseStage( ) { return pauseStage; }
    public NightmareObject getNightmare( ) { return nightmare; }
    public PlayerObject getPlayer( ) { return player; }
    public World getWorld( ) { return world; }
    public int getGameState(){ return gameState; }
}
