package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.UserData;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;

/**
 * Fly power up.
 */
public class FlyPowerUpObject extends PowerUpGameObject {

    /**
     * Constructor for FlyPowerUpObject
     *
     * @param gameScreen     GameScreen reference.
     * @param world          Box2D World
     * @param x              X-position.
     * @param y              Y-position.
     * @param width          Width of the body.
     * @param height         Height of the body.
     */
    public FlyPowerUpObject(GameScreen gameScreen, World world, float x, float y, float width, float height) {
        super(gameScreen, world, x, y, width, height, gameScreen.getGame().resources.powerUpOne, new UserData("POWERUP_FLY"));
    }

    @Override
    public void action( PlayerObject playerObject ) {
        game.resources.powerUpFlySound.loop( prefs.getSoundVolume() );
        playerObject.fly();
        used = true;
    }

}
