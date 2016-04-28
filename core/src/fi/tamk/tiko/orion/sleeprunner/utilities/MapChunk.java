package fi.tamk.tiko.orion.sleeprunner.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.objects.GameObject;
import fi.tamk.tiko.orion.sleeprunner.objects.PowerUpGameObject;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;
import fi.tamk.tiko.orion.sleeprunner.stages.UIStage;

/**
 * Contains one map chunks game objects and information.
 */
public class MapChunk {

    private GameScreen gameScreen;
    private MapChunk previousMapChunk;

    private Array<GameObject> removalGameObjects = new Array<GameObject>();
    private Array<GameObject> gameObjects = new Array<GameObject>();

    private int[][] grid;

    private World world;

    private int maxGroundBlocks = Constants.START_MAX_GROUND_BLOCKS;
    private int minGroundBlocks = Constants.START_MIN_GROUND_BLOCKS;
    private int maxEmptyBlocks = Constants.START_MAX_EMPTY_BLOCKS;
    private int minEmptyBlocks = Constants.START_MIN_EMPTY_BLOCKS;

    private boolean canContainPowerup;
    private boolean canContainFlyingObstacle;
    private String sleepStage;
    private int chunkNumber;
    private int position;

    /**
     * Constructor for MapChunk.
     *
     * @param gameScreen       Reference to GameScreen.
     * @param previousMapChunk Previous map chunk. Note that its game objects are deleted.
     * @param world            The Box2D game world.
     * @param position         Map chunk's position in the mapChunks array.
     * @param chunkNumber      Current map chunk's number.
     */
    public MapChunk(GameScreen gameScreen, MapChunk previousMapChunk, World world, int position, int chunkNumber ) {
        this.gameScreen = gameScreen;
        this.previousMapChunk = previousMapChunk;
        this.world = world;
        this.position = position;
        this.chunkNumber = chunkNumber;
        this.canContainPowerup = ( ( this.chunkNumber % 4 == 0 && gameScreen.getGameState() != Constants.GAME_PLAYER_DEATH ) );
        this.canContainFlyingObstacle = ( ( this.chunkNumber % 3 == 0 && gameScreen.getGameState() != Constants.GAME_PLAYER_DEATH ) );
        setSleepStage();
        // Note that first MapChunk uses start values, others calculate them by method below.
        calculateValues();
        this.grid = MapGenerator.generateMapChunkGrid( this );
        MapGenerator.generateGameObjects(this);
    }

    /**
     * Sets sleep stage to switch between DEEP and REM.
     */
    public void setSleepStage( ) {
        if ( previousMapChunk == null ) {
            // First map chunk is REM.
            this.sleepStage = "REM";
        } else {
            if ( chunkNumber % Constants.DIFFICULTY_CHANGE_INTERVAL == 0 ) {
                // Sleep phase changing chunk.
                if ( previousMapChunk.getSleepStage().equals( "REM" ) ) {
                    this.sleepStage = "DEEP";
                } else {
                    this.sleepStage = "REM";
                }
            } else {
                // Use previous map chunk's sleep phase.
                this.sleepStage = previousMapChunk.getSleepStage();
            }
        }
    }

    /**
     * Calculates current chunks generating values.
     * Such as ground min and max amount and possibility for power up.
     */
    public void calculateValues() {
        if ( previousMapChunk != null ) {
            int minGround = previousMapChunk.getMinGroundBlocks();
            int maxGround = previousMapChunk.getMaxGroundBlocks();
            // Change the values if the chunk is right and the game is not in player death state.
            if (chunkNumber % Constants.DIFFICULTY_CHANGE_INTERVAL == 0 && gameScreen.getGameState() != Constants.GAME_PLAYER_DEATH ) {
                // Decrease the minimum and maximum values to make the game little bit harder.
                minGround -= 1;
                maxGround -= 1;
            }
            // Set chunks attributes to match calculated values.
            setMinGroundBlocks(minGround);
            setMaxGroundBlocks(maxGround);
        }
    }

