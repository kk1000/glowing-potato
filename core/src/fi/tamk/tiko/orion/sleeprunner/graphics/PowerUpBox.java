package fi.tamk.tiko.orion.sleeprunner.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import fi.tamk.tiko.orion.sleeprunner.SleepRunner;
import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.objects.PlayerObject;
import fi.tamk.tiko.orion.sleeprunner.objects.PowerUpGameObject;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;

/**
 * Power up box shows player's power up.
 */
public class PowerUpBox extends Actor {

    private SleepRunner sleepRunner;
    private GameScreen gameScreen;
    private PlayerObject player;

    private boolean powerUpPicked = false;
    private PowerUpGameObject powerUpGameObject;

    private Texture powerUpBoxBackground;

    private float powerUpBoxX;
    private float powerUpBoxY;
    private float powerUpX;
    private float powerUpY;

    /**
     * Constructor for UIText
     *
     * @param sleepRunner   SleepRunner reference.
     * @param gameScreen    GameScreen reference.
     */
    public PowerUpBox(SleepRunner sleepRunner, GameScreen gameScreen) {
        this.sleepRunner = sleepRunner;
        this.gameScreen = gameScreen;
        this.player = gameScreen.getPlayer();

        powerUpBoxBackground = new Texture( Gdx.files.internal( "graphics/powerupboxbackground.png" ) );
        powerUpBoxX = Constants.APP_WIDTH - powerUpBoxBackground.getWidth() - 200f;
        powerUpBoxY = Constants.APP_HEIGHT - powerUpBoxBackground.getHeight();
        powerUpX = powerUpBoxX + 32;
        powerUpY = powerUpBoxY + 25;
    }


    /**
     * Resets power up box for new game.
     */
    public void reset( ) {
        powerUpPicked = false;
        powerUpGameObject = null;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw( powerUpBoxBackground, powerUpBoxX, powerUpBoxY );
        if ( powerUpPicked ) {
            TextureRegion textureRegion = powerUpGameObject.getTextureRegion();
            batch.draw( textureRegion,
                    powerUpX,
                    powerUpY,
                    textureRegion.getRegionWidth()/2,
                    textureRegion.getRegionHeight()/2,
                    textureRegion.getRegionWidth(),
                    textureRegion.getRegionWidth(),
                    2.0f,
                    2.0f,
                    0);
        }
    }

    /**
     * Setters.
     */

    /**
     * Sets powerUpPicked and powerUpGameObject
     *
     * @param powerUpGameObject What power up player picked.
     */
    public void setPowerUpPicked( PowerUpGameObject powerUpGameObject ) {
        this.powerUpGameObject = powerUpGameObject;
        powerUpPicked = true;
    }


}
