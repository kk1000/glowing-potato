package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;

/**
 * Dummy sensor for checking when is the time to
 * generate new map chunk.
 */
public class MidPointBlockObject extends GameObject {

    /**
     * Constructor for MidPointBlockObject.
     *
     * @param world Box2D World
     * @param x     X-position.
     * @param y     Y-position.
     */
    public MidPointBlockObject(World world, float x, float y) {
        super(world, x, y,
                Constants.WORLD_TO_SCREEN / 100f,
                Constants.WORLD_TO_SCREEN / 100f,
                0f, Constants.TILESET_SPRITES[0][3],
                BodyDef.BodyType.KinematicBody,
                new UserData("MIDPOINTBLOCK"));
    }

    /**
     * Act method.
     *
     * @param delta Delta timer (1/60)
     */
    @Override
    public void act(float delta) {
        super.act(delta);
        body.setLinearVelocity(Constants.ENEMY_LINEAR_VELOCITY);
    }

}
