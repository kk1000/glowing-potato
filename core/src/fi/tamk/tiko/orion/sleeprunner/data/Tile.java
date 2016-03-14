package fi.tamk.tiko.orion.sleeprunner.data;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Class for holding game object's tile's properties.
 */
public class Tile {

    public TextureRegion textureRegion;
    public float x;
    public float y;

    /**
     * Constructor.
     *
     * @param x             Tile's x position.
     * @param y             Tile's y position.
     * @param textureRegion Tile's texture.
     */
    public Tile( float x, float y, TextureRegion textureRegion ) {
        this.x = x;
        this.y = y;
        this.textureRegion = textureRegion;
    }

    /**
     * Updates tile's position on every frame.
     */
    public void update( float delta ) {
        this.x += Constants.ENEMY_LINEAR_VELOCITY.x * delta;
    }

}
