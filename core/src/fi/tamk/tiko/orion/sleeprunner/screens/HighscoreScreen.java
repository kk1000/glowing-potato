package fi.tamk.tiko.orion.sleeprunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
 * Screen for displaying high scores.
 */
public class HighscoreScreen extends ScreenAdapter {

    public Stage stage;
    private OrthographicCamera camera;
    private OrthographicCamera camera2;
    private SleepRunner game;
    private SpriteBatch batch;
    private float height;
    private float width;
    private Texture logo;
    private float delta;
    private String titleText;
    private BitmapFont titleFont;
    private BitmapFont textFont;
    private float titleX;
    private float titleY;
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

        titleFont = game.resources.titleFont;
        textFont = game.resources.textFont;

        titleText = game.translate.get("top_scores");
        GlyphLayout glyphLayout = new GlyphLayout( titleFont, titleText );
        titleX = Constants.APP_WIDTH/2 - (glyphLayout.width/2);
        titleY = Constants.APP_HEIGHT/2 + (glyphLayout.height/2);

        backButton = new TextButton(game.translate.get("back"), skin);
        backButton.setBounds(width / 12, height / 11, width / 5, height / 7);
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(game.getMainMenuScreen());
            }
        });

        muteButton = new TextButton(game.translate.get("mute"), skin);
        muteButton.setBounds(width * 0.8f, height * 0.8f, width / 9, height / 8);
        muteButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                setMuteButtonState();
                if (prefs.isMuted()) {
                    muteButton.setColor(Color.WHITE);
                    game.resources.unMuteAllSounds();
                } else {
                    muteButton.setColor(Color.RED);
                    game.resources.muteAllSounds();
                }
            }
        });

        stage.addActor(backButton);
        stage.addActor(muteButton);
    }

    /**
     * Sets mute button's state.
     */
    public void setMuteButtonState( ) {
        if(prefs.isMuted()){
            muteButton.setColor(Color.RED);
        } else{
            muteButton.setColor(Color.WHITE);
        }
    }

    /**
     * Refreshes all text in highscore menu (after language change).
     */

    private void refreshTexts(){
        backButton.setText(game.translate.get("back"));
        titleText = game.translate.get("top_scores");
        muteButton.setText(game.translate.get("mute"));
    }

    @Override
    public void show( ) {
        setMuteButtonState();

        if ( prefs.isMuted() ) {
            game.resources.muteAllSounds();
        }
        refreshTexts();
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        game.getCurrentMusic().setVolume(prefs.getMenuMusicVolume());
        Gdx.input.setInputProcessor(stage);
        batch.setProjectionMatrix(camera.combined);
        batch.draw(logo, 0, 0, logo.getWidth(), logo.getHeight());

        titleFont.draw(batch, titleText, titleX, titleY );
        for(int i = 1; i <= 5; i++){
            textFont.draw( batch, i + ". " + Integer.toString(prefs.getHighscore(i)), titleX,
                            titleY - 50 - textFont.getLineHeight()*i);
        }

        batch.setProjectionMatrix(camera2.combined);
        stage.act();
        stage.draw();
        batch.end();
    }

    @Override
    public void dispose() {
        logo.dispose();
    }

}
