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
     * Checks if a piece can move and adds a neighbor configuration if possible.
     * A piece can only move if there is another piece in the same row or column.
     * It will move towards the other piece and stop one tile before it
     * A piece must travel over the goal if there isn't another piece blocking it
     * The astronaut ("A") has to end at goal
     *
     * @param row row index
     * @param col column index
     * @param direction cardinal direction (n, s, e, w)
     */
    private void checkAndAddNeighbor(int row, int col, Direction direction) {
        int dRow = 0;
        int dCol = 0;
        switch (direction) {
            case NORTH:
                dRow = -1;
                break;
            case SOUTH:
                dRow = 1;
                break;
            case EAST:
                dCol = 1;
                break;
            case WEST:
                dCol = -1;
                break;
        }
        int nextRow = row + dRow;
        int nextCol = col + dCol;
        boolean foundPiece = false;
        while(nextRow >= 0 && nextRow < rows && nextCol >= 0 && nextCol < cols) {  // continue until boundary
            if(!grid[nextRow][nextCol].equals(".")) {  // continue until piece is found
                foundPiece = true;
                break;
            }
            nextRow += dRow;
            nextCol += dCol;
        }
        if(foundPiece && !(nextRow == goalCoords.row() && nextCol == goalCoords.col())) {  // move one tile less
            nextRow -= dRow;
            nextCol -= dCol;
        }
        if(nextRow >= 0 && nextRow < rows && nextCol >= 0 && nextCol < cols && !(nextRow == row && nextCol == col)) {
            if(grid[nextRow][nextCol].equals(".") || (nextRow == goalCoords.row() && nextCol == goalCoords.col())) {
                AstroConfig newConfig = new AstroConfig(this);
                newConfig.grid[row][col] = ".";
                newConfig.grid[nextRow][nextCol] = grid[row][col];
                if(grid[row][col].equals("A")) {  // update astro coordinates
                    newConfig.astroCoords = new Coordinates(nextRow, nextCol);
                }
                this.neighbors.add(newConfig);
            }
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
