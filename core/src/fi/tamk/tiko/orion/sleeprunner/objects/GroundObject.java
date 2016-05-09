package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.UserData;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;

/**
 * Ground object, its width is random.
 *
 * @author   Eetu "mehxit" Järvinen
 * @author   Joni "steiner3k" Korpisalo
 */
public class GroundObject extends GameObject {

    /**
     * Constructor for ground object.
     *
     * @param gameScreen   GameScreen reference.
     * @param world        Box2D World
     * @param x            X-position.
     * @param y            Y-position.
     * @param width        Width of the body.
     * @param height       Height of the body.
     */
    public GroundObject( GameScreen gameScreen, World world, float x, float y, float width, float height) {
        super(gameScreen, world, x, y, width, height, 0f, new TextureRegion(), BodyDef.BodyType.KinematicBody, new UserData("GROUND"));
    }

    @Override
    public void update(float delta) {
        super.update( delta );
        updateTiles(delta);
    }

}