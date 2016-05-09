package fi.tamk.tiko.orion.sleeprunner.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import fi.tamk.tiko.orion.sleeprunner.SleepRunner;
import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.objects.FlyPowerUpObject;
import fi.tamk.tiko.orion.sleeprunner.objects.FlyingSpikesObject;
import fi.tamk.tiko.orion.sleeprunner.objects.GameObject;
import fi.tamk.tiko.orion.sleeprunner.objects.GroundObject;
import fi.tamk.tiko.orion.sleeprunner.objects.MaskPowerUpObject;
import fi.tamk.tiko.orion.sleeprunner.objects.ShieldPowerUpObject;
import fi.tamk.tiko.orion.sleeprunner.objects.SignObject;
import fi.tamk.tiko.orion.sleeprunner.objects.SpikesObject;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;

/**
 * Methods for generating map chunks.
 *
 * @author   Eetu "mehxit" Järvinen
 * @author   Joni "steiner3k" Korpisalo
 */
public class MapGenerator {

    private static int[][] GRID;

    /**
     * Is the integer symbol on the given
     * position in the chunk GRID.
     *
     * @param symbol The integer symbol to look for.
     * @param x      The x position.
     * @param y      The y position.
     */
    public static boolean isSymbolAtPosition( int symbol, int x, int y ) {
        // Check array boundaries.
        if ( ( x < Constants.CHUNK_MAX_TILES_WIDTH && x > -1 ) &&
                ( y < Constants.CHUNK_MAX_TILES_HEIGHT && y > -1 ) ) {
            if ( GRID[ y ][ x ] == symbol ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Is there specific symbol vertical row of given amount.
     *
     * @param symbol The integer symbol to look for.
     * @param x      The (starting) x position. (GRID index)
     * @param y      The y position. (GRID index)
     * @param amount The vertical row amount.
     */
    public static boolean isSymbolInRow(int symbol, int x, int y, int amount) {
        for (int i = 0; i < amount; i++) {
            if (!isSymbolAtPosition(symbol, x - i, y)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds ground or empty to the current position.
     *
     * @param x               Current x position. (GRID index)
     * @param minEmptyBlocks  Minimum amount of empty blocks in the chunk.
     * @param maxEmptyBlocks  Maximum amount of empty blocks in the chunk.
     * @param minGroundBlocks Minimum amount of ground blocks in the chunk.
     * @param maxGroundBlocks Maximum amount of ground blocks in the chunk.
     */
    private static void generateGroundBlock( int x, int minEmptyBlocks, int maxEmptyBlocks, int minGroundBlocks, int maxGroundBlocks ) {
        int random = MathUtils.random(0, 4); // Probability to get empty block.
        if (random == 0) {
            // Try to make empty block.
            GRID[1][x] = Constants.EMPTY_BLOCK;
            GRID[0][x] = Constants.EMPTY_BLOCK;
            if ( isSymbolInRow( Constants.EMPTY_BLOCK, x, 1, maxEmptyBlocks ) ||
                    !isSymbolInRow( Constants.GROUND_BLOCK, x - 1, 1, minGroundBlocks ) ) {
                // There is already maximum amount of empty blocks
                // or not enough ground according to min value
                // replace this position with ground.
                GRID[1][x] = Constants.GROUND_BLOCK;
                GRID[0][x] = Constants.GROUND_BLOCK;
            }
        } else {
            // Try to make ground block.
            GRID[1][x] = Constants.GROUND_BLOCK;
            GRID[0][x] = Constants.GROUND_BLOCK;
            if ( isSymbolInRow( Constants.GROUND_BLOCK, x - 1, 1, maxGroundBlocks ) ||
                    !isSymbolInRow( Constants.EMPTY_BLOCK, x - 1, 1, minEmptyBlocks ) &&
                    !isSymbolAtPosition( Constants.GROUND_BLOCK, x - 1, 1 ) ) {
                // There is not enough of empty blocks according
                // to the minimum value or
                // there are already max ground amount,
                // replace this position with empty.
                GRID[1][x] = Constants.EMPTY_BLOCK;
                GRID[0][x] = Constants.EMPTY_BLOCK;
            }
        }
    }

    /**
     * Tries to add power up block to the position.
     *
     * @param x    Current x position. (GRID index)
     * @return     True if the method NOT insert power up block.
     */
    private static boolean generatePowerUpBlock( int x ) {
        int random = MathUtils.random( 0, 5 ); // Probability to get some power up.
        if ( random == 0 ) {
            // Random power up
            int powerUp = MathUtils.random( 4, 6 );
            // Random power ups y position.
            int y = MathUtils.random( 5, 8 );
            if ( isSymbolAtPosition( Constants.EMPTY_BLOCK, x, y ) ) {
                GRID[ y ][ x ] = powerUp;
                return false;
            }
        }
        return true;
    }

    /**
     * Tries to generate flying spikes to the position.
     *
     * @param   x     Current x position. (GRID index)
     * @return        True if the method NOT insert flying spike block.
     */
    private static boolean generateFlyingSpikeBlock( int x ) {
        int random = MathUtils.random(0, 2); // Probability to get flying spikes.
        if (random == 0) {
            // Try to make spikes block. There must be ground block beneath.
            if (isSymbolAtPosition(Constants.GROUND_BLOCK, x, 0)) {
                // Random the flying spikes y position.
                int y = MathUtils.random( 3, 5 );
                GRID[y][x] = Constants.FLYING_SPIKES_BLOCK;
                return false;
            }
        }
        return true;
    }

    /**
     * Tries to generate spikes to the position.
     *
     * @param x     Current x position. (GRID index)
     * @return      Did the method NOT generate spikes.
     */
    private static boolean generateSpikeBlock( int x ) {
        boolean noGeneration = true;
        int random = MathUtils.random(0, 8); // Probability to get spikes. (Keep in mind empty blocks)
        if (random == 0) {
            // Try to make spikes block. There must be empty block and ground beneath.
            if (isSymbolAtPosition(Constants.EMPTY_BLOCK, x, 2) &&
                    isSymbolAtPosition(Constants.GROUND_BLOCK, x, 1 ) ) {
                int randomAmount = MathUtils.random( 1, 3 );
                int ii = x;
                for ( int i = 0; i < randomAmount; i++ ) {
                    if ( isSymbolAtPosition(Constants.EMPTY_BLOCK, ii, 2) &&
                            isSymbolAtPosition(Constants.GROUND_BLOCK, ii, 1 ) ) {
                        GRID[2][ii++] = Constants.SPIKES_BLOCK;
                    }
                }
                noGeneration = false;
            }
        }
        return noGeneration;
    }

    /**
     * Generates bottom part of the map chunk.
     *
     * @param values    Ground and empty min and max values.
     * @param calm      Is the map chunk first or sleep changing chunk.
     */
    private static void generateBottom( int[] values, boolean calm ) {
        // Create ground and signs.
        for ( int i = 0; i < Constants.CHUNK_MAX_TILES_WIDTH; i++ ) {
            if ( calm ) {
                // This is first map chunk of the world
                // OR sleep stage changing chunk!
                // Create ground by force.
                GRID[1][i] = Constants.GROUND_BLOCK;
                GRID[0][i] = Constants.GROUND_BLOCK;
                // Also create sleep phase sign.
                if (i == 20) {
                    GRID[4][i] = Constants.SIGN_BLOCK;
                    GRID[3][i] = Constants.SIGN_BLOCK;
                    GRID[2][i] = Constants.SIGN_BLOCK;
                }
            } else {
                generateGroundBlock(i, values[0], values[1], values[2], values[3]);
            }
        }
        // If map chunk ended with ground, force empty block to the end.
        int lastIndex = Constants.CHUNK_MAX_TILES_WIDTH - 1;
        if ( GRID[ 1 ][ lastIndex ] == Constants.GROUND_BLOCK ) {
            GRID[ 1 ][ lastIndex ] = Constants.EMPTY_BLOCK;
            GRID[ 0 ][ lastIndex ] = Constants.EMPTY_BLOCK;
            GRID[ 1 ][ lastIndex - 1 ] = Constants.EMPTY_BLOCK;
            GRID[ 0 ][ lastIndex - 1 ] = Constants.EMPTY_BLOCK;
        }
    }

    private static void generateTop( boolean flyingObstacle, boolean powerUp ) {
        // Create spikes, flying spikes and power ups.
        boolean canContainSpikes = true;
        for ( int i = 0; i < Constants.CHUNK_MAX_TILES_WIDTH; i++ ) {
            if ( canContainSpikes ) { canContainSpikes = generateSpikeBlock(i); }
            if ( flyingObstacle ) { flyingObstacle = generateFlyingSpikeBlock( i ); }
            if ( powerUp && ( i > 5 && i < Constants.CHUNK_MAX_TILES_WIDTH - 5 ) ) {
                powerUp = generatePowerUpBlock(i);
            }
        }
    }

    /**
     * Generates semi random map chunk.
     *
     * @param mapChunk The map chunk.
     * @return         Filled 2D integer array.
     */
    public static int[][] generateMapChunkGrid( MapChunk mapChunk ) {
        MapChunk previousMapChunk = mapChunk.getPreviousMapChunk();
        boolean isFirstMapChunk = ( previousMapChunk == null );
        GRID = new int[ Constants.CHUNK_MAX_TILES_HEIGHT ][ Constants.CHUNK_MAX_TILES_WIDTH ];
        int[] values = new int[] { mapChunk.getMinEmptyBlocks(), mapChunk.getMaxEmptyBlocks(),
                mapChunk.getMinGroundBlocks(), mapChunk.getMaxGroundBlocks() };
        boolean calm = ( isFirstMapChunk || mapChunk.getChunkNumber() % Constants.DIFFICULTY_CHANGE_INTERVAL == 0 );
        generateBottom( values, calm );
        if ( !calm ) {
            boolean canContainFlyingObstacle = mapChunk.canContainFlyingObstacle();
            boolean canContainPowerUp = mapChunk.canContainPowerUp();
            generateTop( canContainFlyingObstacle, canContainPowerUp );
        }
        return GRID;
    }

    /**
     * Is the given position in any of the
     * generated game object's bounds.
     *
     * @param x           X-position. (grid index)
     * @param y           Y-position. (grid index)
     * @param symbol      Integer symbol for the game object.
     * @param mapChunk    Current map chunk.
     */
    public static boolean isInGameObjectBounds( int x, int y, int width, int height, int symbol, MapChunk mapChunk ) {
        Array<GameObject> gameObjects = mapChunk.getGameObjects();
        float offsetLength = ( mapChunk.getPosition() * Constants.CHUNK_WIDTH_PIXELS )/100f;
        float gameObjectHeight;
        float gameObjectWidth;
        float gameObjectX;
        float gameObjectY;
        float meterHeight;
        float meterWidth;
        float meterY;
        float meterX;
        for ( GameObject gameObject : gameObjects ) {
            if ( gameObject.getUserData().symbol == symbol ) {
                gameObjectWidth = gameObject.getWidth();
                gameObjectHeight = gameObject.getHeight();
                gameObjectX = gameObject.getX() - gameObjectWidth / 2;
                gameObjectY = gameObject.getY() - gameObjectHeight / 2;
                meterHeight = height / 100f;
                meterWidth = width / 100f;
                meterX = ((x / 100f) + offsetLength);
                meterY = (y / 100f);
                if (meterX < (gameObjectX + gameObjectWidth) &&
                        (meterX + meterWidth) > gameObjectX &&
                        meterY <= (gameObjectY + gameObjectHeight) &&
                        (meterY + meterHeight) >= gameObjectY) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Generates game object with given PIXEL details.
     *
     * @param mapChunk  MapChunk where to add this game object.
     * @param x         X-position.
     * @param y         Y-position.
     * @param width     Width.
     * @param height    Height.
     * @param symbol    Game object's symbol in the 2D integer grid.
     * @return          Created GameObject.
     */
    public static GameObject generateGameObject( MapChunk mapChunk, int x, int y, int width, int height, int symbol ) {
        GameObject gameObject = null;
        GameScreen gameScreen = mapChunk.getGameScreen();
        World world = mapChunk.getWorld();
        float offset = mapChunk.getPosition();
        float meterHeight = height/100f;
        float meterWidth = width/100f;
        float meterX = ( x/100f ) + ( (offset * Constants.CHUNK_WIDTH_PIXELS )/100f );
        float meterY = y/100f;
        float centerX = meterWidth/2 + meterX;
        float centerY = meterHeight/2 + meterY;
        if ( symbol == Constants.GROUND_BLOCK ) {
            gameObject = new GroundObject( gameScreen, world, centerX, centerY, meterWidth, meterHeight );
        } else if ( symbol == Constants.SPIKES_BLOCK ) {
            gameObject = new SpikesObject( gameScreen, world, centerX, centerY, meterWidth, meterHeight);
        } else if ( symbol == Constants.SIGN_BLOCK ) {
            gameObject = new SignObject( gameScreen, world, centerX, centerY, meterWidth, meterHeight, mapChunk.getSleepStage() );
        } else if ( symbol == Constants.POWERUP_SHIELD_BLOCK ) {
            gameObject = new ShieldPowerUpObject( gameScreen, world, centerX, centerY, meterWidth, meterHeight );
        } else if ( symbol == Constants.POWERUP_FLY_BLOCK ) {
            gameObject = new FlyPowerUpObject( gameScreen, world, centerX, centerY, meterWidth, meterHeight );
        } else if ( symbol == Constants.POWERUP_MASK_BLOCK ) {
            gameObject = new MaskPowerUpObject( gameScreen, world, centerX, centerY, meterWidth, meterHeight );
        } else if ( symbol == Constants.FLYING_SPIKES_BLOCK ) {
            gameObject = new FlyingSpikesObject( gameScreen, world, centerX, centerY, meterWidth, meterHeight );
        }
        return gameObject;
    }

    /**
     * Generates game objects from the map chunk's 2D integer 'grid' array.
     */
    public static void generateGameObjects( MapChunk mapChunk ) {
        int[][] grid = mapChunk.getGrid();
        int currentSymbol = 0;
        int height;
        int width;
        int startX = 0;
        int startY = 0;
        int rowX = 1;
        int rowY = 1;
        // Loop through map's content.
        for (int i = Constants.CHUNK_MAX_TILES_HEIGHT - 1; i > -1; i--) {
            for (int j = 0; j < Constants.CHUNK_MAX_TILES_WIDTH; j++) {
                int currentPos = grid[i][j];
                // 'Combo' ended, make a game object of the gathered details.
                if (currentSymbol != currentPos ) {
                    if (currentSymbol > 0) {
                        height = rowY * Constants.WORLD_TO_SCREEN;
                        width = rowX * Constants.WORLD_TO_SCREEN;
                        if ( !isInGameObjectBounds(startX, startY, width, height, currentSymbol, mapChunk ) ) {
                            mapChunk.addGameObject(generateGameObject(mapChunk, startX, startY, width, height, currentSymbol));
                        }
                    }
                    // Reset details.
                    currentSymbol = currentPos;
                    rowX = 1;
                    rowY = 1;
                }
                if (isSymbolAtPosition(currentSymbol, j + 1, i)) {
                    // If this is row's first, set the game object's starting positions.
                    if (rowX == 1) {
                        if (isSymbolAtPosition(currentSymbol, j, i - 1)) {
                            startY = ((i * Constants.WORLD_TO_SCREEN) - Constants.WORLD_TO_SCREEN);
                            rowY++;
                        } else {
                            startY = i * Constants.WORLD_TO_SCREEN;
                        }
                        startX = j * Constants.WORLD_TO_SCREEN;
                    }
                    rowX++;
                } else {
                    if (rowX == 1) {
                        // There is row of one, fix the starting positions.
                        if (isSymbolAtPosition(currentSymbol, j, i - 1)) {
                            startY = ((i * Constants.WORLD_TO_SCREEN) - Constants.WORLD_TO_SCREEN);
                            rowY++;
                        } else {
                            startY = i * Constants.WORLD_TO_SCREEN;
                        }
                        startX = j * Constants.WORLD_TO_SCREEN;
                    }
                }
            }
        }
    }

}
