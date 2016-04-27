package fi.tamk.tiko.orion.sleeprunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
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
public class LaunchScreen implements Screen {

    private OrthographicCamera camera;
    private SleepRunner game;
    private SpriteBatch batch;
    private Texture logo;
    private Preference prefs;
    private float height;
    private float width;
    private float timer;
    private int currentLogo;
    /**
     * Constructor for main menu.
     *
     * @param g = Game created from SleepRunner main class
     */
    public LaunchScreen(SleepRunner g){
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        game = g;

        prefs = new Preference();

        batch = game.getBatch();

        currentLogo = 0;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.APP_WIDTH, Constants.APP_HEIGHT);

        timer = Gdx.graphics.getDeltaTime();

        logo = new Texture(Gdx.files.internal(Constants.MAINMENU_LOGO_IMAGE_PATH));

        game.getMusic().setVolume(prefs.getMusicVolume());

        // Screen starts with TAMK-logo
        logo = new Texture (Gdx.files.internal(Constants.LAUNCH_LOGO_TAMK_IMAGE_PATH));
        currentLogo = 1;
        timer = 0;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        changeLogo();
        batch.setProjectionMatrix(camera.combined);
        batch.draw(logo, width / 2 - (logo.getWidth()/2), height/2 - (logo.getHeight()/2), logo.getWidth(), logo.getHeight());

        batch.end();
    }

    /**
     * Changes logo after a set time or when user touches the screen.
     */
    public void changeLogo(){
        timer += Gdx.graphics.getDeltaTime();
        // TIKO Logo
        if((timer > 0.8  && currentLogo == 1) ||  (Gdx.input.justTouched() && currentLogo == 1 &&  timer > 0.1)){
            logo = new Texture (Gdx.files.internal(Constants.LAUNCH_LOGO_TIKO_IMAGE_PATH));
            timer = 0;
            currentLogo = 2;

        }
        // UKK Logo
        if((timer > 0.8 && currentLogo == 2)  || (Gdx.input.justTouched() && currentLogo == 2 && timer > 0.1)){
            logo = new Texture (Gdx.files.internal(Constants.LAUNCH_LOGO_UKK_IMAGE_PATH));
            timer = 0;
            currentLogo = 3;

        }
        // PETO Logo
        if((timer > 0.8 && currentLogo ==3)  ||  (Gdx.input.justTouched() && currentLogo == 3 && timer > 0.1)){
            logo = new Texture (Gdx.files.internal(Constants.LAUNCH_LOGO_PETO_IMAGE_PATH));
            timer = 0;
            currentLogo = 4;

        }
        if((timer > 0.8 && currentLogo == 4) || (Gdx.input.justTouched() && currentLogo == 4 && timer > 0.1)){
            game.setMainMenuScreen();
        }
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
