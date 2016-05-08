package fi.tamk.tiko.orion.sleeprunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import fi.tamk.tiko.orion.sleeprunner.SleepRunner;
import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.Preference;


/**
 * Screen for main menu.
 */
public class MainMenuScreen extends ScreenAdapter {

    public Stage stage;
    private OrthographicCamera camera;
    private OrthographicCamera camera2;
    private SleepRunner game;
    private SpriteBatch batch;
    private float height;
    private float width;
    private float delta;
    private Texture mainMenuBackground;
    private TextButton gameButton;
    private TextButton highscoreButton;

    private SpriteDrawable finnishFlag;
    private SpriteDrawable englishFlag;
    private SpriteDrawable mutebutton;
    private SpriteDrawable mutedbutton;


    private ImageButton muteButton;
    private ImageButton languageButton;
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

        mainMenuBackground = game.resources.menuBackground;

        prefs = new Preference();

        batch = game.getBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        camera2 = new OrthographicCamera();
        camera2.setToOrtho(false, width, height);

        stage = new Stage();
        skin = new Skin(Gdx.files.internal(Constants.SKIN_PATH));
        delta = Gdx.graphics.getDeltaTime();

        finnishFlag = new SpriteDrawable(new Sprite(game.resources.assetManager.get("graphics/flags/finnish.png", Texture.class)));
        englishFlag = new SpriteDrawable(new Sprite(game.resources.assetManager.get("graphics/flags/english.png", Texture.class)));

        mutebutton = new SpriteDrawable(new Sprite(game.resources.assetManager.get("graphics/mutebutton.png", Texture.class)));
        mutedbutton = new SpriteDrawable(new Sprite(game.resources.assetManager.get("graphics/mutedbutton.png", Texture.class)));

        gameButton = new TextButton(game.translate.get("play"), skin);
        gameButton.setBounds(width / 2.9f, height / 4, width / 4, height / 7);
        gameButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                game.switchToGameMusic();
                if (!game.hasSeenGuide() || Constants.FAIR_VERSION ) {
                    Gdx.app.log( "MainMenuScreen", "Setting guide screen instead of game screen." );
                    game.setScreen(game.getGuideScreen());
                } else {
                    game.setScreen(game.getGameScreen());
                }
            }
        });

        highscoreButton = new TextButton(game.translate.get("highscores"), skin);
        highscoreButton.setBounds(width / 2.9f, height / 11, width / 4, height / 7);
        highscoreButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(game.getHighscoreScreen());
            }
        });

        muteButton = new ImageButton(mutebutton, mutebutton, mutedbutton);
        muteButton.setWidth( 100f );
        muteButton.setHeight( 80f );
        muteButton.setX(width - muteButton.getWidth());
        muteButton.setY(height - muteButton.getHeight());

        muteButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                setMuteButtonState();
                if (prefs.isMuted()) {
                    muteButton.setChecked(false);
                    game.resources.unMuteAllSounds();
                } else {
                    muteButton.setChecked(true);
                    game.resources.muteAllSounds();
                }
            }
        });

        languageButton = new ImageButton(finnishFlag,finnishFlag, englishFlag);
        if (game.getLanguage()==1) {
            languageButton.setChecked(true);
        } else {
            languageButton.setChecked(false);
        }
        languageButton.setWidth( 100f );
        languageButton.setHeight( 100f );
        languageButton.setX( 0 );
        languageButton.setY( height - languageButton.getHeight() );
        languageButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                if (game.getLanguage() == 1) {
                    game.changeLanguage(2);
                    languageButton.setChecked(false);
                    refreshTexts();
                } else {
                    game.changeLanguage(1);
                    languageButton.setChecked(true);
                    refreshTexts();
                }
            }
        });

        game.getCurrentMusic().setVolume(prefs.getMenuMusicVolume());

        stage.addActor(highscoreButton);
        stage.addActor(muteButton);
        stage.addActor(gameButton);
        stage.addActor(languageButton);
    }

    /**
     * Refreshes all text in main menu (after language change).
     */
    private void refreshTexts(){
        highscoreButton.setText(game.translate.get("highscores"));
        gameButton.setText(game.translate.get("play"));
    }

    /**
     * Sets mute button's state.
     */
    public void setMuteButtonState( ) {
        if(prefs.isMuted()){
            muteButton.setChecked(true);
        } else{
            muteButton.setChecked(false);
        }
    }

    @Override
    public void show( ) {
        setMuteButtonState();

        if ( prefs.isMuted() ) {
            game.resources.muteAllSounds();
        }
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        Gdx.input.setInputProcessor(stage);
        batch.setProjectionMatrix(camera.combined);
        batch.draw(mainMenuBackground, 0, 0, mainMenuBackground.getWidth(), mainMenuBackground.getHeight());

        batch.setProjectionMatrix(camera2.combined);
        stage.act();
        stage.draw();
        batch.end();
    }

    @Override
    public void dispose( ) {
        stage.dispose();
    }

}

