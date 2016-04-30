package fi.tamk.tiko.orion.sleeprunner.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import fi.tamk.tiko.orion.sleeprunner.SleepRunner;
import fi.tamk.tiko.orion.sleeprunner.utilities.Tools;

/**
 * Created by joni on 30/04/2016.
 */
public class Tilesets {

    TextureRegion[][] tileSet;
    TextureRegion[][] signTileSet;

    SleepRunner game;

    // signs
    public TextureRegion SIGN_DESTROYED = signTileSet[0][0];
    public TextureRegion SIGN_DEEP_1 = signTileSet[0][1];
    public TextureRegion SIGN_DEEP_2 = signTileSet[0][2];
    public TextureRegion SIGN_REM_1 = signTileSet[0][3];
    public TextureRegion SIGN_REM_2 = signTileSet[0][4];
    // tileset
    public TextureRegion CLOUD_LEFT = tileSet[3][0];
    public TextureRegion CLOUD_CENTER = tileSet[3][1];
    public TextureRegion CLOUD_RIGHT = tileSet[3][2];
    public TextureRegion CLOUD_MIDDLE = tileSet[4][1];
    public TextureRegion SPIKES_LEFT = tileSet[0][0];
    public TextureRegion SPIKES_CENTER = tileSet[0][1];
    public TextureRegion SPIKES_RIGHT = tileSet[0][2];
    public TextureRegion WHITE_SPIKES_LEFT = tileSet[1][0];
    public TextureRegion WHITE_SPIKES_CENTER = tileSet[1][1];
    public TextureRegion WHITE_SPIKES_RIGHT = tileSet[1][2];
    public TextureRegion SPIKEBALL_1 = tileSet[0][4];
    public TextureRegion SPIKEBALL_2 = tileSet[1][4];
    public TextureRegion POWERUP_1 = tileSet[0][5];
    public TextureRegion POWERUP_2 = tileSet[1][5];
    public TextureRegion POWERUP_3 = tileSet[2][4];


    public Tilesets(SleepRunner g,Texture tileset, Texture signTileset){
        this.game = g;
        this.tileSet = Tools.splitTileset(tileset);
        this.signTileSet = Tools.splitSignTileset(signTileset);
    }
}