    /**
     * Clears power up game object from map chunk.
     *
     * @param uiStage   Reference to UiStage.
     * @param id        Game object's UserData id.
     */
    public void clearGameObject( UIStage uiStage, String id ) {
        for ( GameObject gameObject : gameObjects) {
            if ( gameObject.getUserData().id.equals( id ) && gameObject instanceof PowerUpGameObject ) {
                PowerUpGameObject powerUpGameObject = (PowerUpGameObject) gameObject;
                if ( !powerUpGameObject.isCollected() ) {
                    Gdx.app.log( "MapChunk", "PowerUpGameObject collected." );
                    // Player collected power up.
                    powerUpGameObject.collect();
                    uiStage.collectPowerup();
                    removalGameObjects.add( gameObject );
                }
            }
        }
    }

    /**
     * Updates map chunks game objects.
     *
     * @param delta The delta time.
     */
    public void update( float delta ) {
        removeRemovalGameObjects();
        for (GameObject gameObject : gameObjects) {
            // Move and draw every game objects.
            gameObject.update(delta);
            if ( BodyUtils.gameObjectPassed( gameObject ) ) {
                // Remove game objects which has passed the screen.
                removalGameObjects.add(gameObject);
            }
        }
    }

    /**
     * Removes game objects which are set to removalGameObjects array.
     */
    public void removeRemovalGameObjects() {
        for (GameObject gameObject : removalGameObjects) {
            gameObject.dispose();
            gameObjects.removeValue(gameObject, true);
        }
        removalGameObjects.clear();
    }

    /**
     * Draws map chunks game objects.
     *
     * @param batch Spritebatch.
     */
    public void draw( SpriteBatch batch ) {
        for ( GameObject gameObject : gameObjects ) {
            gameObject.draw(batch);
        }
    }

    /**
     * Checks is the map chunk emptied from its content.
     *
     * @return boolean
     */
    public boolean isEmpty() {
        return gameObjects.size == 0;
    }

    /**
     * Adds new game object to map chunks gameObject array.
     *
     * @param gameObject The game object to add.
     */
    public void addGameObject( GameObject gameObject ) {
        if ( gameObject != null ) {
            gameObjects.add(gameObject);
        }
    }

    /**
     * Clears map chunk.
     */
    public void destroy( ) {
        for ( GameObject gameObject : gameObjects ) {
            removalGameObjects.add( gameObject );
        }
        removeRemovalGameObjects();
    }

    /**
     * Setters.
     */

    public void setMaxGroundBlocks( int maxGroundBlocks ) {
        // Minimum of maximum ground blocks is 1. (yes, it's pretty trippy!)
        if ( maxGroundBlocks < 1 ) {
            this.maxGroundBlocks = 1;
        } else {
            this.maxGroundBlocks = maxGroundBlocks;
        }
    }

    public void setMinGroundBlocks( int minGroundBlocks ) {
        if ( minGroundBlocks < 1 ) {
            this.minGroundBlocks = 1;
        } else {
            this.minGroundBlocks = minGroundBlocks;
        }
    }

    public void setMaxEmptyBlocks( int maxEmptyBlocks ) { this.maxEmptyBlocks = maxEmptyBlocks; }
    public void setMinEmptyBlocks( int minEmptyBlocks ) { this.minEmptyBlocks = minEmptyBlocks; }

    /**
     * Getters.
     */

    public int getMaxGroundBlocks( ) { return maxGroundBlocks; }
    public int getMinGroundBlocks( ) { return minGroundBlocks; }
    public int getMaxEmptyBlocks( ) { return maxEmptyBlocks; }
    public int getMinEmptyBlocks( ) { return minEmptyBlocks; }

    public boolean canContainFlyingObstacle( ) { return canContainFlyingObstacle; }
    public boolean canContainPowerup( ) { return canContainPowerup; }
    public Array<GameObject> getGameObjects( ) { return gameObjects; }
    public MapChunk getPreviousMapChunk( ) { return previousMapChunk; }
    public String getSleepStage( ) { return sleepStage; }
    public int getChunkNumber( ) { return chunkNumber; }
    public int getPosition( ) { return position; }
    public int[][] getGrid( ) { return grid; }
    public World getWorld( ) { return world; }

}
