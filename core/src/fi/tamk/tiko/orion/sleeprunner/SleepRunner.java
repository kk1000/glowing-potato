package fi.tamk.tiko.orion.sleeprunner;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;
import fi.tamk.tiko.orion.sleeprunner.screens.MainMenu;

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

	private MainMenu mainMenuScreen;

	private GameScreen gameScreen;

	private Music music;

	@Override
	public void create() {

		music = Gdx.audio.newMusic(Gdx.files.internal(Constants.GAME_MUSIC_PATH));

		batch = new SpriteBatch();

		music.play();
		music.setVolume(0.1f);
		setMainMenuScreen();


	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public void setGameScreen(){
		setScreen (new GameScreen(this));

	}
	public void setMainMenuScreen(){
		setScreen (new MainMenu(this));

	}

	public MainMenu getMainMenu() {
		return mainMenuScreen;
	}
	public GameScreen getGameScreen() {
		return gameScreen;
	}

	public Music getMusic( ) { return music; }


}
