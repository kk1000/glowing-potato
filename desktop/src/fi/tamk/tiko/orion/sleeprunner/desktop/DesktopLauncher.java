package fi.tamk.tiko.orion.sleeprunner.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import fi.tamk.tiko.orion.sleeprunner.SleepRunner;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height = 480;
		config.title = "Sleeprun";
		new LwjglApplication(new SleepRunner(), config);
	}
}
