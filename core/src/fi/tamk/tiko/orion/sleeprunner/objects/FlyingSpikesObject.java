package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;

/**
 * Spikes game object, its width is random.
 *
 * @author   Eetu "mehxit" Järvinen
 * @author   Joni "steiner3k" Korpisalo
 */
public class FlyingSpikesObject extends GameObject {

    /**
     * Constructor for SpikesObject.
     *
     * @param gameScreen     GameScreen reference.
     * @param world          Box2D World
     * @param x              X-position.
     * @param y              Y-position.
     * @param width          Width of the body.
     * @param height         Height of the body.
     */
    public FlyingSpikesObject(GameScreen gameScreen, World world, float x, float y, float width, float height) {
        super(gameScreen, world, x, y, width, height, 0f, gameScreen.getGame().resources.spikeBallOne, BodyDef.BodyType.KinematicBody, new UserData("FLYING_SPIKES"));
    }

    @Override
    public void draw( Batch batch ) {
        float half = Constants.WORLD_TO_SCREEN/100f/2;
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
