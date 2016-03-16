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

    private World world;

    private int[][] grid;

    private Array<GameObject> removalGameObjects = new Array<GameObject>();
    private Array<GameObject> gameObjects = new Array<GameObject>();

    private int offset;

    /**
     * Constructor for MapChunk.
     *
     * @param world   The Box2D game world.
     * @param offset  Map chunk's position in the mapChunks array.
     */
    public MapChunk(World world, int offset) {
        this.world = world;
        this.offset = offset;
        if (offset == 0) {
            grid = MapGenerator.generateMapChunkIntervalGrid();
        } else {
            grid = MapGenerator.generateMapChunkGrid();
        }
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
     * Getters.
     */

    public int[][] getGrid( ) { return grid; }
    public int getOffset( ) { return offset; }
    public World getWorld( ) { return world; }
    public Array<GameObject> getGameObjects( ) { return gameObjects; }

}
