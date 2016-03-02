package fi.tamk.tiko.orion.sleeprunner.objects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.UserData;

/**
 * Superclass of every game object.
 * Not designed to construct object from this class.
 */
public abstract class GameObject extends Actor {

    protected Body body;
    protected UserData userData;
    protected Rectangle screenRectangle;

    public GameObject() {

    }

    /**
     * Constructor for game objects.
     * @param body used for updating.
     */
    public GameObject(Body body) {
        this.body = body;
        this.userData = (UserData) body.getUserData();
        screenRectangle = new Rectangle();
    }

    /**
     * Act method, updates body's rectangle if it's not null (outside of screen).
     * @param delta = delta timer (1/60)
     */
    @Override
    public void act(float delta) {
        super.act(delta);
        if (body.getUserData() != null) {
            updateRectangle();
        } else {
            remove();
        }
    }

    /**
     * @return user data
     */
    public abstract UserData getUserData();

    /**
     * Updates rectangle with transformToScreen method, which multiplies the metric values with WORLD_TO_SCREEN (from Constants) attribute.
     */
    private void updateRectangle() {
        screenRectangle.x = transformToScreen(body.getPosition().x - userData.getWidth() / 2);
        screenRectangle.y = transformToScreen(body.getPosition().y - userData.getHeight() / 2);
        screenRectangle.width = transformToScreen(userData.getWidth());
        screenRectangle.height = transformToScreen(userData.getHeight());
    }

    /**
     * Transforms the metric values to screen values with WORLD_TO_SCREEN attribute.
     * @param n = metric value from updateRectangle-method
     * @return the multiplied value
     */
    protected float transformToScreen(float n) {
        return Constants.WORLD_TO_SCREEN * n;
    }
}