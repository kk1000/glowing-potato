package fi.tamk.tiko.orion.sleeprunner.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import fi.tamk.tiko.orion.sleeprunner.SleepRunner;
import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.objects.PlayerObject;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;
import fi.tamk.tiko.orion.sleeprunner.utilities.MapChunk;

/**
 * Handle's UIStage's drawable texts.
 */
public class UIText extends Actor {

    private GameScreen gameScreen;
    private PlayerObject player;
    private BitmapFont titleFont;
    private BitmapFont debugFont;
    private BitmapFont textFont;
    private World world;
    private SleepRunner game;

    private int playTimes = 0;
    private float score = 0;

    /**
     * Constructor for UIText
     *
     * @param gameScreen    GameScreen reference.
     */
    public UIText(SleepRunner g, GameScreen gameScreen) {
        this.game = g;
        this.gameScreen = gameScreen;
        this.player = gameScreen.getPlayer();
        this.world = gameScreen.getWorld();
        titleFont = g.getTitleFont();
        debugFont = g.getDebugFont();
        textFont = g.getTextFont();
    }

    /**
     * Resets UI's texts for the new game.
     */
    public void reset( ) {
        playTimes++;
        score = 0;
    }

    @Override
    public void act(float delta) {
        super.act( delta );
        if ( gameScreen.getGameState() != Constants.GAME_PLAYER_DEATH ) {
            score += delta * 10;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        MapChunk currentMapChunk = gameScreen.getCurrentMapChunk();
        titleFont.draw(batch, game.translate.get("score")+": " + (int) score, Constants.WORLD_TO_SCREEN, Constants.APP_HEIGHT - 10);
        titleFont.draw(batch, game.translate.get("nightmare") + ":", Constants.WORLD_TO_SCREEN, Constants.APP_HEIGHT - 50);
        // Debug details.
        if ( Constants.DEBUG ) {
            debugFont.draw(batch, "Chunk number: " + currentMapChunk.getChunkNumber(), Constants.APP_WIDTH - 150, Constants.APP_HEIGHT - 10);
            debugFont.draw(batch, "Max Ground: " + currentMapChunk.getMaxGroundBlocks(), Constants.APP_WIDTH - 150, Constants.APP_HEIGHT - 30);
            debugFont.draw(batch, "Min Ground: " + currentMapChunk.getMinGroundBlocks(), Constants.APP_WIDTH - 150, Constants.APP_HEIGHT - 50);
            debugFont.draw(batch, "Change per chunk: " + Constants.DIFFICULTY_CHANGE_INTERVAL, Constants.APP_WIDTH - 150, Constants.APP_HEIGHT - 70);

            debugFont.draw(batch, "Body count: " + world.getBodyCount(), Constants.APP_WIDTH - 150, Constants.APP_HEIGHT - 100);

            debugFont.draw(batch, "Player X " + player.getBody().getPosition().x, Constants.APP_WIDTH - 150, Constants.APP_HEIGHT - 130);
            debugFont.draw(batch, "Player Y " + player.getBody().getPosition().y, Constants.APP_WIDTH - 150, Constants.APP_HEIGHT - 150);

            debugFont.draw(batch, "Play times: " + playTimes, Constants.APP_WIDTH - 150, Constants.APP_HEIGHT - 180);
            debugFont.draw(batch, "Player fly: " + player.isFlying(), Constants.APP_WIDTH - 150, Constants.APP_HEIGHT - 200);
        }
    }

    /**
     * Setters.
     */

    public void setScore( int score ) { this.score = score; }

    /**
     * Getters
     */

    public int getScore(){
        return (int)score;
    }
}
