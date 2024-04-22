package puzzles.astro.model;

import puzzles.astro.solver.Astro;
import puzzles.astro.solver.Robot;
import puzzles.common.Coordinates;
import puzzles.common.Direction;
import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static puzzles.common.Direction.*;

/**
 * Represents a configuration for solving Astro puzzles
 *
 * @author RIT CS
 * @author Quang Huynh (qth9368)
 */

public class AstroConfig implements Configuration{
    public String[][] grid;
    private Coordinates astroCoords;
    private Coordinates goalCoords;
    public int rows;
    public int cols;
    private Collection<Configuration> neighbors;
    /**
     * Construct new AstroConfig
     *
     * @param filename name of Astro file
     * @throws IOException
     */
    public AstroConfig(String filename) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            neighbors = new HashSet<>();
            /** Read dimension info */
            String line = br.readLine();
            String dim[] = line.split("\\s+");
            rows = Integer.parseInt(dim[0]);  // first line, first int is row
            cols = Integer.parseInt(dim[1]); // first line, second int is col
            grid = new String[rows][cols];  // initialize grid array with specified dimensions

            /** Read goal info */
            line = br.readLine();
            String[] goalLine = line.split("\\s+");
            int goalRow = Integer.parseInt(goalLine[1].split(",")[0]);
            int goalColumn = Integer.parseInt(goalLine[1].split(",")[1]);
            grid[goalRow][goalColumn] = goalLine[0];
            goalCoords = new Coordinates(goalRow, goalColumn);

            /** Read astronaut info */
            line = br.readLine();
            String[] astroLine = line.split("\\s+");
            int astroRow = Integer.parseInt(astroLine[1].split(",")[0]);
            int astroColumn = Integer.parseInt(astroLine[1].split(",")[1]);
            grid[astroRow][astroColumn] = astroLine[0];
            astroCoords = new Coordinates(astroRow, astroColumn);

            /** Read number of robots and robot info */
            line = br.readLine();
            int numRobots = Integer.parseInt(line);
            Collection<Robot> robots = new HashSet<>();
            for(int i = 0; i < numRobots; i++) {
                line = br.readLine();
                String[] robotInfo = line.split("\\s+");
                char robotSymbol = robotInfo[0].charAt(0);
                int robotRow = Integer.parseInt(robotInfo[1].split(",")[0]);
                int robotColumn = Integer.parseInt(robotInfo[1].split(",")[1]);
                robots.add(new Robot(robotSymbol, robotRow, robotColumn));
                grid[robotRow][robotColumn] = String.valueOf(robotSymbol);
            }

            /** Assign Grid with remaining cells*/
            for(int row = 0; row < rows; row++) {
                for(int col = 0; col < cols; col++) {
                    if(grid[row][col] == null) {
                        grid[row][col] = ".";
                    }
                }
            }

            /** Process neighbor info */
            for(int row = 0; row < rows; row++) {  // read and process neighbor info for each cell
                line = br.readLine();
                //String[] neighborInfo = line.split("\\s+");
                for(int col = 0; col < cols; col++) {
                  //  processNeighbors(row, col, neighborInfo[col]);
                }
            }
        }
    }

    /**
     * AstroConfig constructor
     *
     * @param other other AstroConfig
     */
    public AstroConfig(AstroConfig other) {
        this.rows = other.rows;
        this.cols = other.cols;
        this.grid = copyGrid(other.grid);
        this.astroCoords = other.astroCoords;
        this.goalCoords = other.goalCoords;
        this.neighbors = new HashSet<>();
    }

    /**
     * Copies other grid
     *
     * @param original original grid
     * @return copied grid
     */
    private String[][] copyGrid(String[][] original) {
        String[][] newGrid = new String[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, newGrid[i], 0, original[i].length);
        }
        return newGrid;
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
        this.neighbors = new HashSet<>();
        for(int row = 0; row < this.rows; row++) {  // identity robots & astronaut
            for(int col = 0; col < this.cols; col++) {
                if(grid[row][col].equals(".") || grid[row][col].equals(this.grid[goalCoords.row()][goalCoords.col()])) {
                    continue;
                }
                checkAndAddNeighbor(row, col, NORTH);  // check movement for all 4 directions
                checkAndAddNeighbor(row, col, SOUTH);
                checkAndAddNeighbor(row, col, WEST);
                checkAndAddNeighbor(row, col, EAST);
            }
        }
        return this.neighbors;
    }

    /**
     * Checks if a piece can move and add neighbor (getNeighbors helper function)
     * A piece can move if there is another piece in the same row/column as selected piece
     * A piece moves towards the other piece until the path is blocked (it passes over the goal)
     * A piece can only move towards another piece in the same row/column
     *
     * @param row row index
     * @param col column index
     * @param direction cardinal direction (n, s, e, w)
     */
    private void checkAndAddNeighbor(int row, int col, Direction direction) {
        int deltaRow = 0;
        int deltaCol = 0;
        switch(direction) {
            case NORTH:
                deltaRow = -1;
                break;
            case SOUTH:
                deltaRow = 1;
                break;
            case WEST:
                deltaCol = -1;
                break;
            case EAST:
                deltaCol = 1;
                break;
        }
        int newRow = row + deltaRow;
        int newCol = col + deltaCol;
        while(newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && grid[newRow][newCol].equals(".")) {
            newRow += deltaRow;
            newCol += deltaCol;
        }
        if(newRow != row || newCol != col) {  // only add if the piece has moved
            AstroConfig newConfig = new AstroConfig(this);
            newConfig.grid[row][col] = ".";  // set old position to empty cell
            if(newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols) {
                newRow -= deltaRow;
                newCol -= deltaCol;
            }
            newConfig.grid[newRow][newCol] = this.grid[row][col];
            newConfig.updatePositions(row, col, newRow, newCol);
            this.neighbors.add(newConfig);
        }
    }

    /**
     * Update the positions after moving
     *
     * @param oldRow old row index
     * @param oldCol old column index
     * @param newRow row index that it is moved to
     * @param newCol column index that it is moved to
     */
    private void updatePositions(int oldRow, int oldCol, int newRow, int newCol) {
        if(astroCoords.row() == oldRow && astroCoords.col() == oldCol) {
            this.astroCoords = new Coordinates(newRow, newCol);
        }
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
        if(other instanceof AstroConfig) {
            AstroConfig otherAstro = (AstroConfig) other;
            result = Objects.equals(astroCoords, otherAstro.astroCoords)
                    && Objects.equals(goalCoords, ((AstroConfig) other).goalCoords);
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
        return Objects.hash(Arrays.deepHashCode(grid), astroCoords, goalCoords);
    }

    /**
     * Prints Astro grid
     *
     * @return Return each step of solution
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                sb.append(grid[row][col]).append(" ");
            }
            if(row < rows - 1) {  // removes space from last line
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Returns matrix
     *
     * @return return grid matrix of astro
     */
    public String[][] getGrid() {
        return grid;
    }
}
