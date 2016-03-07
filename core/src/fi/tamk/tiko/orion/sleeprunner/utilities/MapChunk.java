package fi.tamk.tiko.orion.sleeprunner.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.objects.GameObject;
import fi.tamk.tiko.orion.sleeprunner.objects.GroundObject;
import fi.tamk.tiko.orion.sleeprunner.objects.SpikesObject;

/**
 * Contains one map chunks game objects and information.
 */
public class MapChunk {

    private World world;

    private int[][] grid;

    private MapObjects mapObjects = new MapObjects();

    private Array<GameObject> gameObjects = new Array<GameObject>();
    private Array<GameObject> removalGameObjects = new Array<GameObject>();

    private float mapChunksWidth = Constants.CHUNK_MAX_TILES_WIDTH * Constants.WORLD_TO_SCREEN / 100f;
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
            grid = MapGenerator.createIntervalMapChunkGrid();
        } else {
            grid = MapGenerator.createMapChunkGrid();
        }
        MapObjects groundMapObjects = MapGenerator.generateObjects(grid, Constants.GROUND_BLOCK, "ground-object");
        MapObjects spikesMapObjects = MapGenerator.generateObjects(grid, Constants.SPIKES_BLOCK, "spikes-object");
        // Add ground map objects to mapObjects list.
        for (MapObject mapObject : groundMapObjects) {
            mapObjects.add(mapObject);
        }
        // Add spikes map objects to mapObjects list.
        for (MapObject mapObject : spikesMapObjects) {
            mapObjects.add(mapObject);
        }
        // Create game objects (ground, spikes) from map objects.
        createGameObjects();
    }

    /**
     * Scales given rectangle's size by given scale
     * and returns it.
     *
     * @param rect  The rectangle to scale.
     * @param scale The scale.
     * @return Scaled rectangle.
     */
    public static Rectangle scaleRectangle(Rectangle rect, float scale) {
        Rectangle rectangle = new Rectangle();
        rectangle.x = rect.x * scale;
        rectangle.y = rect.y * scale;
        rectangle.width = rect.width * scale;
        rectangle.height = rect.height * scale;
        return rectangle;
    }

    /**
     * Creates map chunks game objects from the map objects.
     */
    public void createGameObjects() {
        Array<RectangleMapObject> rectangleMapObjects = mapObjects.getByType(RectangleMapObject.class);
        for (RectangleMapObject rectangleMapObject : rectangleMapObjects) {
            Rectangle pixelRectangle = rectangleMapObject.getRectangle();
            Rectangle meterRectangle = scaleRectangle(pixelRectangle, 1 / 100f);
            float centerX = meterRectangle.getWidth() / 2 + meterRectangle.getX() + (offset * mapChunksWidth);
            float centerY = meterRectangle.getHeight() / 2 + meterRectangle.getY();
            float width = meterRectangle.getWidth();
            float height = meterRectangle.getHeight();
            if (rectangleMapObject.getName().equals("ground-object")) {
                GroundObject ground = new GroundObject(world, centerX, centerY, width, height);
                gameObjects.add(ground);
            } else if (rectangleMapObject.getName().equals("spikes-object")) {
                SpikesObject spikes = new SpikesObject(world, centerX, centerY, width, height);
                gameObjects.add(spikes);
            }
        }
    }

    /**
     * Removes game objects which are outside the screen.
     *
     * @param batch Spritebatch.
     */
    public void update(SpriteBatch batch) {
        removeRemovalGameObjects();
        for (GameObject gameObject : gameObjects) {
            gameObject.update(Gdx.graphics.getDeltaTime());
            gameObject.draw(batch);
            if (!BodyUtils.gameObjectInBounds(gameObject)) {
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
}
