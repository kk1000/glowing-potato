package fi.tamk.tiko.orion.sleeprunner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.Preference;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;
import fi.tamk.tiko.orion.sleeprunner.screens.HighscoreScreen;
import fi.tamk.tiko.orion.sleeprunner.screens.LaunchScreen;
import fi.tamk.tiko.orion.sleeprunner.screens.MainMenuScreen;

/**
 * SleepRunner is an upcoming sidescroller android game.
 * Learn more about our team from our website.
 *
 * @author   Eetu "mehxit" Järvinen
 * @author   Joni "steiner3k" Korpisalo
 * @author   Sonja Moisio (Graphics)
 * @version  0.0.0
 * @since    0.0.0
 */
public class SleepRunner extends Game {

	private SpriteBatch batch;

	public I18NBundle translate;

	private MainMenuScreen mainMenuScreen;

	private GameScreen gameScreen;

	private HighscoreScreen highscoreScreen;

	private LaunchScreen launchScreen;

    private Preference prefs;

	private Music mainMenuMusic;
	private Music gameMusic;
    private Music currentMusic;

    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    private BitmapFont titleFont;
    private BitmapFont textFont;
    private BitmapFont debugFont;

	private Skin skin;

	@Override
	public void create() {
        mainMenuMusic = Gdx.audio.newMusic( Gdx.files.internal( "sounds/music/mainmenu.mp3" ) );
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal(Constants.GAME_MUSIC_PATH));
        prefs = new Preference();

		Locale defaultLocale = Locale.getDefault();

		batch = new SpriteBatch();

		translate = I18NBundle.createBundle(Gdx.files.internal("localization/languages"),defaultLocale);

		skin  = new Skin(Gdx.files.internal(Constants.SKIN_PATH));

        mainMenuMusic.play();
        mainMenuMusic.setVolume(0f);
        currentMusic = mainMenuMusic;

        generator = new FreeTypeFontGenerator( Gdx.files.internal( "ui/Oswald-Bold.ttf" ) );
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2.0f;
        titleFont = generator.generateFont( parameter );

        generator = new FreeTypeFontGenerator( Gdx.files.internal( "ui/Oswald-Regular.ttf" ) );
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        textFont = generator.generateFont( parameter );

        debugFont = new BitmapFont();

		setLaunchScreen();
	}

    /**
     * Switch music to main menu music.
     */
    public void switchToMainMenuMusic( ) {
        currentMusic.stop();
        currentMusic = mainMenuMusic;
        currentMusic.setVolume( prefs.getMusicVolume() );
        currentMusic.play();
        currentMusic.setLooping(true);
    }


    /**
     * Switch music to the game music.
     */
    public void switchToGameMusic( ) {
        currentMusic.stop();
        currentMusic = gameMusic;
        currentMusic.setVolume( prefs.getMusicVolume() );
        currentMusic.play();
        currentMusic.setLooping(true);
    }

	public SpriteBatch getBatch() {
		return batch;
	}

	public void setLaunchScreen() { setScreen( getLaunchScreen()) ; }

	public void setGameScreen(){
		setScreen( getGameScreen() );
	}

	public void setMainMenuScreen(){
		setScreen( getMainMenuScreen() );
	}

	public void setHighscoreScreen() {setScreen ( getHighscoreScreen() );}

	public MainMenuScreen getMainMenuScreen() {
		if ( mainMenuScreen == null ) {
            Gdx.app.log( "SleepRunner", "Creating new MainMenuScreen instance" );
			mainMenuScreen = new MainMenuScreen( this );
		}
		return mainMenuScreen;
	}

	public GameScreen getGameScreen() {
        if ( gameScreen == null ) {
			Gdx.app.log( "SleepRunner", "Creating new GameScreen instance" );
            gameScreen = new GameScreen( this );
        }
		return gameScreen;
	}

	public HighscoreScreen getHighscoreScreen (){
		if (highscoreScreen == null) {
			Gdx.app.log( "SleepRunner", "Creating new highscoreScreen instance" );
			highscoreScreen = new HighscoreScreen( this );
		}
		return highscoreScreen;
	}

	public LaunchScreen getLaunchScreen(){
		if (launchScreen == null) {
			Gdx.app.log( "SleepRunner", "Creating new launchScreen instance" );
			launchScreen = new LaunchScreen( this );
		}
		return launchScreen;
	}


    public BitmapFont getTitleFont( ) { return titleFont; }
    public BitmapFont getTextFont( ) { return textFont; }
    public BitmapFont getDebugFont( ) { return debugFont; }

    public Music getMainMenuMusic( ) { return mainMenuMusic; }
    public Music getCurrentMusic( ) { return currentMusic; }
	public Music getGameMusic( ) { return gameMusic; }

	public Skin getSkin() { return skin;}

}
