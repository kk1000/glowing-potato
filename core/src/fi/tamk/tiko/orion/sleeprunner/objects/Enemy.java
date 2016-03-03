package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;

import fi.tamk.tiko.orion.sleeprunner.data.EnemyUserData;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;

/**
 * Enemy/obstacle actor class.
 * Extended from GameObject.
 */
public class Enemy extends GameObject {

    public Texture texture;
    private String path;

    /**
     * Constructor for Enemy.
     *
     * @param body = body of the enemy/obstacle object
     */
    public Enemy(Body body){
        super(body);
        path = getUserData().getTexturepath();
        texture = new Texture(Gdx.files.internal(path));
    }

    /**
     * @return enemy user data
     */
    @Override
    public UserData getUserData() {
        return (EnemyUserData) userData;
    }

    /**
     * Draw method.
     *
     * @param batch = sprite batch
     * @param parentAlpha = global alpha level
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(texture, (screenRectangle.x - (screenRectangle.width * 0.1f)),
                screenRectangle.y, screenRectangle.width * 1.2f, screenRectangle.height * 1.1f);
    }

    /**
     * Act method.
     *
     * @param delta = delta timer (1/60)
     */
    @Override
    public void act(float delta){
        super.act(delta);
        body.setLinearVelocity(getUserData().getLinearVelocity());
    }


}
