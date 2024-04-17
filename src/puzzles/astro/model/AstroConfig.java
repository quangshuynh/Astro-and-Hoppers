package puzzles.astro.model;

import puzzles.astro.solver.Robot;
import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a configuration for solving Astro puzzles
 *
 * @author RIT CS
 * @author Quang Huynh (qth9368)
 */

public class AstroConfig implements Configuration{
    private String[][] grid;
    private String astroCoords;
    private String goalCoords;
    private int rows;
    private int cols;
    /**
     * Construct new AstroConfig
     *
     * @param filename name of Astro file
     * @throws IOException
     */
    public AstroConfig(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            /** Read dimension info */
            String line = br.readLine();
            String dim[] = line.split("\\s+");
            rows = Integer.parseInt(dim[0]);  // first line, first int is row
            cols = Integer.parseInt(dim[1]); // first line, second int is col
            grid = new String[rows][cols];  // Initialize grid array with the specified dimensions

            /** Read goal info */
            line = br.readLine();
            String[] goalLine = line.split("\\s+");
            int goalRow = Integer.parseInt(goalLine[1].split(",")[0]);
            int goalColumn = Integer.parseInt(goalLine[1].split(",")[1]);
            goalCoords = goalRow + "," + goalColumn;

            /** Read astronaut info */
            line = br.readLine();
            String[] astroLine = line.split("\\s+");
            int astroRow = Integer.parseInt(astroLine[1].split(",")[0]);
            int astroColumn = Integer.parseInt(astroLine[1].split(",")[1]);
            astroCoords = astroRow + "," + astroColumn;

            /** Read number of robots and robot info */
            line = br.readLine();
            int numRobots = Integer.parseInt(line);
            Collection<Robot> robots = new HashSet<>();
            for (int i = 0; i < numRobots; i++) {
                line = br.readLine();
                String[] robotInfo = line.split("\\s+");
                char robotSymbol = robotInfo[0].charAt(0);
                int robotRow = Integer.parseInt(robotInfo[1].split(",")[0]);
                int robotColumn = Integer.parseInt(robotInfo[1].split(",")[1]);
                robots.add(new Robot(robotSymbol, robotRow, robotColumn));
            }

            /** Assign Grid */
            for(int row = 0; row < rows; row++) {
                line = br.readLine();  // read the next line of the grid
                String[] rowValues = line.split("\\s+");
                for(int col = 0; col < cols; col++) {
                    grid[row][col] = rowValues[col];
                }
            }
        }
    }

    /**
     * Checks if the current config is a solution to Astro puzzle
     *
     * @return true if the config is solution, false otherwise.
     */
    @Override
    public boolean isSolution() {
        return Objects.equals(astroCoords, goalCoords);
    }

    /**
     * Retrieves the neighboring configs of the current config
     *
     * @return a collection of neighboring configurations.
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        return null;
    }

    /**
     * Checks if another object's hours and current is equal to this config
     *
     * @param other The object to compare against.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        return false;
    }

    /**
     * Computes the hash code for this config
     *
     * @return The hash code of the clock config
     */
    @Override
    public int hashCode() { return 0; }

    /**
     * Prints Astro grid
     *
     * @return Return each step of solution
     */
    @Override
    public String toString() {
        return null;
    }
}
