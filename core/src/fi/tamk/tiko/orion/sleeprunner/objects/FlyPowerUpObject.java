package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.UserData;

/**
 * Fly power up.
 */
public class FlyPowerUpObject extends PowerUpObject {


    public static final TextureRegion POWERUP_FLY_TEXTURE = new TextureRegion( new Texture( Gdx.files.internal( "graphics/powerup_fly.png" ) ) );

    /**
     * Constructor for FlyPowerUpObject
     *
     * @param world          Box2D World
     * @param x              X-position.
     * @param y              Y-position.
     * @param width          Width of the body.
     * @param height         Height of the body.
     */
    public FlyPowerUpObject(World world, float x, float y, float width, float height) {
        super(world, x, y, width, height,POWERUP_FLY_TEXTURE, new UserData("POWERUP_FLY"));
    }

}
