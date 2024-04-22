package puzzles.common.solver;

import puzzles.common.Coordinates;
import puzzles.common.Direction;

/**
 * Class that helps with moving entities
 *
 * @author Quang Huynh (qth9368)
 */
public class Move {
    private Coordinates coordinates;
    private Direction direction;

    /**
     *
     *
     * @param coordinates coordinates of node
     * @param direction cardinal direction (N, S, E, W)
     */
    public Move (Coordinates coordinates, Direction direction) {
        this.coordinates = coordinates;
        this.direction = direction;
    }

    /**
     * get direction of where node is going
     *
     * @return direction that it is facing
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * get coordinates of piece
     *
     * @return coordinates
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }
}
