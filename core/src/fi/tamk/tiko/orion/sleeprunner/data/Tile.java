package fi.tamk.tiko.orion.sleeprunner.data;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;

/**
 * Class for holding game object's tile's properties.
 */
public class Tile {

    public GameScreen gameScreen;
    public TextureRegion textureRegion;
    public float x;
    public float y;

    /**
     * Constructor.
     *
     * @param gameScreen    GameScreen reference.
     * @param x             Tile's x position.
     * @param y             Tile's y position.
     * @param textureRegion Tile's texture.
     */
    public Tile( GameScreen gameScreen, float x, float y, TextureRegion textureRegion ) {
        this.gameScreen = gameScreen;
        this.x = x;
        this.y = y;
        this.textureRegion = textureRegion;
    }

    /**
     * Updates tile's position on every frame.
     */
    public void update( float delta ) {
        this.x += gameScreen.getCurrentGameSpeed() * delta;
    }

}
