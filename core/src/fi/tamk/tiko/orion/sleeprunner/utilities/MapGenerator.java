package fi.tamk.tiko.orion.sleeprunner.utilities;

import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.MathUtils;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;

/**
 * Methods for generating map chunks.
 */
public class MapGenerator {

    /**
     * Checks is the integer symbol on
     * the given position in the chunk grid.
     *
     * @param grid   The mapchunk grid.
     * @param symbol The integer symbol to look for.
     * @param x      The x position.
     * @param y      The y position.
     */
    public static boolean isSymbolAtPosition( int[][] grid, int symbol, int x, int y ) {
        // First check array boundaries.
        if ( ( x < Constants.CHUNK_MAX_TILES_WIDTH && x > -1 ) &&
                ( y < Constants.CHUNK_MAX_TILES_HEIGHT && y > -1 ) ) {
            if ( grid[ y ][ x ] == symbol ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates semi random map chunk.
     *
     * @return
     */
    public static int[][] createMapChunkGrid() {
        int[][] grid = new int[ Constants.CHUNK_MAX_TILES_HEIGHT ][ Constants.CHUNK_MAX_TILES_WIDTH ];
        generateGrounds(grid);
        generateSpikes(grid);
        return grid;
    }

    /**
     * Creates ground only map chunk.
     *
     * @return
     */
    public static int[][] createIntervalMapChunkGrid( ) {
        int[][] grid = new int[ Constants.CHUNK_MAX_TILES_HEIGHT ][ Constants.CHUNK_MAX_TILES_WIDTH ];
        for ( int i = 0; i < Constants.CHUNK_MAX_TILES_WIDTH; i++ ) {
            grid[ 0 ][ i ] = Constants.GROUND_BLOCK;
        }
        grid[0][Constants.CHUNK_MAX_TILES_WIDTH - 1] = Constants.EMPTY_BLOCK;
        return grid;
    }

    /**
     * Generates semi random ground
     * for the map chunk.
     *
     * @param grid Map chunk grid.
     */
    public static MapObjects generateGrounds( int[][] grid ) {
        int ground = Constants.GROUND_BLOCK;
        int empty = Constants.EMPTY_BLOCK;
        for ( int i = 0; i < Constants.CHUNK_MAX_TILES_WIDTH; i++ ) {
            int random = MathUtils.random(0, 4); // Probability to get empty block.
            if (random == 0) {
                // Try to make jump gap between grounds.
                if (isSymbolAtPosition(grid, ground, i - 1, 0) &&
                        isSymbolAtPosition(grid, empty, i - 2, 0) &&
                        isSymbolAtPosition(grid, empty, i - 3, 0) &&
                        isSymbolAtPosition(grid, empty, i - 4, 0)) {
                    // Four empty blocks is maximum, ignore the empty space.
                    grid[0][i] = ground;
                } else {
                    // There is still space for gap.
                    grid[0][i] = empty;
                }
            } else {
                // Add ground to this position.
                grid[0][i] = ground;
            }
        }
        return generateObjects(grid, ground, "ground-rectangle");
    }

    /**
     * Generates semi random spikes.
     *
     * @param grid Map chunk grid.
     */
    public static MapObjects generateSpikes(int[][] grid) {
        int spikes = Constants.SPIKES_BLOCK;
        int ground = Constants.GROUND_BLOCK;
        int empty = Constants.EMPTY_BLOCK;
        for (int i = 0; i < Constants.CHUNK_MAX_TILES_WIDTH; i++) {
            int random = MathUtils.random(0, 4); // Probability to get spikes. (Keep in mind the ground generation)
            if (random == 0) {
                // Try to make spikes. They can only go where is ground beneath.
                if (isSymbolAtPosition(grid, ground, i, 0)) {
                    // Max spikes amount in a row is four.
                    if (isSymbolAtPosition(grid, spikes, i - 1, 1) &&
                            isSymbolAtPosition(grid, spikes, i - 2, 1) &&
                            isSymbolAtPosition(grid, spikes, i - 3, 1) &&
                            isSymbolAtPosition(grid, spikes, i - 4, 1)) {
                        grid[1][i] = empty;
                    } else {
                        // There is space for more spikes
                        grid[1][i] = spikes;
                    }
                }
            }
        }
        return generateObjects(grid, spikes, "spikes-rectangle");
    }

    /**
     * Generates rectangle objects of given symbol in the grid.
     *
     * @param symbol The integer symbol on grid.
     * @param name   The name to assign to the map object.
     * @return       List of map objects.
     */
    public static MapObjects generateObjects( int[][] grid, int symbol, String name ) {
        MapObjects mapObjects = new MapObjects();
        int startX = 0;
        int startY = 0;
        int symbolRow = 1;
        // Loop through map's content.
        for ( int i = 0; i < Constants.CHUNK_MAX_TILES_HEIGHT; i++ ) {
            for ( int j = 0; j < Constants.CHUNK_MAX_TILES_WIDTH; j++ ) {
                int currentPos = grid[i][j];
                if ( currentPos == symbol && isSymbolAtPosition( grid, symbol, j + 1, i ) ) {
                    // Set starting positions if this position is
                    // initiating the "symbol row combo".
                    if ( symbolRow == 1 ) {
                        startY = i * Constants.WORLD_TO_SCREEN;
                        startX = j * Constants.WORLD_TO_SCREEN;
                    }
                    symbolRow++;
                } else {
                    if ( symbolRow == 1 ) {
                        // There is just one block,
                        // fix the start positions.
                        startY = i * Constants.WORLD_TO_SCREEN;
                        startX = j * Constants.WORLD_TO_SCREEN;
                    }
                    if ( currentPos == symbol ) {
                        // Create rectangle object from its
                        // starting position and scale width by "combo".
                        RectangleMapObject rectangleMapObject = new RectangleMapObject(
                                startX,
                                startY,
                                symbolRow * Constants.WORLD_TO_SCREEN,
                                Constants.WORLD_TO_SCREEN);
                        rectangleMapObject.setName(name);
                        mapObjects.add(rectangleMapObject);
                    }
                    // Reset "symbol row combo" counter.
                    symbolRow = 1;
                }
            }
        }
        return mapObjects;
    }

}
