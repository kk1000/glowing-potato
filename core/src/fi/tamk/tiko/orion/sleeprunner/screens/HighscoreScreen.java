package fi.tamk.tiko.orion.sleeprunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fi.tamk.tiko.orion.sleeprunner.SleepRunner;
import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.Preference;

/**
 * Created by joni on 18/04/2016.
 */
public class HighscoreScreen implements Screen {

    public Stage stage;
    private OrthographicCamera camera;
    private OrthographicCamera camera2;
    private SleepRunner game;
    private SpriteBatch batch;
    private float height;
    private float width;
    private BitmapFont scoreFont;
    private Texture logo;
    private float delta;
    private TextButton backButton;
    private TextButton muteButton;
    private Skin skin;
    private Preference prefs;


    /**
     * Constructor for main menu.
     *
     * @param g = Game created from SleepRunner main class
     */
    public HighscoreScreen(SleepRunner g){
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        game = g;

        prefs = new Preference();

        batch = game.getBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        camera2 = new OrthographicCamera();
        camera2.setToOrtho(false, width, height);

        stage = new Stage();
        skin = new Skin(Gdx.files.internal(Constants.SKIN_PATH));
        delta = Gdx.graphics.getDeltaTime();

        logo = new Texture(Gdx.files.internal(Constants.MAINMENU_LOGO_IMAGE_PATH));
        scoreFont = new BitmapFont(Gdx.files.internal(Constants.GAME_FONT_PATH));

        backButton = new TextButton(game.translate.get("back"), skin);
        backButton.setBounds(width / 12, height / 11, width / 5, height / 7);
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                Gdx.app.log("TAG", "Clicked menu");
                game.setMainMenuScreen();
            }
        });

        muteButton = new TextButton(game.translate.get("mute"), skin);
        muteButton.setBounds(width * 0.8f, height * 0.8f, width / 9, height / 8);
        muteButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                prefs.setMuted();
                if (prefs.getMuted()) {
                    muteButton.setColor(Color.RED);
                } else {
                    muteButton.setColor(Color.WHITE);
                }
            }
        });


        stage.addActor(backButton);
        stage.addActor(muteButton);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        game.getMusic().setVolume(prefs.getMusicVolume());
        Gdx.input.setInputProcessor(stage);
        batch.setProjectionMatrix(camera.combined);
        batch.draw(logo, 0, 0, logo.getWidth(), logo.getHeight());

        scoreFont.draw(batch, game.translate.get("top_scores"), width/2.8f, height/2f);
        for(int i = 1; i <= 5; i++){
            scoreFont.draw( batch, Integer.toString(prefs.getHighscore(i)), width/2.1f,
                            height*0.4f - scoreFont.getLineHeight()*i);
        }

        batch.setProjectionMatrix(camera2.combined);
        stage.act();
        stage.draw();
        batch.end();
    }

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
        logo.dispose();
    }

}
