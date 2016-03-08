package fi.tamk.tiko.orion.sleeprunner.utilities;

import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;

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
     * @param x      The (starting) x position.
     * @param y      The y position.
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
            // Last two are empty in the interval map chunk.
            if (i < (Constants.CHUNK_MAX_TILES_WIDTH - 2)) {
                grid[0][i] = Constants.GROUND_BLOCK;
                grid[1][i] = Constants.GROUND_BLOCK;
            }
        }
        return grid;
    }

    // TODO: Split generateObjects to different methods.
    // TODO: Make ground and spikes generation within one grid loop.
    // TODO: Make ground and spikes MapObject generation within one grid loop.

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
            int random = MathUtils.random(0, 6); // Probability to get empty block.
            if (random == 0) {
                // Try to make empty block.
                grid[1][i] = empty;
                grid[0][i] = empty;
                if (isSymbolInRow(grid, empty, i, 1, Constants.MAX_EMPTY_AMOUNT)) {
                    // There is already max amount of empty block in a row.
                    grid[1][i] = ground;
                    grid[0][i] = ground;
                }
            } else {
                // Try to make ground block.
                grid[1][i] = ground;
                grid[0][i] = ground;
                if (isSymbolAtPosition(grid, ground, i - Constants.MIN_EMPTY_AMOUNT, 1) &&
                        isSymbolInRow(grid, empty, i - 1, 1, Constants.MIN_EMPTY_AMOUNT - 1)) {
                    // There has to be empty block according to minimum value.
                    grid[1][i] = empty;
                    grid[0][i] = empty;
                }
            }
        }
        return generateObjects(grid, ground, "ground-rectangle");
    }

    /**
     * Generates semi random spikes.
     * for the map chunk.
     *
     * @param grid Map chunk grid.
     */
    public static MapObjects generateSpikes(int[][] grid) {
        int spikes = Constants.SPIKES_BLOCK;
        int empty = Constants.EMPTY_BLOCK;
        for (int i = 0; i < Constants.CHUNK_MAX_TILES_WIDTH; i++) {
            int random = MathUtils.random(0, 3); // Probability to get spikes. (Keep in mind empty blocks)
            if (random == 0) {
                // Try to make spikes block. There must be empty block.
                if (isSymbolAtPosition(grid, empty, i, 0)) {
                    // Fill the empty blocks with spikes.
                    int ii = i;
                    while (isSymbolAtPosition(grid, empty, ii, 0)) {
                        grid[0][ii++] = spikes;
                    }
                }
            }
        }
        return generateObjects(grid, spikes, "spikes-rectangle");
    }

    /**
     * Is the given position in any of the MapObject rectangle bounds.
     *
     * @param y          The y position.
     * @param mapObjects 'List' containing MapObject rectangles.
     */
    public static boolean isRectangleYBounds(int y, MapObjects mapObjects) {
        Array<RectangleMapObject> rectangleMapObjects = mapObjects.getByType(RectangleMapObject.class);
        for (RectangleMapObject rectangleMapObject : rectangleMapObjects) {
            Rectangle rectangle = rectangleMapObject.getRectangle();
            if (y <= rectangle.getHeight() - y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generates rectangle objects of given symbol in the grid.
     *
     * @param symbol The integer symbol on grid.
     * @param name   The name to assign to the map object.
     * @return       List of map objects.
     */
    public static MapObjects generateObjects( int[][] grid, int symbol, String name ) {
        Array<Integer> ignoredIndexes = new Array<Integer>();
        MapObjects mapObjects = new MapObjects();
        int symbolRow = 1;
        int startX = 0;
        int startY = 0;
        int height = Constants.WORLD_TO_SCREEN;
        // Loop through map's content.
        for (int i = Constants.CHUNK_MAX_TILES_HEIGHT - 1; i > -1; i--) {
            if (!isRectangleYBounds(i, mapObjects)) {
                for (int j = 0; j < Constants.CHUNK_MAX_TILES_WIDTH; j++) {
                    int currentPos = grid[i][j];
                    if (currentPos == symbol && isSymbolAtPosition(grid, symbol, j + 1, i)) {
                        //Gdx.app.log( "MapGenerator", "Current pos and right pos is same symbol!" );
                        // Set starting positions if this position is
                        // initiating the "symbol row combo".
                        if (symbolRow == 1) {
                            if (isSymbolAtPosition(grid, symbol, j, i - 1)) {
                                height += Constants.WORLD_TO_SCREEN;
                                ignoredIndexes.add(i);
                                startY = ((i * Constants.WORLD_TO_SCREEN) - Constants.WORLD_TO_SCREEN);
                            } else {
                                startY = i * Constants.WORLD_TO_SCREEN;
                            }
                            startX = j * Constants.WORLD_TO_SCREEN;
                        }
                        symbolRow++;
                    } else {
                        if (symbolRow == 1) {
                            // There is just one block,
                            // fix the start positions.
                            if (isSymbolAtPosition(grid, symbol, j, i - 1)) {
                                height += Constants.WORLD_TO_SCREEN;
                                ignoredIndexes.add(i);
                                startY = ((i * Constants.WORLD_TO_SCREEN) - Constants.WORLD_TO_SCREEN);
                            } else {
                                startY = i * Constants.WORLD_TO_SCREEN;
                            }
                            startX = j * Constants.WORLD_TO_SCREEN;
                        }
                        if (currentPos == symbol) {
                            // Create rectangle object from its
                            // starting position and scale width by "combo".
                            // Height is now also calculated.
                            RectangleMapObject rectangleMapObject = new RectangleMapObject(
                                    startX,
                                    startY,
                                    symbolRow * Constants.WORLD_TO_SCREEN,
                                    height);
                            rectangleMapObject.setName(name);
                            mapObjects.add(rectangleMapObject);
                        }
                        // Reset "symbol row combo" counter.
                        symbolRow = 1;
                        height = Constants.WORLD_TO_SCREEN;
                    }
                }
            }
        }
        return mapObjects;
    }

}
