package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;

/**
 * Spikes game object, its width is random.
 */
public class ShieldPowerUpObject extends GameObject {

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
        super(world, x, y, width, height, 0f, new TextureRegion( new Texture( Gdx.files.internal( "graphics/powerup_shield.png" ) )), BodyDef.BodyType.KinematicBody, new UserData("SHIELD"));
    }

    @Override
    public void update(float delta) {
        body.setLinearVelocity(Constants.ENEMY_LINEAR_VELOCITY);
    }

}
