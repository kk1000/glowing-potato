package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.EnemyUserData;

/**
 * Enemy/obstacle actor class.
 * Extended from GameObject.
 */
public class Enemy extends GameObject {

    private EnemyUserData userData;

    /**
     * Constructor for Enemy object.
     *
     * @param world Box2D world.
     */
    public Enemy(World world, float x, float y, float width, float height) {
        super(world, x, y, width, height,
                Constants.ENEMY_DENSITY,
                new Texture(Gdx.files.internal("")),
                1,
                5,
                1 / 60, BodyDef.BodyType.KinematicBody);
        userData = new EnemyUserData(Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);
        body.setUserData(userData);
    }

}
