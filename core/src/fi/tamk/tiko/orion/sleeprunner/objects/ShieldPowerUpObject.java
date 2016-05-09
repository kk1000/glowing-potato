package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.UserData;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;

/**
 * Shield power up.
 *
 * @author   Eetu "mehxit" Järvinen
 * @author   Joni "steiner3k" Korpisalo
 */
public class ShieldPowerUpObject extends PowerUpGameObject {

    /**
     * Constructor for ShieldPowerUpObject
     *
     * @param gameScreen     GameScreen reference.
     * @param world          Box2D World
     * @param x              X-position.
     * @param y              Y-position.
     * @param width          Width of the body.
     * @param height         Height of the body.
     */
    public ShieldPowerUpObject(GameScreen gameScreen, World world, float x, float y, float width, float height) {
        super(gameScreen, world, x, y, width, height, gameScreen.getGame().resources.powerUpThree, new UserData("POWERUP_SHIELD"));
    }

    @Override
    public void action( PlayerObject playerObject ) {
        game.resources.powerUpShieldSound.play( prefs.getSoundVolume() );
        playerObject.setShielded( true );
        used = true;
    }

}
