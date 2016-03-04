package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.GroundUserData;

/**
 * Ground actor class.
 * Ground texture is split to two textureregions for the endless movement visual effect.
 * Extended from GameObject.
 */
public class Ground extends GameObject {

    private GroundUserData userData;

    /**
     * Constructor for Ground.
     *
     * @param world     Box2D World
     * @param x         X-position.
     * @param y         Y-position.
     * @param width     Width.
     * @param height    Height.
     */
    public Ground(World world, float x, float y, float width, float height, TextureRegion textureRegion) {
        super(world, x, y, width, height, 0f, textureRegion, BodyDef.BodyType.KinematicBody);
        userData = new GroundUserData(width, height);
        body.setUserData(userData);
        Gdx.app.log("Ground", "Created " + width + "x" + height + " ground to pos " + x + ", " + y);
    }

}
