package fi.tamk.tiko.orion.sleeprunner.utilities;

/**
 * Contains methods for creating the game world and game objects.
 * (Will this include random map generation?)
 */

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.EnemyType;
import fi.tamk.tiko.orion.sleeprunner.data.EnemyUserData;
import fi.tamk.tiko.orion.sleeprunner.data.GroundUserData;
import fi.tamk.tiko.orion.sleeprunner.data.PlayerUserData;


public class WorldUtilities {

    /**
     * Creates the world for the game.
     * @return the new world
     */
    public static World createWorld() {
        return new World(Constants.WORLD_GRAVITY, true);
    }

    /**
     * Creates ground to the world.
     * @param world = world used in game.
     * @return ground body
     */
    public static Body createGround(World world, float x, float y, float width, float height, TextureRegion textureRegion) {
        BodyDef bodyDef = new BodyDef();

        bodyDef.position.set(new Vector2(x, y));
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);

        body.createFixture(shape, Constants.GROUND_DENSITY);
        body.setUserData(new GroundUserData(width, height));

        shape.dispose();

        return body;
    }

    /**
     * Creates a player to the world.
     * @param world = world used in game.
     * @return players body
     */
    public static Body createPlayer(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(new Vector2(Constants.PLAYER_X, Constants.PLAYER_Y));
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT / 2);

        Body body = world.createBody(bodyDef);
        body.createFixture(shape, Constants.PLAYER_DENSITY);
        body.resetMassData();

        body.setGravityScale(Constants.PLAYER_GRAVITY_SCALE);
        body.setUserData(new PlayerUserData(Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT));

        shape.dispose();

        return body;
    }

    /**
     * Creates enemy or obstacle to the world.
     * @param world = world used in game.
     * @return enemys/obstacles body
     */
    public static Body createEnemy(World world){

        EnemyType enemyType = RandomUtils.getRandomEnemyType();

        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.KinematicBody;

        bodyDef.position.set(new Vector2(enemyType.getX(), enemyType.getY()));

        PolygonShape shape = new PolygonShape();

        shape.setAsBox(enemyType.getWidth() / 2, enemyType.getHeight() / 2);

        Body body = world.createBody(bodyDef);

        body.createFixture(shape, enemyType.getDensity());

        body.resetMassData();

        EnemyUserData userData = new EnemyUserData(enemyType.getWidth(),enemyType.getHeight(),enemyType.getPath());

        body.setUserData(userData);

        shape.dispose();

        return body;
    }



}
