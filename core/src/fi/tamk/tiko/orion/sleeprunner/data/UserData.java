package fi.tamk.tiko.orion.sleeprunner.data;

/**
 * Box2D Body's specific details.
 */
public class UserData {

    public String id; // PLAYER, GROUND, SPIKES
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
     * @param id
     */
    private void setSymbol( String id ) {
        if ( id.equals( "GROUND" ) ) {
            symbol = Constants.GROUND_BLOCK;
        } else if ( id.equals( "SPIKES" ) ) {
            symbol = Constants.SPIKES_BLOCK;
        } else {
            symbol = -1;
        }
    }

}
