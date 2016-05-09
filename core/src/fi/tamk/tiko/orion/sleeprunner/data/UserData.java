package fi.tamk.tiko.orion.sleeprunner.data;

import com.badlogic.gdx.Gdx;

/**
 * Box2D Body's specific details.
 *
 * @author   Eetu "mehxit" Järvinen
 * @author   Joni "steiner3k" Korpisalo
 */
public class UserData {

    public String id; // PLAYER, GROUND, SPIKES...
    public int symbol; //        1       2

    /**
     * Constructor for user data
     *
     * @param id User data's identifier.
     */
    public UserData(String id) {
        this.id = id;
        setSymbol( id );
    }

    /**
     * Set's integer symbol according to ID.
     *
     * @param id Game object's object id.
     */
    private void setSymbol( String id ) {
        if ( id.equals( "GROUND" ) ) {
            symbol = Constants.GROUND_BLOCK;
        } else if ( id.equals( "SPIKES" ) ) {
            symbol = Constants.SPIKES_BLOCK;
        } else if ( id.equals( "SIGN" )){
            symbol = Constants.SIGN_BLOCK;
        } else if ( id.equals( "POWERUP_MASK" ) ) {
            symbol = Constants.POWERUP_MASK_BLOCK;
        } else if ( id.equals( "POWERUP_SHIELD" ) ) {
            symbol = Constants.POWERUP_SHIELD_BLOCK;
        } else if ( id.equals( "POWERUP_FLY" ) ) {
            symbol = Constants.POWERUP_FLY_BLOCK;
        } else if ( id.equals( "FLYING_SPIKES" ) ) {
            symbol = Constants.FLYING_SPIKES_BLOCK;
        } else {
            Gdx.app.log( "UserData", "GameObject with the id \"" + id + "\" has unknown map generation symbol." );
            symbol = -1;
        }
    }

}
