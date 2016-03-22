package fi.tamk.tiko.orion.sleeprunner.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import fi.tamk.tiko.orion.sleeprunner.objects.GameObject;

/**
 * Contains one map chunks game objects and information.
 */
public class MapChunk {

    private Array<GameObject> removalGameObjects = new Array<GameObject>();
    private Array<GameObject> gameObjects = new Array<GameObject>();

    private int[][] grid;

    private World world;

    private int maxEmptyBlocks;
    private int minEmptyBlocks;
    private int maxGroundBlocks;
    private int minGroundBlocks;

    private int position;

    /**
     * Constructor for MapChunk.
     *
     * @param world     The Box2D game world.
     * @param position  Map chunk's position in the mapChunks array.
     * @param minEmptyBlocks  Minimum amount of empty blocks in the chunk.
     * @param maxEmptyBlocks  Maximum amount of empty blocks in the chunk.
     * @param minGroundBlocks Minimum amount of ground blocks in the chunk.
     * @param maxGroundBlocks Maximum amount of ground blocks in the chunk.
     */
    public MapChunk(World world, int position, int minEmptyBlocks, int maxEmptyBlocks, int minGroundBlocks, int maxGroundBlocks ) {
        this.world = world;
        this.position = position;
        this.minEmptyBlocks = minEmptyBlocks;
        this.maxEmptyBlocks = maxEmptyBlocks;
        this.minGroundBlocks = minGroundBlocks;
        this.maxGroundBlocks = maxGroundBlocks;
        this.grid = MapGenerator.generateMapChunkGrid( this.minEmptyBlocks, this.maxEmptyBlocks, this.minGroundBlocks, this.maxGroundBlocks );
        MapGenerator.generateGameObjects(this);
    }

    /**
     * Updates map chunks game objects.
     *
     * @param batch Spritebatch.
     */
    public void update(SpriteBatch batch) {
        removeRemovalGameObjects();
        for (GameObject gameObject : gameObjects) {
            // Move and draw every game objects.
            gameObject.update(Gdx.graphics.getDeltaTime());
            gameObject.draw( batch );
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
     * Getters.
     */

    public Array<GameObject> getGameObjects( ) { return gameObjects; }
    public World getWorld( ) { return world; }
    public int getPosition( ) { return position; }
    public int[][] getGrid( ) { return grid; }

}
