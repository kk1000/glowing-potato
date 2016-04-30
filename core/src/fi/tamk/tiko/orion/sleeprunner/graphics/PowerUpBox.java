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
        setWidth(powerUpBoxBackground.getWidth());
        setHeight(powerUpBoxBackground.getHeight());
        setX( Constants.APP_WIDTH - powerUpBoxBackground.getWidth() - 200f );
        setY( Constants.APP_HEIGHT - powerUpBoxBackground.getHeight() );
        powerUpX = getX() + 32;
        powerUpY = getY() + 25;
    }


    /**
     * Resets power up box for new game.
     */
    public void reset( ) {
        powerUpPicked = false;
        powerUpGameObject = null;
    }

    /**
     * Uses collected power up.
     */
    public void usePowerUp( ) {
        if ( powerUpPicked ) {
            if ( !powerUpGameObject.isUsed() ) {
                player.usePowerUp( powerUpGameObject );
                reset();
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw( powerUpBoxBackground, getX(), getY() );
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

    /**
     * Getters.
     */

    /**
     * @return boolean Has the player collected power up to the power up box.
     */
    public boolean hasPowerUp( ) {
        return powerUpPicked;
    }

}
