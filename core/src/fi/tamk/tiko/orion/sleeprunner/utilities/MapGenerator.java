package fi.tamk.tiko.orion.sleeprunner.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.objects.GameObject;
import fi.tamk.tiko.orion.sleeprunner.objects.GroundObject;
import fi.tamk.tiko.orion.sleeprunner.objects.ShieldPowerUpObject;
import fi.tamk.tiko.orion.sleeprunner.objects.SpikesObject;

/**
 * Methods for generating map chunks.
 */
public class MapGenerator {

    /**
     * Is the integer symbol on the given
     * position in the chunk grid.
     *
     * @param grid   The map chunk grid.
     * @param symbol The integer symbol to look for.
     * @param x      The x position.
     * @param y      The y position.
     */
    public static boolean isSymbolAtPosition( int[][] grid, int symbol, int x, int y ) {
        // Check array boundaries.
        if ( ( x < Constants.CHUNK_MAX_TILES_WIDTH && x > -1 ) &&
                ( y < Constants.CHUNK_MAX_TILES_HEIGHT && y > -1 ) ) {
            if ( grid[ y ][ x ] == symbol ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Is there specific symbol vertical row of given amount.
     *
     * @param grid   The map chunk grid.
     * @param symbol The integer symbol to look for.
     * @param x      The (starting) x position. (grid index)
     * @param y      The y position. (grid index)
     * @param amount The vertical row amount.
     */
    public static boolean isSymbolInRow(int[][] grid, int symbol, int x, int y, int amount) {
        for (int i = 0; i < amount; i++) {
            if (!isSymbolAtPosition(grid, symbol, x - i, y)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds ground or empty to the current position.
     *
     * @param grid The 2D integer map grid.
     * @param x    Current x position. (grid index)
     */
    private static void generateGroundBlock( int[][] grid, int x ) {
        int random = MathUtils.random(0, 4); // Probability to get empty block.
        if (random == 0) {
            // Try to make empty block.
            grid[1][x] = Constants.EMPTY_BLOCK;
            grid[0][x] = Constants.EMPTY_BLOCK;
            if ( isSymbolInRow( grid, Constants.EMPTY_BLOCK, x, 1, Constants.MAX_EMPTY_AMOUNT ) ||
                    !isSymbolInRow( grid, Constants.GROUND_BLOCK, x - 1, 1, Constants.MIN_GROUND_AMOUNT ) ) {
                // There is already maximum amount of empty blocks
                // or not enough ground according to min value
                // replace this position with ground.
                grid[1][x] = Constants.GROUND_BLOCK;
                grid[0][x] = Constants.GROUND_BLOCK;
            }
        } else {
            // Try to make ground block.
            grid[1][x] = Constants.GROUND_BLOCK;
            grid[0][x] = Constants.GROUND_BLOCK;
            if ( !isSymbolInRow( grid, Constants.EMPTY_BLOCK, x - 1, 1, Constants.MIN_EMPTY_AMOUNT ) &&
                    !isSymbolAtPosition( grid, Constants.GROUND_BLOCK, x - 1, 1 ) ) {
                // There is not enough of empty blocks according
                // to the minimum value, replace this position with empty.
                grid[1][x] = Constants.EMPTY_BLOCK;
                grid[0][x] = Constants.EMPTY_BLOCK;
            }
        }
    }

    /**
     * Tries to add power up block to the position.
     *
     * @param grid The 2D integer map grid.
     * @param x    Current x position. (grid index)
     */
    private static void generatePowerUpBlock( int[][] grid, int x ) {
        int random = MathUtils.random( 0, 10 ); // Probability to get any power up.
        if ( random == 0 ) {
            // Random power ups y position.
            int y = MathUtils.random( 4, 6 );
            if ( isSymbolAtPosition( grid, Constants.EMPTY_BLOCK, x, y ) ) {
                grid[ y ][ x ] = Constants.POWERUP_SHIELD_BLOCK;
            }
        }
    }

    /**
     * Tries to generate spikes to the position.
     *
     * @param grid  The 2D integer map grid.
     * @param x     Current x position. (grid index)
     */
    private static void generateSpikeBlock( int[][] grid, int x ) {
        int random = MathUtils.random(0, 3); // Probability to get spikes. (Keep in mind empty blocks)
        if (random == 0) {
            // Try to make spikes block. There must be empty block.
            if (isSymbolAtPosition(grid, Constants.EMPTY_BLOCK, x, 0)) {
                // Fill the empty blocks with spikes.
                int ii = x;
                while (isSymbolAtPosition(grid, Constants.EMPTY_BLOCK, ii, 0)) {
                    grid[0][ii++] = Constants.SPIKES_BLOCK;
                }
            }
        }
    }

    /**
     * Generates semi random positions for grounds
     * and spikes to the 2D integer array.
     *
     * @return Filled 2D integer array.
     */
    public static int[][] generateMapChunkGrid() {
        int[][] grid = new int[ Constants.CHUNK_MAX_TILES_HEIGHT ][ Constants.CHUNK_MAX_TILES_WIDTH ];
        Gdx.app.log( "MapGenerator", "Generating new map chunk grid!" );
        for ( int i = 0; i < Constants.CHUNK_MAX_TILES_WIDTH; i++ ) {
            generateGroundBlock(grid, i);
            generateSpikeBlock(grid, i);
            //generatePowerUpBlock( grid, i );
        }
        return grid;
    }

    /**
     * Generates only ground map chunk grid.
     *
     * @return Filled 2D integer array.
     */
    public static int[][] generateMapChunkIntervalGrid( ) {
        int[][] grid = new int[ Constants.CHUNK_MAX_TILES_HEIGHT ][ Constants.CHUNK_MAX_TILES_WIDTH ];
        for ( int i = 0; i < Constants.CHUNK_MAX_TILES_WIDTH; i++ ) {
            grid[0][i] = Constants.GROUND_BLOCK;
            grid[1][i] = Constants.GROUND_BLOCK;
        }
        return grid;
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
    public static boolean isInGameObjectBounds( int x, int y, int symbol, MapChunk mapChunk ) {
        Array<GameObject> gameObjects = mapChunk.getGameObjects();
        float offsetLength = ( mapChunk.getOffset() * Constants.CHUNK_WIDTH_PIXELS )/100f;
        float gameObjectHeight;
        float gameObjectWidth;
        float gameObjectX;
        float gameObjectY;
        float meterY;
        float meterX;
        float scale = 1/100f;
        for ( GameObject gameObject : gameObjects ) {
            if ( gameObject.getUserData().symbol == symbol ) {
                gameObjectWidth = gameObject.getWidth();
                gameObjectHeight = gameObject.getHeight();
                gameObjectX = gameObject.getX() - gameObjectWidth/2;
                gameObjectY = gameObject.getY() - gameObjectHeight/2;
                meterX = ( x * scale ) + offsetLength;
                meterY = ( y * scale );
                if ( ( meterX <= ( gameObjectX + meterX ) && meterX <= ( gameObjectWidth + meterX ) ) &&
                        ( meterY <= gameObjectY && meterY <= gameObjectHeight ) ) {
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
        World world = mapChunk.getWorld();
        float offset = mapChunk.getOffset();
        float scale = 1/100f;
        float meterHeight = height * scale;
        float meterWidth = width * scale;
        float meterX = ( x * scale ) + ( (offset * Constants.CHUNK_WIDTH_PIXELS )/100f );
        float meterY = y * scale;
        float centerX = meterWidth/2 + meterX;
        float centerY = meterHeight/2 + meterY;
        if ( symbol == Constants.GROUND_BLOCK ) {
            gameObject = new GroundObject( world, centerX, centerY, meterWidth, meterHeight );
        } else if ( symbol == Constants.SPIKES_BLOCK ) {
            gameObject = new SpikesObject( world, centerX, centerY, meterWidth, meterHeight );
        } else if ( symbol == Constants.POWERUP_SHIELD_BLOCK ) {
            gameObject = new ShieldPowerUpObject( world, centerX, centerY, meterWidth, meterHeight );
        }
        return gameObject;
    }

    /**
     * Generates game objects from the map chunk's 2D integer 'grid' array.
     */
    public static void generateGameObjects( MapChunk mapChunk ) {
        int[][] grid = mapChunk.getGrid();
        int currentSymbol = 0;
        int startX = 0;
        int startY = 0;
        int rowX = 1;
        int rowY = 1;
        // Loop through map's content.
        for (int i = Constants.CHUNK_MAX_TILES_HEIGHT - 1; i > -1; i--) {
            for (int j = 0; j < Constants.CHUNK_MAX_TILES_WIDTH; j++) {
                int currentPos = grid[i][j];
                if (!isInGameObjectBounds(j, i, currentPos, mapChunk )) {
                    // 'Combo' ended, make a game object of the gathered details.
                    if (currentSymbol != currentPos || ( j == Constants.CHUNK_MAX_TILES_WIDTH - 1 ) ) {
                        if (currentSymbol > 0) {
                            mapChunk.addGameObject(generateGameObject(mapChunk, startX, startY, rowX * Constants.WORLD_TO_SCREEN, rowY * Constants.WORLD_TO_SCREEN, currentSymbol));
                        }
                        // Reset details.
                        currentSymbol = currentPos;
                        rowX = 1;
                        rowY = 1;
                    }
                    if (isSymbolAtPosition(grid, currentSymbol, j + 1, i)) {
                        // If this is row's first, set the game object's starting positions.
                        if (rowX == 1) {
                            if (isSymbolAtPosition(grid, currentSymbol, j, i - 1)) {
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
                            if (isSymbolAtPosition(grid, currentSymbol, j, i - 1)) {
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

}
