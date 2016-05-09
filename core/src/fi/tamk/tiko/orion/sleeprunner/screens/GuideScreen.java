package fi.tamk.tiko.orion.sleeprunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import fi.tamk.tiko.orion.sleeprunner.SleepRunner;
import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.Preference;

/**
 * Game's guide screen, shows instructions to the game.
 *
 * @author   Eetu "mehxit" Järvinen
 * @author   Joni "steiner3k" Korpisalo
 */
public class GuideScreen extends ScreenAdapter {

    private OrthographicCamera camera;
    private SleepRunner sleepRunner;
    private SpriteBatch batch;
    private Preference prefs;

    private GlyphLayout glyphLayout;
    private String guideThreeText;
    private String guideTwoText;
    private String guideOneText;

    private TextureRegion guideThree;
    private TextureRegion guideTwo;
    private TextureRegion guideOne;

    private TextureRegion currentGuide;
    private String currrentGuideText;
    private int currentGuideNumber;

    /**
     * Constructor for GuideScreen.
     *
     * @param sleepRunner SleepRunner reference.
     */
    public GuideScreen(SleepRunner sleepRunner){
        this.sleepRunner = sleepRunner;
        this.prefs = new Preference();
        this.batch = sleepRunner.getBatch();

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Constants.APP_WIDTH, Constants.APP_HEIGHT);

        this.guideThreeText = sleepRunner.translate.get( "guide_three_text" );
        this.guideTwoText = sleepRunner.translate.get( "guide_two_text" );
        this.guideOneText = sleepRunner.translate.get( "guide_one_text" );

        this.glyphLayout = new GlyphLayout( this.sleepRunner.resources.titleFont, guideOneText );

        this.guideThree = sleepRunner.resources.guideSet[1][0];
        this.guideTwo = sleepRunner.resources.guideSet[0][1];
        this.guideOne = sleepRunner.resources.guideSet[0][0];
    }

    /**
     * Switches to next guide image, there are total of three images.
     */
    public void changeGuide( ) {
        if ( currentGuideNumber == 1 ) {
            currentGuide = guideOne;
            currrentGuideText = guideOneText;
            glyphLayout.setText(sleepRunner.resources.titleFont, guideOneText);
        } else if ( currentGuideNumber == 2 ) {
            // Show second guide.
            Gdx.app.log( "GuideScreen", "Changing to second guide.");
            currentGuide = guideTwo;
            currrentGuideText = guideTwoText;
            glyphLayout.setText( sleepRunner.resources.titleFont, guideTwoText );
        } else if ( currentGuideNumber == 3 ) {
            // Show third guide.
            Gdx.app.log( "GuideScreen", "Changing to third guide." );
            currentGuide = guideThree;
            currrentGuideText = guideThreeText;
            glyphLayout.setText( sleepRunner.resources.titleFont, guideThreeText );
        } else if ( currentGuideNumber == 4 ) {
            // We're done, exit from this screen to the game screen.
            Gdx.app.log("GuideScreen", "Exiting guide screen to the game screen.");
            sleepRunner.setScreen(sleepRunner.getGameScreen());
        }
    }

    @Override
    public void show( ) {
        sleepRunner.setSeenGuide( true );
        currentGuideNumber = 1;
        changeGuide();
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Change guide by touch.
        if ( Gdx.input.justTouched() ) {
            currentGuideNumber++;
            changeGuide();
        }

        batch.begin();

        // Draw current guide image.
        batch.draw(currentGuide,
                Constants.APP_WIDTH / 2 - currentGuide.getRegionWidth() / 2,
                Constants.APP_HEIGHT / 2 - currentGuide.getRegionHeight() / 2,
                currentGuide.getRegionWidth(),
                currentGuide.getRegionHeight()
        );

        // Draw current guide text.
        sleepRunner.resources.titleFont.draw( batch,
                currrentGuideText,
                Constants.APP_WIDTH/2 - glyphLayout.width/2,
                glyphLayout.height * 2 );

        batch.end();
    }

}
