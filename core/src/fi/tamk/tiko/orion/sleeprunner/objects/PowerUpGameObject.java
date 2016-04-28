package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;

/**
 *  Superclass of every power up game object.
 *  Not designed to construct object from this class.
 */
public abstract class PowerUpGameObject extends GameObject {

    private boolean collected = false;

    /**
     * Constructor for PowerUpGameObject.
     *
     * @param world           Box2D World
     * @param x               X-position.
     * @param y               Y-position.
     * @param width           Width of the body.
     * @param height          Height of the body.
     * @param powerUpTexture  Powerup's own texture.
     * @param powerUpUserData Powerup's own user data.
     */
    public PowerUpGameObject(World world, float x, float y, float width, float height, TextureRegion powerUpTexture, UserData powerUpUserData) {
        super(world, x, y, width, height, 0f, powerUpTexture, BodyDef.BodyType.KinematicBody, powerUpUserData );
    }

    /**
     * Collects power up, sets the collected attribute to true.
     */
    public void collect( ) {
        collected = true;
    }

    @Override
    public void update(float delta) {
        body.setLinearVelocity(Constants.ENEMY_LINEAR_VELOCITY);
    }

    @Override
    public void draw( Batch batch ) {
        if ( !hidden ) {
            float half = Constants.WORLD_TO_SCREEN / 100f / 2;
            float x = body.getPosition().x - half;
            float y = body.getPosition().y - half;
            batch.draw(textureRegion,
                    x,
                    y,
                    Constants.WORLD_TO_SCREEN / 2,
                    Constants.WORLD_TO_SCREEN / 2,
                    Constants.WORLD_TO_SCREEN / 100f,
                    Constants.WORLD_TO_SCREEN / 100f,
                    1.0f,
                    1.0f,
                    0);
        }
    }

    /**
     * Getters.
     */

    /**
     * @return collected Is the power up collected.
     */
    public boolean isCollected( ) { return collected; }

}
