package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;

/**
 * GroundObject actor class.
 * GroundObject texture is split to two textureregions for the endless movement visual effect.
 * Extended from GameObject.
 */
public class GroundObject extends GameObject {

    /**
     * Constructor for game objects which got no animation.
     *
     * @param world  Box2D World
     * @param x      X-position.
     * @param y      Y-position.
     * @param width  Width of the body.
     * @param height Height of the body.
     */
    public GroundObject(World world, float x, float y, float width, float height) {
        super(world, x, y, width, height, 0f, Constants.TILESET_SPRITES[0][0], BodyDef.BodyType.KinematicBody, new UserData("GROUND"));
    }

    @Override
    public void act(float delta){
        super.act(delta);
        body.setLinearVelocity(Constants.ENEMY_LINEAR_VELOCITY);
    }

}