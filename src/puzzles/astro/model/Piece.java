package puzzles.astro.model;

import puzzles.common.Coordinates;
import puzzles.common.Direction;

import java.util.Objects;

/**
 * Astronaut record
 *
 * @author Quang Huynh
 */
public record Piece(String name, Coordinates coords) {
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
