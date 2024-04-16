package puzzles.clock;

import puzzles.common.solver.Configuration;

import java.util.*;

/**
 * Represents a configuration for solving clock puzzles
 *
 * @author RIT CS
 * @author Quang Huynh (qth9368)
 */
public class ClockConfig implements Configuration {
    private final int hours;  // total amount of hours
    private final int current; // current hour
    private final int end;  // ending hour

    /**
     * Construct new ClockConfig
     *
     * @param hours total number of hours on clock
     * @param current current position
     * @param end destination position
     */
    public ClockConfig(int hours, int current, int end) {
        this.hours = hours;
        this.current = current;
        this.end = end;
    }
    /**
     * Checks if the current config is a solution to clock puzzle
     *
     * @return true if the config is solution, false otherwise.
     */
    @Override
    public boolean isSolution() {
        return current == end;
    }

    /**
     * Retrieves the neighboring configs of the current config
     *
     * @return a collection of neighboring configurations.
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        Collection<Configuration> neighbors = new ArrayList<>();
        int next = (current % hours) + 1;
        int prev = (current == 1) ? hours : current - 1;  // if current is 1, return hours, else return current - 1
        neighbors.add(new ClockConfig(hours, next, end));
        neighbors.add(new ClockConfig(hours, prev, end));
        return neighbors;
    }

    /**
     * Checks if another object's hours and current is equal to this config
     *
     * @param other The object to compare against.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if(other instanceof ClockConfig) {
            ClockConfig otherClock = (ClockConfig) other;
            result = hours == otherClock.hours && current == otherClock.current;
        }
        return result;
    }

    /**
     * Computes the hash code for this config
     *
     * @return The hash code of the clock config
     */
    @Override
    public int hashCode() {
        return Objects.hash(hours, current);
    }

    /**
     * Provides each step (current step)
     *
     * @return Return each step of solution
     */
    @Override
    public String toString() {
        return String.valueOf(current);
    }
}
