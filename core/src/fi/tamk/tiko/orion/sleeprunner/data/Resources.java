package fi.tamk.tiko.orion.sleeprunner.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.data.Preference;
import fi.tamk.tiko.orion.sleeprunner.utilities.Tools;

/**
 * Splits all the tiles and holds them.
 */
public class Resources {

    public AssetManager assetManager;
    public Preference preference;
    public Skin skin;

    public TextureRegion[][] tileSet;
    public TextureRegion[][] signTileSet;
    public TextureRegion[][] guideSet;

    public Music mainMenuMusic;
    public Music gameMusic;

    public Sound nightmareReachSound;
    public Sound nightmareBackSound;
    public Sound powerUpShieldSound;
    public Sound powerUpPickSound;
    public Sound powerUpFlySound;
    public Sound playerDeathSound;
    public Sound playerRunSound;
    public Sound answerWrongSound;
    public Sound answerRightSound;

    public BitmapFont buttonFont;
    public BitmapFont titleFont;
    public BitmapFont debugFont;
    public BitmapFont textFont;
    public BitmapFont tinyFont;

    public Texture powerUpBox;

    public TextureRegion signDestroyed;
    public TextureRegion signDeepOne;
    public TextureRegion signDeepTwo;
    public TextureRegion signRemOne;
    public TextureRegion signRemTwo;
    public TextureRegion cloudLeft;
    public TextureRegion cloudCenter;
    public TextureRegion cloudRight;
    public TextureRegion cloudMiddle;
    public TextureRegion spikesLeft;
    public TextureRegion spikesCenter;
    public TextureRegion spikesRight;
    public TextureRegion whiteSpikesLeft;
    public TextureRegion whiteSpikesCenter;
    public TextureRegion whiteSpikesRight;
    public TextureRegion spikeBallOne;
    public TextureRegion spikeBallTwo;
    public TextureRegion powerUpOne;
    public TextureRegion powerUpTwo;
    public TextureRegion powerUpThree;

    public Animation playerDodgeAnimation;
    public Animation playerRunAnimation;
    public Animation playerFlyAnimation;
    public Animation nightmareAnimation;
    public Animation deepSignAnimation;
    public Animation remSignAnimation;

    /**
     * Constructor for Resources.
     */
    public Resources( ) {
        this.preference = new Preference();
        this.skin = new Skin(Gdx.files.internal(Constants.SKIN_PATH));

        // Load assets.
        this.assetManager = new AssetManager();
        Tools.loadAssets( assetManager );

        // UI
        this.guideSet = TextureRegion.split(assetManager.get("graphics/guide.png", Texture.class), 370, 172);
        this.powerUpBox = assetManager.get("graphics/powerupboxbackground.png", Texture.class);

        // Music.
        this.mainMenuMusic = assetManager.get("sounds/music/mainmenu.mp3",Music.class);
        this.gameMusic = assetManager.get("sounds/music/main.mp3", Music.class);

        // Sounds.
        this.nightmareReachSound = assetManager.get("sounds/sfx/nightmarereach.mp3", Sound.class);
        this.nightmareBackSound = assetManager.get("sounds/sfx/nightmareback.mp3", Sound.class);
        this.playerDeathSound = assetManager.get("sounds/sfx/death.mp3", Sound.class);
        this.playerRunSound = assetManager.get("sounds/sfx/steps.mp3", Sound.class);
        this.powerUpPickSound = assetManager.get("sounds/sfx/powerup.mp3", Sound.class);
        this.powerUpFlySound = assetManager.get("sounds/sfx/fly.mp3", Sound.class);
        this.answerRightSound = assetManager.get("sounds/sfx/success.mp3", Sound.class);
        this.answerWrongSound = assetManager.get("sounds/sfx/mistake.mp3", Sound.class );
        this.powerUpShieldSound = assetManager.get( "sounds/sfx/shield.mp3", Sound.class );

        // Fonts.
        this.buttonFont = skin.getFont("button-font");
        this.titleFont = skin.getFont("title-font");
        this.debugFont = skin.getFont( "tiny-font" );
        this.textFont = skin.getFont("default-font");
        this.tinyFont = skin.getFont("tiny-font");

        // Tiles.
        this.tileSet = TextureRegion.split(assetManager.get("graphics/tileset_new.png", Texture.class),
                Constants.WORLD_TO_SCREEN,
                Constants.WORLD_TO_SCREEN);

        this.signTileSet = TextureRegion.split( assetManager.get( "graphics/tileset_signs.png", Texture.class ),
                Constants.WORLD_TO_SCREEN * 2,
                Constants.WORLD_TO_SCREEN * 3 );

        this.signDestroyed = signTileSet[0][0];
        this.signDeepOne = signTileSet[0][1];
        this.signDeepTwo = signTileSet[0][2];
        this.signRemOne = signTileSet[0][3];
        this.signRemTwo = signTileSet[0][4];
        this.cloudLeft = tileSet[2][0];
        this.cloudCenter = tileSet[3][1];
        this.cloudRight = tileSet[2][2];
        this.cloudMiddle = tileSet[2][1];
        this.spikesLeft =  tileSet[0][0];
        this.spikesCenter = tileSet[0][1];
        this.spikesRight = tileSet[0][2];
        this.whiteSpikesLeft = tileSet[1][0];
        this.whiteSpikesCenter = tileSet[1][1];
        this.whiteSpikesRight = tileSet[1][2];
        this.spikeBallOne = tileSet[0][3];
        this.spikeBallTwo = tileSet[1][3];
        this.powerUpOne = tileSet[0][4];
        this.powerUpTwo = tileSet[1][4];
        this.powerUpThree = tileSet[2][3];

        // Animations.
        this.playerDodgeAnimation = Tools.createAnimation(
                assetManager.get( "graphics/player.png", Texture.class ),
                12, 1, 9, 2, 1/10f );

        this.playerRunAnimation = Tools.createAnimation(
                assetManager.get( "graphics/player.png", Texture.class ),
                12, 1, 1, 8, 1/10f );

        this.playerFlyAnimation = Tools.createAnimation(
                assetManager.get( "graphics/player.png", Texture.class ),
                12, 1, 11, 2, 1/10f );

        this.nightmareAnimation = Tools.createAnimation(
                assetManager.get( "graphics/nightmare.png", Texture.class ),
                4, 1, 1, 4, 1/5f );

        this.deepSignAnimation = Tools.createAnimation(
                assetManager.get( "graphics/tileset_signs.png", Texture.class ),
                5, 1, 2, 2, 1/5f );

        this.remSignAnimation = Tools.createAnimation(
                assetManager.get( "graphics/tileset_signs.png", Texture.class ),
                5, 1, 4, 2, 1/5f );
    }

    /**
     * Mutes all sounds.
     */
    public void muteAllSounds( ) {
        preference.muteAll();
        this.mainMenuMusic.setVolume( preference.getMenuMusicVolume() );
        this.gameMusic.setVolume( preference.getGameMusicVolume() );
    }

    /**
     * Un-mutes all sounds.
     */
    public void unMuteAllSounds( ) {
        preference.unMuteAll();
        this.mainMenuMusic.setVolume( preference.getMenuMusicVolume() );
        this.gameMusic.setVolume( preference.getGameMusicVolume() );
    }

}
