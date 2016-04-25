package fi.tamk.tiko.orion.sleeprunner.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;

import java.util.Collections;
import java.util.Comparator;

import fi.tamk.tiko.orion.sleeprunner.utilities.Tools;

/**
 * Created by joni on 13/03/2016.
 */
public class Preference {

    private Preferences prefs;

    public Preference(){

        prefs = Gdx.app.getPreferences("SleepRunnerPreferences");
        if(prefs.getBoolean("isCreated")==false){
            Tools.createPreferences(prefs);
        }
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

    public int getHighscore(int i){
       return prefs.getInteger("highscore"+i);
    }

    public void putHighscore(int i){
         Array<Integer> highscores = new Array<Integer>();
        for(int j = 0; j <= 4; j++) {
            highscores.add(prefs.getInteger("highscore"+(j+1)));
        }
        highscores.add(i);
        highscores.sort(Collections.reverseOrder());

        for(int k = 1; k <= 5; k++){
            prefs.putInteger("highscore"+k,highscores.get(k-1));
        }
        prefs.flush();
    }


    public boolean getMuted(){
        return prefs.getBoolean("isMuted");
    }
}
