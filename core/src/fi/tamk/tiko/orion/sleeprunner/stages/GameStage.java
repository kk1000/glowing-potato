package fi.tamk.tiko.orion.sleeprunner.stages;

/**
 * Stage for the gameplay.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import fi.tamk.tiko.orion.sleeprunner.SleepRunner;
import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.graphics.Background;
import fi.tamk.tiko.orion.sleeprunner.graphics.Background2;
import fi.tamk.tiko.orion.sleeprunner.objects.Enemy;
import fi.tamk.tiko.orion.sleeprunner.objects.Ground;
import fi.tamk.tiko.orion.sleeprunner.objects.Player;
import fi.tamk.tiko.orion.sleeprunner.utilities.BodyUtils;
import fi.tamk.tiko.orion.sleeprunner.utilities.WorldUtilities;


/**
 * The stage of the game.
 * Extended from Stage and implements ContactListener.
 */

public class GameStage extends Stage implements ContactListener {

    private static final float VIEWPORT_WIDTH = Constants.APP_WIDTH;
    private static final float VIEWPORT_HEIGHT = Constants.APP_HEIGHT;

    private float deathTimer = 2;
    private boolean isDead = false;

    private World world;
    private Ground ground;
    private Player player;

    private float enemyMove =-10;

    private SleepRunner game;

    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;

    private OrthographicCamera camera;
    private Box2DDebugRenderer renderer;

    private Rectangle screenRightSide;
    private Rectangle screenLeftSide;

    private Vector3 touchPoint;

    /**
     * Constructor for the game stage.
     *
     * @param g = Game created from the SleepRunner main class
     */
    public GameStage(SleepRunner g) {

        super(new ScalingViewport(Scaling.stretch, VIEWPORT_WIDTH, VIEWPORT_HEIGHT, new OrthographicCamera(VIEWPORT_WIDTH,VIEWPORT_HEIGHT)));

        game = g;

        setupWorld();
        setupTouchControlAreas();
        setupCamera();
        renderer = new Box2DDebugRenderer();
    }

    /**
     * Creates rectangles for both halves of the screen used for controls.
     */
    private void setupTouchControlAreas(){
        touchPoint = new Vector3();
        screenRightSide = new Rectangle(getCamera().viewportWidth / 2f, 0f, getCamera().viewportWidth / 2f, getCamera().viewportHeight);
        screenLeftSide = new Rectangle(0,0,getCamera().viewportWidth / 2, getCamera().viewportHeight);
        Gdx.input.setInputProcessor(this);
    }

    /**
     * Setups all objects used in game.
     */
    private void setupWorld(){

        world = WorldUtilities.createWorld();
        world.setContactListener(this);



        setupBackground();
        setupMovingBackground();
        setupGround();
        setupPlayer();
        setupEnemy();

    }

    private void setupMovingBackground(){
        addActor(new Background2());
    }

    private void setupBackground(){
        addActor(new Background());
    }

    private void setupGround(){
        ground = new Ground(WorldUtilities.createGround(world));
        addActor(ground);
    }

    private void setupPlayer(){
        player = new Player(WorldUtilities.createPlayer(world));
        addActor(player);
    }

    private void setupEnemy(){
        Enemy enemy = new Enemy(WorldUtilities.createEnemy(world));
        addActor(enemy);

    }
    private void setupCamera() {
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        camera.update();
    }

    /**
     * Method for touch down event.
     * Translates touched coordinates to the world.
     * Jumps if right side of screen is touched, dodges if left side of screen is touched.
     *
     * @param x
     * @param y
     * @param pointer
     * @param button
     * @return the position of the touch
     */
    @Override
    public boolean touchDown(int x, int y, int pointer, int button){

        translateScreenToWorldCoordinates(x,y);

        if(rightSideTouched(touchPoint.x,touchPoint.y)){
            player.jump();
        }
        else  if(leftSideTouched(touchPoint.x,touchPoint.y)){
            player.dodge();
        }
        return super.touchDown(x,y,pointer,button);
    }

    /**
     * Method for touch up event.
     *
     * @param screenX
     * @param screenY
     * @param pointer
     * @param button
     * @return the position of the release
     */
    public boolean touchUp(int screenX, int screenY, int pointer, int button){

        if (player.isDodging()){
            player.stopDodge();
        }

        return super.touchUp(screenX, screenY, pointer, button);
    }

    /**
     * Checks if right side of screen is touched.
     *
     * @param x
     * @param y
     * @return boolean
     */
    private boolean rightSideTouched(float x, float y) {
        return screenRightSide.contains(x, y);
    }

    /**
     * Checks if left side of screen is touched.
     *
     * @param x
     * @param y
     * @return boolean
     */
    private boolean leftSideTouched(float x, float y) {
        return screenLeftSide.contains(x, y);
    }

    /**
     * Translates the x and y position of touch to world coordinates.
     *
     * @param x = x-position of touch
     * @param y = y-position of touch
     */
    private void translateScreenToWorldCoordinates(int x, int y) {
        getCamera().unproject(touchPoint.set(x, y, 0));
    }

    /**
     * Act method.
     *
     * @param delta = delta timer (1/60)
     */
    @Override
    public void act(float delta) {
        super.act(delta);


        Array<Body> bodies = new Array<Body>(world.getBodyCount());
        world.getBodies(bodies);

        for(int i = 0; i < bodies.size; i++){
            update(bodies.get(i));
        }
        enemyMove -= delta*Constants.ENEMY_SPEED;



        Constants.ENEMY_LINEAR_VELOCITY.set(enemyMove, 0);

        // Fixed timestep
        accumulator += delta;

        while (accumulator >= delta) {
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }


        if(isDead){
            deathTimer -= delta;
        }
        if(deathTimer <= 0){
            game.setMainMenuScreen();
        }


    }

    /**
     * Draw method.
     */
    @Override
    public void draw() {
        super.draw();
        renderer.render(world, camera.combined);
    }

    /**
     * Removes enemy/obstacle body from the world if its outside of screen and creates a new one.
     *
     * @param body = body of the enemy/obstacle
     */
    public void update(Body body){
        if(!BodyUtils.bodyInBounds(body)) {
            if (BodyUtils.bodyIsEnemy(body) &&!player.isHit()){
                setupEnemy();
            }
            world.destroyBody(body);
        }

    }

    @Override
    public void beginContact(Contact contact) {

        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        if((BodyUtils.bodyIsPlayer(a)&& BodyUtils.bodyIsEnemy(b)) || (BodyUtils.bodyIsPlayer(b) && BodyUtils.bodyIsEnemy(a))){
            player.hit();
            isDead = true;
            enemyMove = -10;
            Constants.ENEMY_LINEAR_VELOCITY.set(-enemyMove,0);
        }

        else if ((BodyUtils.bodyIsPlayer(a) && BodyUtils.bodyIsGround(b)) || (BodyUtils.bodyIsGround(a)&& BodyUtils.bodyIsPlayer(b))){
            player.landed();
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}

