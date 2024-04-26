package puzzles.astro.model;

import puzzles.common.Coordinates;
import puzzles.common.Direction;

import java.util.Objects;

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
    public static boolean isValidMove(Piece[][] grid, Coordinates coords, Direction dir) {
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
    public boolean isValidMove(Piece[][] grid, Direction dir) {
        return isValidMove(grid, coords, dir);
    }

    /**
     * Computes the hash code for piece
     *
     * @return The hash code of the piece
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Checks if another object's name is equal
     *
     * @param other The object to compare against.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if(other instanceof Piece) {
            Piece otherPiece = (Piece) other;
            result = Objects.equals(name, otherPiece.name);
        }
        return result;
    }

    /**
     * Prints name
     *
     * @return name of piece
     */
    @Override
    public String toString() {
        return name;
    }
}
