package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;

/**
 * SpikesObject/obstacle actor class.
 * Extended from GameObject.
 */
public class SpikesObject extends GameObject {

    /**
     * Constructor for SpikesObject.
     *
     * @param world          Box2D World
     * @param x              X-position.
     * @param y              Y-position.
     * @param width          Width of the body.
     * @param height         Height of the body.
     * @param mapChunkGrid   Map chunk grid.
     */
    public SpikesObject(World world, float x, float y, float width, float height, int[][] mapChunkGrid) {
        super(world, x, y, width, height, 0f, Constants.TILESET_SPRITES[0][1], BodyDef.BodyType.KinematicBody, new UserData("SPIKES"), mapChunkGrid);
    }

    @Override
    public void update(float delta) {
        body.setLinearVelocity(Constants.ENEMY_LINEAR_VELOCITY);
    }

}
