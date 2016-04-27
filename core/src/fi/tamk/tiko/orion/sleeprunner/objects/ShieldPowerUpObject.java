package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.UserData;

/**
 * Shield power up.
 */
public class ShieldPowerUpObject extends PowerUpGameObject {


    public static final TextureRegion POWERUP_SHIELD_TEXTURE = new TextureRegion( new Texture( Gdx.files.internal( "graphics/powerup_shield.png" ) ) );

    /**
     * Constructor for ShieldPowerUpObject
     *
     * @param world          Box2D World
     * @param x              X-position.
     * @param y              Y-position.
     * @param width          Width of the body.
     * @param height         Height of the body.
     */
    public ShieldPowerUpObject(World world, float x, float y, float width, float height) {
        super(world, x, y, width, height, POWERUP_SHIELD_TEXTURE, new UserData("POWERUP_SHIELD"));
    }

}
