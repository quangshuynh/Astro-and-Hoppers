package puzzles.dice;

import puzzles.common.solver.Configuration;

import java.util.*;

/**
 * Represents a configuration for solving dice puzzles
 *
 * @author RIT CS
 * @author Quang Huynh (qth9368)
 */
public class DiceConfig implements Configuration {
    private static List<Die> dice;  // dice faces
    private final String current;  // current roll
    private final String end;  // end roll

    /**
     * Construct new DiceConfig
     *
     * @param dice list of dice
     * @param current current position
     * @param end destination position
     */
    public DiceConfig(List<Die> dice, String current, String end) {
        this.dice = dice;
        this.current = current;
        this.end = end;
    }

    /**
     * Checks if the current config is a solution to dice puzzle
     *
     * @return true if the config is solution, false otherwise.
     */
    @Override
    public boolean isSolution() {
        return current.equals(end);
    }

    /**
     * Retrieves the neighboring configs of the current config
     *
     * @return a collection of neighboring configurations.
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        List<Configuration> neighbors = new ArrayList<>();
        for(int i = 0; i < current.length(); i++) {
            char face = current.charAt(i);
            List<Character> faceNeighbors = dice.get(i).getNeighbors(face);   // get neighbors for each die face
            for(char neighborFace : faceNeighbors) {
                StringBuilder newConfig = new StringBuilder(current);
                newConfig.setCharAt(i, neighborFace);   // replace face with neighbor face
                neighbors.add(new DiceConfig(dice, newConfig.toString(), end));
            }
        }
        return neighbors;
    }

    /**
     * Checks if another object is equal to this config
     *
     * @param other The object to compare against.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if(other instanceof DiceConfig) {
            DiceConfig otherDice = (DiceConfig) other;
            result = dice == otherDice.dice && Objects.equals(current, otherDice.current);
        }
        return result;
    }

    /**
     * Computes the hash code for this config
     *
     * @return The hash code of the dice config
     */
    @Override
    public int hashCode() {
        return Objects.hash(dice, current);
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
