package fi.tamk.tiko.orion.sleeprunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fi.tamk.tiko.orion.sleeprunner.SleepRunner;
import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.stages.GameStage;

/**
 * Screen for the game.
 * Uses different stages for the game.
 */

public class GameScreen implements Screen{

    private SpriteBatch batch;

    private float score = 0;
    private BitmapFont scorefont;

    private OrthographicCamera camera;

    private float width;
    private float height;

    private GameStage stage;
    private SleepRunner game;

    /**
     * Constructor for GameScreen.
     *
     * @param g = Game-class from SleepRunner main class
     */
    public GameScreen(SleepRunner g){
        game = g;

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

        batch = game.getBatch();

        scorefont = new BitmapFont(Gdx.files.internal(Constants.GAME_FONT_PATH));
        game = g;
        stage = new GameStage(game);
    }

    @Override
    public void show() {

    }

    /**
     * Render method.
     *
     * @param delta = delta timer (1/60)
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        score += delta*10;
        scorefont.draw(batch, "Score" + ": " + (int)score, width * 0.7f, height * 0.8f);
        stage.draw();
        stage.act();

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
