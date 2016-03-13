package fi.tamk.tiko.orion.sleeprunner.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by joni on 13/03/2016.
 */
public class Preference {

    private Preferences prefs;

    public Preference(){
        prefs = Gdx.app.getPreferences("SleepRunnerPreferences");
    }

    public void setSoundVolume(float volume){
        prefs.putFloat("soundVolume",volume);
        prefs.flush();
    }

    public float getSoundVolume(){
        return prefs.getFloat("soundVolume");
    }

    public void setMusicVolume(float volume){
        prefs.putFloat("musicVolume", volume);
        prefs.flush();
    }

    public float getMusicVolume(){
        return prefs.getFloat("musicVolume");
    }

    public void setMuted(){
        if(!prefs.getBoolean("isMuted")) {
            prefs.putBoolean("isMuted", true);
            prefs.putFloat("musicVolume", 0);
            prefs.putFloat("soundVolume", 0);
        } else {
            prefs.putBoolean("isMuted", false);
            prefs.putFloat("musicVolume", 0.5f);
            prefs.putFloat("soundVolume", 0.5f);
        }
        prefs.flush();
    }

    public boolean getMuted(){
        return prefs.getBoolean("isMuted");
    }
}
