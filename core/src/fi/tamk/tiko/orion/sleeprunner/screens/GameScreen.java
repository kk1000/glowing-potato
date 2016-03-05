package fi.tamk.tiko.orion.sleeprunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fi.tamk.tiko.orion.sleeprunner.SleepRunner;
import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.stages.BackgroundStage;
import fi.tamk.tiko.orion.sleeprunner.stages.GameStage;

/**
 * Screen for the game.
 * Uses different stages for the game.
 */

public class GameScreen implements Screen{

    private SpriteBatch batch;

    private float score = 0;
    private BitmapFont scoreFont;

    private OrthographicCamera uiCamera;
    private OrthographicCamera camera;
    private OrthographicCamera backgroundCamera;

    private GameStage gameStage;
    private BackgroundStage backgroundStage;
    private SleepRunner game;

    /**
     * Constructor for GameScreen.
     *
     * @param g = Game-class from SleepRunner main class
     */
    public GameScreen(SleepRunner g){
        game = g;

        backgroundCamera = new OrthographicCamera();
        backgroundCamera.setToOrtho(false, Constants.APP_WIDTH, Constants.APP_HEIGHT);

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Constants.APP_WIDTH, Constants.APP_HEIGHT);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

        batch = game.getBatch();

        scoreFont = new BitmapFont(Gdx.files.internal(Constants.GAME_FONT_PATH));
        game = g;
        gameStage = new GameStage(game, camera, batch);
        backgroundStage = new BackgroundStage(game, backgroundCamera, batch);
    }

    @Override
    public void show() {

    }

    /**
     * Render method.
     *
     * @param delta Delta timer (1/60)
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Backgrounds

        backgroundStage.getViewport().apply();
        backgroundStage.act();
        backgroundStage.draw();


        // The game itself

        gameStage.getViewport().apply();
        gameStage.act();
        gameStage.draw();


        // UI.
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();
        score += delta * 10;
        scoreFont.draw(batch, "Score:" + (int) score, Constants.WORLD_TO_SCREEN, Constants.APP_HEIGHT - 10);
        batch.end();
    }


    /**
     * Method for resizing.
     *
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height) {

        backgroundStage.getViewport().update(width, height, false);
        gameStage.getViewport().update(width, height, true);
        
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}
