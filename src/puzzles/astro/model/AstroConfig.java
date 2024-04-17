package puzzles.astro.model;

import puzzles.astro.solver.Astro;
import puzzles.astro.solver.Robot;
import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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
    private Collection<Configuration> neighbors;
    /**
     * Construct new AstroConfig
     *
     * @param filename name of Astro file
     * @throws IOException
     */
    public AstroConfig(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
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
            grid[goalRow][goalColumn] = "*";
            goalCoords = goalRow + "," + goalColumn;


            /** Read astronaut info */
            line = br.readLine();
            String[] astroLine = line.split("\\s+");
            int astroRow = Integer.parseInt(astroLine[1].split(",")[0]);
            int astroColumn = Integer.parseInt(astroLine[1].split(",")[1]);
            grid[astroRow][astroColumn] = "A";
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
        List<Configuration> neighbors = new ArrayList<>();
        int[] astroPosition = Arrays.stream(astroCoords.split(",")).mapToInt(Integer::parseInt).toArray();
        int astroRow = astroPosition[0];
        int astroCol = astroPosition[1];
        neighbors.addAll(generateMoves(astroRow, astroCol, "A"));
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++) {
                if(grid[row][col].matches("[A-Z]") && !grid[row][col].equals("A")) { // robots
                    neighbors.addAll(generateMoves(row, col, grid[row][col]));
                }
            }
        }
        return neighbors;
    }

    /**
     * Generates all possible moves for a piece at a given position.
     */
    private List<Configuration> generateMoves(int row, int col, String piece) {
        List<Configuration> moves = new ArrayList<>();
        int[] rowOffsets = {-1, 1, 0, 0};
        int[] colOffsets = {0, 0, -1, 1};
        for(int i = 0; i < rowOffsets.length; i++) {
            int newRow = row;
            int newCol = col;
            while(canMove(newRow + rowOffsets[i], newCol + colOffsets[i])) {
                newRow += rowOffsets[i];
                newCol += colOffsets[i];
            }
            if(newRow != row || newCol != col) {
                AstroConfig newConfig = new AstroConfig(this);
                newConfig.grid[row][col] = ".";
                newConfig.grid[newRow][newCol] = piece;
                if(piece.equals("A")) {
                    newConfig.astroCoords = newRow + "," + newCol;
                }
                moves.add(newConfig);
            }
        }
        return moves;
    }

    /**
     * Checks if a cell is within bounds and not occupied by another piece.
     */
    private boolean canMove(int newRow, int newCol) {
        return newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && grid[newRow][newCol].equals(".");
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
}
