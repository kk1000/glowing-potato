package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;

/**
 *  Superclass of every power up game object.
 *  Not designed to construct object from this class.
 *
 * @author   Eetu "mehxit" Järvinen
 * @author   Joni "steiner3k" Korpisalo
 */
public abstract class PowerUpGameObject extends GameObject {

    private boolean collected = false;
    protected boolean used = false;

    /**
     * Constructor for PowerUpGameObject.
     *
     * @param gameScreen     GameScreen reference.
     * @param world           Box2D World
     * @param x               X-position.
     * @param y               Y-position.
     * @param width           Width of the body.
     * @param height          Height of the body.
     * @param powerUpTexture  Powerup's own texture.
     * @param powerUpUserData Powerup's own user data.
     */
    public PowerUpGameObject( GameScreen gameScreen, World world, float x, float y, float width, float height, TextureRegion powerUpTexture, UserData powerUpUserData) {
        super( gameScreen, world, x, y, width, height, 0f, powerUpTexture, BodyDef.BodyType.KinematicBody, powerUpUserData );
    }

    /**
     * Collects power up, sets the collected attribute to true.
     */
    public void collect( ) {
        game.resources.powerUpPickSound.play( prefs.getSoundVolume() );
        collected = true;
    }

    @Override
    public void draw( Batch batch ) {
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

    /**
     * Power ups own action.
     */
    public abstract void action( PlayerObject playerObject );

    /**
     * Getters.
     */

    /**
     * @return collected Is the power up collected.
     */
    public boolean isCollected( ) { return collected; }

    /**
     * @return used Is the power up used.
     */
    public boolean isUsed( ) { return used; }

}
