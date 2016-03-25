package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;

/**
 * Sign object is a sign telling player which sleep stage is next.
 */
public class SignObject extends GameObject {

    private GlyphLayout glyphLayout;
    private BitmapFont font;
    private String text;

    /**
     * Constructor for sign object.
     *
     * @param world        Box2D World
     * @param x            X-position.
     * @param y            Y-position.
     * @param width        Width of the body.
     * @param height       Height of the body.
     * @param text         Text of the sign.
     */
    public SignObject(World world, float x, float y, float width, float height, BitmapFont font, String text ) {
        super(world, x, y, width, height, 0f, Constants.TILESET_SPRITES[0][0], BodyDef.BodyType.KinematicBody, new UserData("SIGN") );
        this.font = font;
        this.text = text;
        this.glyphLayout = new GlyphLayout( this.font, this.text );
    }

    /**
     * Calculates sign's text position.
     *
     * @param bodyX Body's x position.
     * @param bodyY Body's y position.
     * @return x and y in an integer array
     */
    private int[] calculateTextPositions( float bodyX, float bodyY ) {
        int[] pos = new int[ 2 ];
        pos[ 0 ] = (int) ( ( bodyX * 100f ) + glyphLayout.width/2 );
        pos[ 1 ] = (int) ( ( bodyY * 100f ) - glyphLayout.height/2 );
        return pos;
    }

    @Override
    public void draw( Batch batch ) {
        float bodyX = body.getPosition().x;
        float bodyY = body.getPosition().y;
        batch.draw( textureRegion,
                bodyX,
                bodyY,
                Constants.WORLD_TO_SCREEN / 2,
                Constants.WORLD_TO_SCREEN / 2,
                Constants.WORLD_TO_SCREEN / 100f,
                Constants.WORLD_TO_SCREEN / 100f,
                1.0f,
                1.0f,
                0);
        int[] textPositions = calculateTextPositions( bodyX, bodyY );
        //batch.setProjectionMatrix(  );
        //font.draw( batch, text, textPositions[0], textPositions[1] );
    }

    @Override
    public void update(float delta) {
        body.setLinearVelocity(Constants.ENEMY_LINEAR_VELOCITY);
    }


}
