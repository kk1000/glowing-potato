package fi.tamk.tiko.orion.sleeprunner.stages;

/**
 * Stage for the gameplay.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
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

import fi.tamk.tiko.orion.sleeprunner.SleepRunner;
import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.graphics.Background;
import fi.tamk.tiko.orion.sleeprunner.graphics.Background2;
import fi.tamk.tiko.orion.sleeprunner.objects.Enemy;
import fi.tamk.tiko.orion.sleeprunner.objects.GameObject;
import fi.tamk.tiko.orion.sleeprunner.objects.Ground;
import fi.tamk.tiko.orion.sleeprunner.objects.Player;
import fi.tamk.tiko.orion.sleeprunner.utilities.BodyUtils;
import fi.tamk.tiko.orion.sleeprunner.utilities.MapGenerator;
import fi.tamk.tiko.orion.sleeprunner.utilities.WorldUtilities;


/**
 * The stage of the game.
 * Extended from Stage and implements ContactListener.
 */

public class GameStage extends Stage implements ContactListener {

    private final float TIME_STEP = 1 / 300f;
    int[][] chunkGrid = MapGenerator.createIntervalMapChunkGrid();
    private float deathTimer = 2;
    private Array<GameObject> gameObjects = new Array<GameObject>();
    private boolean isDead = false;
    private World world;
    private Player player;
    private float enemyMove = -10;
    private SleepRunner game;
    private float accumulator = 0f;

    private OrthographicCamera camera;
    private Box2DDebugRenderer renderer;

    private Rectangle screenRightSide;
    private Rectangle screenLeftSide;

    private Vector3 touchPoint;

    /**
     * Constructor for the game stage.
     *
     * @param g Game created from the SleepRunner main class
     */
    public GameStage(SleepRunner g) {
        game = g;

        setupWorld();
        setupTouchControlAreas();
        setupCamera();
        renderer = new Box2DDebugRenderer();
    }

    /**
     * Scales given rectangle's size by given amount
     * and returns it.
     *
     * @param rect  The rectangle to scale.
     * @param scale The scale.
     * @return Scaled rectangle.
     */
    public static Rectangle scaleRectangle(Rectangle rect, float scale) {
        Rectangle rectangle = new Rectangle();
        rectangle.x = rect.x * scale;
        rectangle.y = rect.y * scale;
        rectangle.width = rect.width * scale;
        rectangle.height = rect.height * scale;
        return rectangle;
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
        createObjectsToBodies(MapGenerator.generateObjects(chunkGrid, Constants.GROUND_BLOCK, "ground-object"));
        setupPlayer();
        //setupEnemy();
    }

    private void setupMovingBackground(){
        addActor(new Background2());
    }

    private void setupBackground(){
        addActor(new Background());
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
        camera = new OrthographicCamera(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        camera.update();
    }

    /**
     * Creates MapObjects to Box2D bodies.
     *
     * @param mapObjects Created MapObjects in a map chunk.
     */
    public void createObjectsToBodies(MapObjects mapObjects) {
        Array<RectangleMapObject> rectangleMapObjects = mapObjects.getByType(RectangleMapObject.class);
        Gdx.app.log("GameStage", "Creating bodies from " + rectangleMapObjects.size + " rectangle map objects!");
        for (RectangleMapObject rectangleMapObject : rectangleMapObjects) {
            Rectangle pixelRectangle = rectangleMapObject.getRectangle();
            Rectangle meterRectangle = scaleRectangle(pixelRectangle, 2 / 100f);
            float centerX = meterRectangle.getWidth() / 2 + meterRectangle.getX();
            float centerY = meterRectangle.getHeight() / 2 + meterRectangle.getY();
            float width = meterRectangle.getWidth();
            float height = meterRectangle.getHeight();
            if (rectangleMapObject.getName().equals("ground-object")) {
                Gdx.app.log("WorldUtilities", "Ground object!");
                TextureRegion textureRegion = Constants.TILESET_SPRITES[0][0];
                Ground ground = new Ground(WorldUtilities.createGround(world, centerX, centerY, width, height, textureRegion));
                gameObjects.add(ground);
                addActor(ground);
            }
        }
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
          //  if (BodyUtils.bodyIsEnemy(body) &&!player.isHit()){
           //     setupEnemy();
         //   }
            world.destroyBody(body);
            Gdx.app.log("GameStage","Removed body");
        }
    }

    /**
     * Begins the contact-event.
     *
     * @param contact
     */
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

