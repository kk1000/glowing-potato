package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;
import fi.tamk.tiko.orion.sleeprunner.utilities.Tools;

/**
 * Sign object is a sign telling player which sleep stage is next.
 */
public class SignObject extends AnimatedGameObject {


    /**
     * Tiles (texture regions) for different signs.
     */

    public static Texture DEEP_TEXTURE = new Texture( Gdx.files.internal( "graphics/sign_deep.png" ) );
    public static Texture REM_TEXTURE = new Texture( Gdx.files.internal( "graphics/sign_rem.png" ) );
    public static Texture CLICKED_TEXTURE = new Texture( Gdx.files.internal( "graphics/sign_clicked.png" ) );

    public static Animation REM_ANIMATION = Tools.createAnimation( REM_TEXTURE, 2, 1, 1, 2, 1/5f );
    public static Animation DEEP_ANIMATION = Tools.createAnimation( DEEP_TEXTURE, 2, 1, 1, 2, 1/5f );

    private boolean clicked = false;

    private String sleepStage;

    /**
     * Constructor for sign object.
     *
     * @param world        Box2D World
     * @param x            X-position.
     * @param y            Y-position.
     * @param width        Width of the body.
     * @param height       Height of the body.
     * @param sleepStage   What sleep stage does the sign represent.
     */
    public SignObject(World world, float x, float y, float width, float height, String sleepStage ) {
        super(world, x, y, width, height, 0f, DEEP_TEXTURE, BodyDef.BodyType.KinematicBody, new UserData("SIGN") );
        this.sleepStage = sleepStage;
        setSignTexture();
    }

    /**
     * Sets clicked attribute to true.
     */
    public void click( ) {
        pauseAnimation();
        this.currentFrame = new TextureRegion( CLICKED_TEXTURE );
        clicked = true;
    }

    /**
     * Chooses right texture for the sign.
     */
    private void setSignTexture( ) {
        if ( sleepStage.equals( "REM" ) ) {
            this.changeAnimation( REM_ANIMATION );
        } else if ( sleepStage.equals( "DEEP" ) ) {
            this.changeAnimation( DEEP_ANIMATION );
        }
    }


    @Override
    public void update(float delta) {
        super.update( delta );
        body.setLinearVelocity(GameScreen.CURRENT_GAME_SPEED);
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
     * @return clicked Is the sign clicked.
     */
    public boolean isClicked( ) { return clicked; }

}
