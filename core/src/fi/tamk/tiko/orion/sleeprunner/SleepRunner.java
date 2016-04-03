package fi.tamk.tiko.orion.sleeprunner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;
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

	private MainMenuScreen mainMenuScreen;

	private GameScreen gameScreen;

	private Music music;

	private Skin skin;

	@Override
	public void create() {
		music = Gdx.audio.newMusic(Gdx.files.internal(Constants.GAME_MUSIC_PATH));

		batch = new SpriteBatch();

		skin  = new Skin(Gdx.files.internal(Constants.SKIN_PATH));

		music.play();
		music.setVolume(0.1f);
		setMainMenuScreen();
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public void setGameScreen(){
		setScreen( getGameScreen() );
	}

	public void setMainMenuScreen(){
		setScreen( getMainMenuScreen() );
	}

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

	public Music getMusic( ) { return music; }

	public Skin getSkin() { return skin;}

}
