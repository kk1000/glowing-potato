package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.UserData;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;

/**
 * Sign object is a sign telling player which sleep stage is next.
 *
 * @author   Eetu "mehxit" Järvinen
 * @author   Joni "steiner3k" Korpisalo
 */
public class SignObject extends AnimatedGameObject {

    private boolean clicked = false;
    private String sleepStage;

    /**
     * Constructor for sign object.
     *
     * @param gameScreen     GameScreen reference.
     * @param world        Box2D World
     * @param x            X-position.
     * @param y            Y-position.
     * @param width        Width of the body.
     * @param height       Height of the body.
     * @param sleepStage   What sleep stage does the sign represent.
     */
    public SignObject( GameScreen gameScreen, World world, float x, float y, float width, float height, String sleepStage ) {
        super( gameScreen, world, x, y, width, height, 0f,
                gameScreen.getGame().resources.assetManager.get( "graphics/tileset_signs.png", Texture.class ),
                BodyDef.BodyType.KinematicBody,
                new UserData("SIGN") );
        this.sleepStage = sleepStage;
        setSignTexture();
    }

    /**
     * Sets clicked attribute to true.
     */
    public void click( ) {
        pauseAnimation();
        this.currentFrame = game.resources.signDestroyed;
        clicked = true;
    }

    /**
     * Chooses right texture for the sign.
     */
    private void setSignTexture( ) {
        if ( sleepStage.equals( "REM" ) ) {
            this.changeAnimation( game.resources.remSignAnimation );
        } else if ( sleepStage.equals( "DEEP" ) ) {
            this.changeAnimation( game.resources.deepSignAnimation );
        }
    }

    @Override
    public void update(float delta) {
        super.update( delta );
        body.setLinearVelocity(gameScreen.getCurrentGameSpeed(), 0);
    }

    @Override
    public void draw( Batch batch) {
        batch.draw(currentFrame,
                body.getPosition().x - currentFrame.getRegionWidth() / 100f / 2,
                currentFrame.getRegionHeight() / 100f / 2,
                currentFrame.getRegionWidth() / 2 / 100f,
                currentFrame.getRegionHeight() / 2 / 100f,
                currentFrame.getRegionWidth() / 100f,
                currentFrame.getRegionHeight() / 100f,
                1.0f,
                1.0f,
                body.getTransform().getRotation() * MathUtils.radiansToDegrees);
    }

    /**
     * @return Is the sign "used"
     */
    public boolean isClicked( ) { return clicked; }

}
