package fi.tamk.tiko.orion.sleeprunner.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;

/**
 * Game screen's background.
 * Background is split to two textureregions for the endless movement visual effect.
 */
public class Background1 extends Actor {


    // textureregion used for whole texture image
    private final TextureRegion textureRegion;
    // first half of the background
    private Rectangle textureRegionBounds1;
    // second half of the background
    private Rectangle textureRegionBounds2;
    // background's movement speed
    private int speed = 10;

    /**
     * Constructor for background.
     * Texture for textureregion, rectangles for the split regions.
     */
    public Background1(){
        textureRegion = new TextureRegion(new Texture(Gdx.files.internal(Constants.BACKGROUND_IMAGE_PATH)));
        textureRegionBounds1 = new Rectangle(0 - Constants.APP_WIDTH / 2, 0, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        textureRegionBounds2 = new Rectangle(Constants.APP_WIDTH / 2, 0, Constants.APP_WIDTH, Constants.APP_HEIGHT);


    }

    /**
     * Sets value for speed.
     *
     * @param s = value for speed
     */
    public void setSpeed(int s){
        s = this.speed;
    }

    /**
     * Resets bounds if the leftest textureregion hits left side of screen.
     * Updates both textureregion's x-position (-delta = left, delta = right).
     *
     * @param delta = delta timer (1/60)
     */
    @Override
    public void act(float delta){
        if(leftBoundsReached(delta)){
            resetBounds();
        }else{
            updateXBounds(-delta);
        }
    }

    /**
     * Draw method.
     *
     * @param batch = spritebatch
     * @param parentAlpha = alpha level (default value)
     */
    @Override
    public void draw(Batch batch, float parentAlpha){
        super.draw(batch, parentAlpha);
        batch.draw(textureRegion, textureRegionBounds1.x, textureRegionBounds1.y, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        batch.draw(textureRegion, textureRegionBounds2.x, textureRegionBounds2.y, Constants.APP_WIDTH, Constants.APP_HEIGHT);
    }

    /**
     * Checks if left side of screen is reached.
     *
     * @param delta = delta timer (1/60)
     * @return boolean
     */
    private boolean leftBoundsReached(float delta){
        return (textureRegionBounds2.x - (delta*speed)) <= 0;
    }

    /**
     * Updates both textureregion's x-position with @param delta.
     * @param delta = delta timer (1/60)
     */
    private void updateXBounds(float delta){
        textureRegionBounds1.x += delta*speed;
        textureRegionBounds2.x += delta*speed;
    }

    /**
     * Resets bounds when called.
     * Swaps textureregion1 to textureregion2, and creates a new rectangle for textureregion2.
     */
    private void resetBounds(){
        textureRegionBounds1 = textureRegionBounds2;
        textureRegionBounds2 = new Rectangle(Constants.APP_WIDTH,0,Constants.APP_WIDTH,Constants.APP_HEIGHT);
    }

}