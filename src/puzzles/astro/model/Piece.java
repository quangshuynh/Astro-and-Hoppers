package puzzles.astro.model;

import puzzles.common.Coordinates;
import puzzles.common.Direction;

/**
 * Astronaut record
 *
 * @author Quang Hunh
 */
public record Piece(String name, Coordinates coords) {
    /**
     * Checks if move in a direction is valid within given grid
     *
     * @param grid game grid
     * @param coords coordinates of astronaut
     * @param dir  cardinal direction (n, s, e, w)
     * @return whether move is valid or not
     */
    public static boolean isValidMove(String[][] grid, Coordinates coords, Direction dir) {
        try {
            if(dir == Direction.NORTH) {
                return grid[coords.row() - 1][coords.col()] == null;
            } else if(dir == Direction.SOUTH) {
                return grid[coords.row() + 1][coords.col()] == null;
            } else if(dir == Direction.EAST) {
                return grid[coords.row()][coords.col() + 1] == null;
            } else if(dir == Direction.WEST) {
                return grid[coords.row()][coords.col() - 1] == null;
            } else {
                return false;
            }
        } catch(IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Checks if move is valid or not
     *
     * @param grid game grid
     * @param dir cardinal direction (n, s, e, w)
     * @return whether move is valid or not
     */
    public boolean isValidMove(String[][] grid, Direction dir) {
        return isValidMove(grid, coords, dir);
    }
}
