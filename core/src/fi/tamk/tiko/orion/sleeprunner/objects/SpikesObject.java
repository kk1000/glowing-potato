package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;

/**
 * Spikes game object, its width is random.
 */
public class SpikesObject extends GameObject {

    /**
     * Tiles (texture regions) for spike's different parts.
     */

    public static final TextureRegion MIDDLE_TEXTURE = Constants.TILESET_SPRITES[0][1];
    public static final TextureRegion RIGHT_TEXTURE = Constants.TILESET_SPRITES[0][2];
    public static final TextureRegion LEFT_TEXTURE = Constants.TILESET_SPRITES[0][0];

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
        super(world, x, y, width, height, 0f, Constants.TILESET_SPRITES[0][1], BodyDef.BodyType.KinematicBody, new UserData("SPIKES"));
    }

    @Override
    public void update(float delta) {
        updateTiles( delta );
        body.setLinearVelocity(GameScreen.CURRENT_GAME_SPEED);
    }

}
