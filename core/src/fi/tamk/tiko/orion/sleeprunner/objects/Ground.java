package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.GroundUserData;


/**
 * Ground actor class.
 * Ground texture is split to two textureregions for the endless movement visual effect.
 * Extended from GameObject.
 */
public class Ground extends GameObject {

    private final TextureRegion textureRegion;
    private Rectangle textureRegionBounds1;
    private Rectangle textureRegionBounds2;
    private int speed = 10;

    /**
     * Constructor for Ground.
     * Texture for textureregion, rectangles for the split regions.
     *
     * @param body = body of the ground object
     */
    public Ground(Body body) {
        super(body);
        textureRegion = new TextureRegion(new Texture(Gdx.files.internal(Constants.GROUND_IMAGE_PATH)));
        textureRegionBounds1 = new Rectangle(0 - getUserData().getWidth() / 2, 0, getUserData().getWidth(), getUserData().getHeight());
        textureRegionBounds2 = new Rectangle(getUserData().getWidth() / 2, 0, getUserData().getWidth(), getUserData().getWidth());
    }

    /**
     * @return ground user data
     */
    @Override
    public GroundUserData getUserData() {
        return (GroundUserData) userData;
    }

    /**
     * Resets bounds if the leftest textureregion hits left side of screen.
     * Updates both textureregion's x-position (-delta = left, delta = right).
     *
     * @param delta = delta timer (1/60)
     */
    @Override
    public void act(float delta){
        super.act(delta);
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
        batch.draw(textureRegion, textureRegionBounds1.x, screenRectangle.y, screenRectangle.getWidth(), screenRectangle.getHeight());
        batch.draw(textureRegion, textureRegionBounds2.x, screenRectangle.y, screenRectangle.getWidth(), screenRectangle.getHeight());
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
        textureRegionBounds2 = new Rectangle(textureRegionBounds1.x+screenRectangle.width,0,screenRectangle.width,screenRectangle.height);
    }

}
