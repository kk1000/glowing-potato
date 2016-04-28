package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;

/**
 * Sign object is a sign telling player which sleep stage is next.
 */
public class SignObject extends GameObject {


    /**
     * Tiles (texture regions) for different signs.
     */

    public static final TextureRegion DEEPSLEEP_TEXTURE = Constants.TILESET_SPRITES[4][2];
    public static final TextureRegion REMSLEEP_TEXTURE = Constants.TILESET_SPRITES[4][1];
    public static final TextureRegion EMPTY_TEXTURE = Constants.TILESET_SPRITES[4][0];

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
        super(world, x, y, width, height, 0f, Constants.TILESET_SPRITES[0][4], BodyDef.BodyType.KinematicBody, new UserData("SIGN") );
        this.sleepStage = sleepStage;
        setSignTexture();
    }

    /**
     * Chooses right texture for the sign.
     */
    private void setSignTexture( ) {
        if ( sleepStage.equals( "REM" ) ) {
            this.textureRegion = REMSLEEP_TEXTURE;
        } else if ( sleepStage.equals( "DEEP" ) ) {
            this.textureRegion = DEEPSLEEP_TEXTURE;
        } else {
            this.textureRegion = EMPTY_TEXTURE;
        }
    }

    @Override
    public void draw( Batch batch ) {
        float half = Constants.WORLD_TO_SCREEN/100f/2;
        float x = body.getPosition().x - half;
        float y = body.getPosition().y - half;
        batch.draw( textureRegion,
                x,
                y,
                Constants.WORLD_TO_SCREEN / 2,
                Constants.WORLD_TO_SCREEN / 2,
                Constants.WORLD_TO_SCREEN / 100f,
                Constants.WORLD_TO_SCREEN / 100f,
                1.0f,
                1.0f,
                0);
    }

    @Override
    public void update(float delta) {
        body.setLinearVelocity(GameScreen.CURRENT_GAME_SPEED);
    }


}
