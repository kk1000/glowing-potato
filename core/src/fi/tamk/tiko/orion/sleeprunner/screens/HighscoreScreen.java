package fi.tamk.tiko.orion.sleeprunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import fi.tamk.tiko.orion.sleeprunner.SleepRunner;
import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.Preference;
import fi.tamk.tiko.orion.sleeprunner.graphics.GraphicButton;

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
    private Texture menuBackground;
    private float delta;
    private String titleText;
    private BitmapFont titleFont;
    private BitmapFont textFont;
    private float titleX;
    private float titleY;
    private TextButton backButton;
    private GraphicButton muteButton;

    private SpriteDrawable mutebutton;
    private SpriteDrawable mutedbutton;

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

        menuBackground = game.resources.menuBackground;

        titleFont = game.resources.titleFont;
        textFont = game.resources.textFont;

        titleText = game.translate.get("top_scores");
        GlyphLayout glyphLayout = new GlyphLayout( titleFont, titleText );
        titleX = Constants.APP_WIDTH/2 - (glyphLayout.width/2);
        titleY = Constants.APP_HEIGHT/2 + (glyphLayout.height/2);

        backButton = new TextButton(game.translate.get("back"), skin);
        backButton.setWidth(100f);
        backButton.setHeight(80f);
        backButton.setX(0);
        backButton.setY(height - backButton.getHeight());
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(game.getMainMenuScreen());
            }
        });

        mutebutton = new SpriteDrawable(new Sprite(game.resources.assetManager.get("graphics/mutebutton.png", Texture.class)));
        mutedbutton = new SpriteDrawable(new Sprite(game.resources.assetManager.get("graphics/mutedbutton.png", Texture.class)));

        muteButton = new GraphicButton(mutebutton, mutebutton, mutedbutton);
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

        stage.addActor(backButton);
        stage.addActor(muteButton);
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

    /**
     * Refreshes all text in highscore menu (after language change).
     */
    private void refreshTexts(){
        backButton.setText(game.translate.get("back"));
        titleText = game.translate.get("top_scores");
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

        batch.draw(menuBackground, 0, 0, menuBackground.getWidth(), menuBackground.getHeight());

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
    public void dispose( ) {
        stage.dispose();
    }

}
