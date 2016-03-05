package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;

/**
 * SpikesObject/obstacle actor class.
 * Extended from GameObject.
 */
public class SpikesObject extends GameObject {

    private EnemyUserData userData;

    /**
     * Constructor for SpikesObject.
     *
     * @param world          Box2D World
     * @param x              X-position.
     * @param y              Y-position.
     * @param width          Width of the body.
     * @param height         Height of the body.
     */
    public SpikesObject(World world, float x, float y, float width, float height) {
        super(world, x, y, width, height, 0f, Constants.TILESET_SPRITES[0][1], BodyDef.BodyType.KinematicBody);
        userData = new EnemyUserData(width, height);
        body.setUserData(userData);
    }

    /**
     * Act method.
     *
     * @param delta Delta timer (1/60)
     */
    @Override
    public void act(float delta){
        super.act(delta);
        body.setLinearVelocity(userData.getLinearVelocity());
    }

    /**
     * Getters.
     */

    public EnemyUserData getUserData() {
        return userData;
    }

}
