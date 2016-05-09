package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.UserData;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;

/**
 * Mask power up.
 *
 * @author   Eetu "mehxit" Järvinen
 * @author   Joni "steiner3k" Korpisalo
 */
public class MaskPowerUpObject extends PowerUpGameObject {

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
    public MaskPowerUpObject(GameScreen gameScreen, World world, float x, float y, float width, float height) {
        super(gameScreen, world, x, y, width, height, gameScreen.getGame().resources.powerUpTwo, new UserData("POWERUP_MASK"));
    }

    @Override
    public void action( PlayerObject playerObject ) {
        gameScreen.getNightmare().moveBack();
        gameScreen.getUiStage().minusNightmareMeterByStep();
        used = true;
    }

}
