package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.UserData;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;

/**
 * Spikes game object, its width is random.
 */
public class SpikesObject extends GameObject {

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
    public SpikesObject(GameScreen gameScreen, World world, float x, float y, float width, float height) {
        super( gameScreen, world, x, y, width, height, 0f, gameScreen.getGame().resources.spikesCenter, BodyDef.BodyType.KinematicBody, new UserData("SPIKES"));
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        updateTiles(delta);
    }

}
