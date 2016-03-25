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

    public static final TextureRegion MIDDLETOP_TEXTURE = Constants.TILESET_SPRITES[2][1];
    public static final TextureRegion MIDDLE_TEXTURE = Constants.TILESET_SPRITES[3][1];
    public static final TextureRegion RIGHT_TEXTURE = Constants.TILESET_SPRITES[2][2];
    public static final TextureRegion LEFT_TEXTURE = Constants.TILESET_SPRITES[2][0];

    /**
     * Constructor for ground object.
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
        updateTiles( delta );
        body.setLinearVelocity(Constants.ENEMY_LINEAR_VELOCITY);
    }

}