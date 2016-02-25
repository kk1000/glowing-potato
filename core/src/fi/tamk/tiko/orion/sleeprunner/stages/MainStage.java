package fi.tamk.tiko.orion.sleeprunner.stages;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Superclass of the every other stages.
 * Not designed to construct object from this class.
 */
public abstract class MainStage extends Stage implements ContactListener {

    /**
     * Constructor.
     *
     */
    public MainStage( ) {

    }

    /**
     * Methods overwritten from the Stage class.
     */

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) { return true; }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) { return true; }


    @Override
    public void act(float delta) { }

    @Override
    public void draw() { }

    /**
     * Methods overwritten from the ContactListener interface.
     */

    @Override
    public void beginContact(Contact contact) { }

    @Override
    public void endContact(Contact contact) { }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) { }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) { }

}
