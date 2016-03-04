package fi.tamk.tiko.orion.sleeprunner.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.objects.GameObject;
import fi.tamk.tiko.orion.sleeprunner.objects.Ground;

/**
 * Contains methods for creating the game world and game objects.
 * (Will this include random map generation?)
 */
public class WorldUtilities {

    /**
     * Creates MapObjects to Box2D bodies.
     *
     * @param world      Box2D world.
     * @param mapObjects Created MapObjects in a map chunk.
     */
    public static void createObjectsToBodies(Array<GameObject> gameObjects, World world, MapObjects mapObjects) {
        Array<RectangleMapObject> rectangleMapObjects = mapObjects.getByType(RectangleMapObject.class);
        Gdx.app.log("WorldUtilities", "Creating bodies from " + rectangleMapObjects.size + " rectangle map objects!");
        for (RectangleMapObject rectangleMapObject : rectangleMapObjects) {
            Rectangle pixelRectangle = rectangleMapObject.getRectangle();
            Rectangle meterRectangle = scaleRectangle(pixelRectangle, 1 / 100f);
            float centerX = meterRectangle.getWidth() / 2 + meterRectangle.getX();
            float centerY = meterRectangle.getHeight() / 2 + meterRectangle.getY();
            float width = meterRectangle.getWidth();
            float height = meterRectangle.getHeight();
            if (rectangleMapObject.getName().equals("ground-object")) {
                Gdx.app.log("WorldUtilities", "Ground object!");
                TextureRegion textureRegion = Constants.TILESET_SPRITES[0][0];
                Ground ground = new Ground(world, centerX, centerY, width, height, textureRegion);
                gameObjects.add(ground);
            }
        }
    }

    /**
     * Scales given rectangle's size by given amount
     * and returns it.
     * <p/>
     * Is this method done already somewhere or something?
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

}
