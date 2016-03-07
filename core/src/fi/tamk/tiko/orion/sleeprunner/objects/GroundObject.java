package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;

/**
 * Ground object, its width is random.
 */
public class GroundObject extends GameObject {

    /**
     * Tiles (texture regions) for ground's different parts.
     */

    public static final TextureRegion MIDDLE_TEXTURE = Constants.TILESET_SPRITES[0][7];
    public static final TextureRegion RIGHT_TEXTURE = Constants.TILESET_SPRITES[0][8];
    public static final TextureRegion LEFT_TEXTURE = Constants.TILESET_SPRITES[0][6];

    /**
     * Constructor for game objects which got no animation.
     *
     * @param world        Box2D World
     * @param x            X-position.
     * @param y            Y-position.
     * @param width        Width of the body.
     * @param height       Height of the body.
     */
    public GroundObject(World world, float x, float y, float width, float height) {
        super(world, x, y, width, height, 0f, Constants.TILESET_SPRITES[0][0], BodyDef.BodyType.KinematicBody, new UserData("GROUND"));
    }

    @Override
    public void update(float delta) {
        body.setLinearVelocity(Constants.ENEMY_LINEAR_VELOCITY);
    }

}