package fi.tamk.tiko.orion.sleeprunner.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
     * Calculates right tile to the ground.
     *
     * @param grid Map chunk grid.
     * @param x    Ground tile's X-position.
     * @param y    Ground tile's Y-position.
     * @return Right tile.
     */
    public static TextureRegion calculateGroundTile(int[][] grid, int x, int y) {
        TextureRegion middleTexture = Constants.TILESET_SPRITES[0][7];
        TextureRegion rightTexture = Constants.TILESET_SPRITES[0][8];
        TextureRegion leftTexture = Constants.TILESET_SPRITES[0][6];
        int ground = Constants.GROUND_BLOCK;
        int empty = Constants.EMPTY_BLOCK;
        TextureRegion calculated;
        if (isSymbolAtPosition(grid, empty, x + 1, y)) {
            // Right edge.
            calculated = rightTexture;
        } else if (isSymbolAtPosition(grid, empty, x - 1, y)) {
            // Left edge.
            calculated = leftTexture;
        } else if (isSymbolAtPosition(grid, ground, x + 1, y) && isSymbolAtPosition(grid, ground, x - 1, y)) {
            // Middle.
            calculated = middleTexture;
        } else if (isSymbolAtPosition(grid, empty, x + 1, y) && isSymbolAtPosition(grid, empty, x - 1, y)) {
            // Alone.
            calculated = middleTexture;
        } else {
            // Wut?
            calculated = leftTexture;
        }
        return calculated;
    }

    /**
     * Creates semi random map chunk.
     *
     * @return
     */
    public static int[][] createMapChunkGrid() {
        int[][] grid = new int[ Constants.CHUNK_MAX_TILES_HEIGHT ][ Constants.CHUNK_MAX_TILES_WIDTH ];
        generateGrounds( grid );
        Gdx.app.log("MapGenerator", "New grid's first is " + grid[0][0]);
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
     * @param grid Mapchunk grid.
     */
    public static MapObjects generateGrounds( int[][] grid ) {
        for ( int i = 0; i < Constants.CHUNK_MAX_TILES_WIDTH; i++ ) {
            int random = MathUtils.random(0, 4);
            if (random == 0) {
                // Let's try to make jump gap between grounds.
                if (isSymbolAtPosition( grid, Constants.EMPTY_BLOCK, i - 1, 0) &&
                        isSymbolAtPosition( grid, Constants.EMPTY_BLOCK, i - 2, 0) &&
                        isSymbolAtPosition(grid, Constants.EMPTY_BLOCK, i - 3, 0) &&
                        isSymbolAtPosition(grid, Constants.EMPTY_BLOCK, i - 4, 0)) {
                    // Four empty blocks is maximum, ignore the empty space.
                    grid[0][i] = Constants.GROUND_BLOCK;
                } else {
                    // There is still space for gap.
                    grid[0][i] = Constants.EMPTY_BLOCK;
                }
            } else {
                // Add ground to this position.
                grid[0][i] = Constants.GROUND_BLOCK;
            }
        }
        return generateObjects( grid, Constants.GROUND_BLOCK, "ground-rectangle" );
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
