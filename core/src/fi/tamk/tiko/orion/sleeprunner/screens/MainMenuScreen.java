package fi.tamk.tiko.orion.sleeprunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
 * Screen for main menu.
 */
public class MainMenuScreen implements Screen{

    public Stage stage;
    private OrthographicCamera camera;
    private OrthographicCamera camera2;
    private SleepRunner game;
    private SpriteBatch batch;
    private float height;
    private float width;
    private Texture logo;
    private float delta;
    private TextButton gameButton;
    private TextButton muteButton;
    private TextButton highscoreButton;
    private Skin skin;
    private Preference prefs;


    /**
     * Constructor for main menu.
     *
     * @param g = Game created from SleepRunner main class
     */
    public MainMenuScreen(SleepRunner g){
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

        gameButton = new TextButton(game.translate.get("play"), skin);
        gameButton.setBounds(width / 2.9f, height / 4, width / 4, height / 7);
        gameButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                game.switchToGameMusic();
                game.setGameScreen();
            }
        });

        highscoreButton = new TextButton(game.translate.get("highscores"), skin);
        highscoreButton.setBounds(width / 2.9f, height / 11, width / 4, height / 7);
        highscoreButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                game.setHighscoreScreen();
            }
        });

        muteButton = new TextButton(game.translate.get("mute"), skin);
        muteButton.setBounds(width * 0.8f, height * 0.8f, width / 9, height / 8);
        muteButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                    prefs.setMuted();
                        if(prefs.getMuted()){
                            muteButton.setColor(Color.RED);
                        } else{
                            muteButton.setColor(Color.WHITE);
                        }
            }
        });

        stage.addActor(highscoreButton);
        stage.addActor(muteButton);
        stage.addActor(gameButton);
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
        game.getCurrentMusic().setVolume(prefs.getMusicVolume());
        Gdx.input.setInputProcessor(stage);
        batch.setProjectionMatrix(camera.combined);
        batch.draw(logo, 0, 0, logo.getWidth(), logo.getHeight());

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

