package fi.tamk.tiko.orion.sleeprunner.utilities;

import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.objects.GameObject;
import fi.tamk.tiko.orion.sleeprunner.objects.GroundObject;
import fi.tamk.tiko.orion.sleeprunner.objects.MidPointBlockObject;

/**
 * Contains one map chunks game objects and information.
 */
public class MapChunk {

    private Stage stage;
    private World world;

    private int[][] grid;

    private MapObjects mapObjects;

    private Array<GameObject> gameObjects = new Array<GameObject>();
    private Array<GameObject> removalGameObjects = new Array<GameObject>();

    private float offset = 0;

    /**
     * Constructor for MapChunk.
     *
     * @param stage   Stage where game objects will be put.
     * @param world   The Box2D game world.
     * @param landing Is the map chunk landing or not.
     */
    public MapChunk(Stage stage, World world, boolean landing) {
        this.stage = stage;
        this.world = world;
        if (landing) {
            grid = MapGenerator.createIntervalMapChunkGrid();
        } else {
            grid = MapGenerator.createMapChunkGrid();
            offset = (Constants.CHUNK_MAX_TILES_WIDTH * Constants.WORLD_TO_SCREEN) / 100f;
        }
        mapObjects = MapGenerator.generateObjects(grid, Constants.GROUND_BLOCK, "ground-object");
        mapObjects.add(MapGenerator.generateObjects(grid, Constants.MIDPOINT_BLOCK, "midpointblock-object").get(0));
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
    private void createGameObjects() {
        Array<RectangleMapObject> rectangleMapObjects = mapObjects.getByType(RectangleMapObject.class);
        for (RectangleMapObject rectangleMapObject : rectangleMapObjects) {
            Rectangle pixelRectangle = rectangleMapObject.getRectangle();
            Rectangle meterRectangle = scaleRectangle(pixelRectangle, 1 / 100f);
            float centerX = meterRectangle.getWidth() / 2 + meterRectangle.getX() + offset;
            float centerY = meterRectangle.getHeight() / 2 + meterRectangle.getY();
            float width = meterRectangle.getWidth();
            float height = meterRectangle.getHeight();
            if (rectangleMapObject.getName().equals("ground-object")) {
                GroundObject ground = new GroundObject(world, centerX, centerY, width, height);
                gameObjects.add(ground);
                stage.addActor(ground);
            } else if (rectangleMapObject.getName().equals("midpointblock-object")) {
                MidPointBlockObject midPointBlockObject = new MidPointBlockObject(world, centerX, centerY);
                gameObjects.add(midPointBlockObject);
                stage.addActor(midPointBlockObject);
            }
        }
    }

    /**
     * Removes game objects which are outside the screen.
     *
     * @return boolean If new map chunk should be created.
     */
    public boolean update() {
        boolean newMapChunk = false;
        for (GameObject gameObject : gameObjects) {
            if (!BodyUtils.gameObjectInBounds(gameObject)) {
                if (gameObject.getUserData().id.equals("MIDPOINTBLOCK")) {
                    newMapChunk = true;
                }
                removalGameObjects.add(gameObject);
                gameObjects.removeValue(gameObject, true);
            }
        }
        for (GameObject gameObject : removalGameObjects) {
            world.destroyBody(gameObject.getBody());
        }
        removalGameObjects.clear();
        return newMapChunk;
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
     * Removes every game object from the map chunk.
     */
    public void clearGameObjects() {
        removalGameObjects = gameObjects;
        for (GameObject gameObject : removalGameObjects) {
            world.destroyBody(gameObject.getBody());
        }
        removalGameObjects.clear();
    }

}
