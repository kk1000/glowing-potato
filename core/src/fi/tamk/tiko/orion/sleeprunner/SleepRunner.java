package fi.tamk.tiko.orion.sleeprunner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

import fi.tamk.tiko.orion.sleeprunner.data.Preference;
import fi.tamk.tiko.orion.sleeprunner.data.Resources;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;
import fi.tamk.tiko.orion.sleeprunner.screens.GuideScreen;
import fi.tamk.tiko.orion.sleeprunner.screens.HighscoreScreen;
import fi.tamk.tiko.orion.sleeprunner.screens.LaunchScreen;
import fi.tamk.tiko.orion.sleeprunner.screens.MainMenuScreen;


/**
 * SleepRunner is an upcoming sidescroller android game.
 * Learn more about our team from our website.
 *
 * http://privaservu.com/Orion/index_en.html
 *
 * @author   Eetu "mehxit" Järvinen
 * @author   Joni "steiner3k" Korpisalo
 * @author   Sonja Moisio (Graphics)
 * @version  1.0.0
 * @since    0.0.0
 */
public class SleepRunner extends Game {

    public I18NBundle translate;
    public Resources resources;

    //1 = finnish 2 = english
    private int language;

    private HighscoreScreen highscoreScreen;
	private MainMenuScreen mainMenuScreen;
    private LaunchScreen launchScreen;
    private GuideScreen guideScreen;
    private GameScreen gameScreen;

    private Preference prefs;
    private SpriteBatch batch;
    private Music currentMusic;

    private boolean seenGuide = false;

	@Override
	public void create() {
        prefs = new Preference();

		Locale defaultLocale = Locale.getDefault();

		batch = new SpriteBatch();

        resources = new Resources();

        resources.mainMenuMusic.play();
        resources.mainMenuMusic.setLooping(true);
        resources.mainMenuMusic.setVolume(0f);
        currentMusic = resources.mainMenuMusic;

        if(defaultLocale.getCountry().equals("FI")) {
            language = 1;
        }  else {
            language = 2;
        }

        changeLanguage(language);

		setScreen( getLaunchScreen() );
	}

    @Override
    public void dispose( ) {
        if ( gameScreen != null ) { gameScreen.dispose(); }
        if ( mainMenuScreen != null ) { mainMenuScreen.dispose(); }
        if ( launchScreen != null ) { launchScreen.dispose(); }
        if ( highscoreScreen != null ) { highscoreScreen.dispose(); }
        resources.assetManager.dispose();
        batch.dispose();
    }

    /**
     * Switch music to main menu music.
     */
    public void switchToMainMenuMusic( ) {
        currentMusic.stop();
        currentMusic = resources.mainMenuMusic;
        currentMusic.setVolume(prefs.getMenuMusicVolume());
        currentMusic.play();
        currentMusic.setLooping(true);
    }

    /**
     * Switch music to the game music.
     */
    public void switchToGameMusic( ) {
        currentMusic.stop();
        currentMusic = resources.gameMusic;
        currentMusic.setVolume(prefs.getGameMusicVolume());
        currentMusic.play();
        currentMusic.setLooping(true);
    }

    /**
     * Switches languages
     */
    public void changeLanguage(int i){
        if(i == 1) {
             translate = resources.assetManager.get("localization/languages_fi_FI",I18NBundle.class);
             language = 1;
        }
        if(i == 2) {
            translate = resources.assetManager.get("localization/languages_en_EN",I18NBundle.class);
            language = 2;
        }
    }

    /**
     * @param seenGuide Has the player seen the game instructions.
     */
    public void setSeenGuide( boolean seenGuide ) { this.seenGuide = seenGuide; }

    /**
     * @return Sprite batch.
     */
	public SpriteBatch getBatch() {
		return batch;
	}

    /**
     * @return Main menu screen.
     */
	public MainMenuScreen getMainMenuScreen() {
		if ( mainMenuScreen == null ) {
            Gdx.app.log( "SleepRunner", "Creating new MainMenuScreen instance" );
			mainMenuScreen = new MainMenuScreen( this );
		}
		return mainMenuScreen;
	}

    /**
     * @return Guide screen.
     */
    public GuideScreen getGuideScreen( ) {
        if ( guideScreen == null ) {
            Gdx.app.log( "SleepRunner", "Creating new GuideScreen instance." );
            guideScreen = new GuideScreen( this );
        }
        return guideScreen;
    }

    /**
     * @return Game screen.
     */
	public GameScreen getGameScreen() {
        if ( gameScreen == null ) {
			Gdx.app.log( "SleepRunner", "Creating new GameScreen instance" );
            gameScreen = new GameScreen( this );
        }
		return gameScreen;
	}

    /**
     * @return Highscore screen.
     */
	public HighscoreScreen getHighscoreScreen (){
		if (highscoreScreen == null) {
			Gdx.app.log( "SleepRunner", "Creating new highscoreScreen instance" );
			highscoreScreen = new HighscoreScreen( this );
		}
		return highscoreScreen;
	}

    /**
     * @return Launch screen.
     */
	public LaunchScreen getLaunchScreen(){
		if (launchScreen == null) {
			Gdx.app.log( "SleepRunner", "Creating new launchScreen instance" );
			launchScreen = new LaunchScreen( this );
		}
		return launchScreen;
	}

    /**
     * @return Currently playing music.
     */
    public Music getCurrentMusic( ) { return currentMusic; }

    /**
     * @return Integer symbol representing current game language.
     */
    public int getLanguage(){ return language; }

    /**
     * @return Has the player seen the game instructions.
     */
    public boolean hasSeenGuide( ) { return seenGuide; }

}
