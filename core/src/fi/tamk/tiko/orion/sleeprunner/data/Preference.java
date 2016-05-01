package fi.tamk.tiko.orion.sleeprunner.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;

import java.util.Collections;

import fi.tamk.tiko.orion.sleeprunner.utilities.Tools;

/**
 * Handle's game's preferences.
 */
public class Preference {

    private Preferences prefs;

    /**
     * Constructor for Preference.
     */
    public Preference(){
        prefs = Gdx.app.getPreferences("SleepRunnerPreferences");
        if(!prefs.getBoolean("isCreated")){
            Tools.createPreferences(prefs);
        }
    }

    /**
     * Mutes or un mutes all sounds.
     */
    public void setMuted(){
        if(!prefs.getBoolean("isMuted")) {
            prefs.putBoolean("isMuted", true);
            prefs.putFloat("gameMusicVolume", 0);
            prefs.putFloat("menuMusicVolume", 0);
            prefs.putFloat("soundVolume", 0);
        } else {
            prefs.putBoolean("isMuted", false);
            prefs.putFloat( "soundVolume", 0.7f );
            prefs.putFloat( "gameMusicVolume", 0.4f );
            prefs.putFloat( "menuMusicVolume", 0.7f );
        }
        prefs.flush();
    }

    /**
     * Sets new highscore to the given rank.
     *
     * @param i Highscore ranking.
     */
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

    /**
     * @param volume New in-game music volume.
     */
    public void setGameMusicVolume( float volume ) {
        prefs.putFloat("gameMusicVolume", volume);
        prefs.flush();
    }

    /**
     * @param volume New menu music's volume.
     */
    public void setMenuMusicVolume(float volume){
        prefs.putFloat("menuMusicVolume", volume);
        prefs.flush();
    }

    /**
     * @param volume Sounds' new volume.
     */
    public void setSoundVolume(float volume){
        prefs.putFloat("soundVolume", volume);
        prefs.flush();
    }

    /**
     * @param i Highscore ranking.
     *
     * @return given rank's highscore.
     */
    public int getHighscore(int i){
        return prefs.getInteger("highscore"+i);
    }

    /**
     * @return sounds' volume.
     */
    public float getSoundVolume(){
        return prefs.getFloat("soundVolume");
    }

    /**
     * @return in-game music volume.
     */
    public float getMenuMusicVolume(){
        return prefs.getFloat("menuMusicVolume");
    }

    /**
     * @return in-game music volume.
     */
    public float getGameMusicVolume(){
        return prefs.getFloat("gameMusicVolume");
    }

    /**
     * @return is the sounds muted.
     */
    public boolean getMuted(){
        return prefs.getBoolean("isMuted");
    }
}
