package puzzles.hoppers.model;

import puzzles.astro.model.AstroConfig;
import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


/**
 * The model for the Hoppers puzzle
 *
 * @author CS RIT
 * @author Kai Fan
 */
public class HoppersModel {
    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private HoppersConfig currentConfig;
    //Enumeration for representing the different states of the game
    private enum GameStatus {
        READY, // initial state
        ONGOING, // game in progress
        LOST,
        WON
    }
    private String filename; // filename
    private GameStatus gameStatus; //the game's current state

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<HoppersModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void notifyObservers(String msg) {
        for (var observer : observers) {
            observer.update(this, msg);
        }
    }

    /**
     * Creates a new game
     * @param filename name of Hopper puzzle
     */
    public HoppersModel(String filename) throws IOException {
        this.filename = filename;
        currentConfig = new HoppersConfig(this.filename);
    }

    /**
     * Get rows
     *
     * @return rows of grid
     */
    public int getRow() {
        return currentConfig.row;
    }

    /**
     * Get columns
     *
     * @return columns of grid
     */
    public int getCol() {
        return currentConfig.col;
    }

    /**
     * Does the next move for user
     * Hint
     */
    public void hint() {
        Solver solver = new Solver();
        List<Configuration>path = solver.solve(currentConfig);
        if(path.size() == 1) {
            notifyObservers("Already solved!");
            return;
        }
        else if(path.isEmpty()) {
            notifyObservers("No solution!");
            return;
        }
        if(!(path.get(1) instanceof HoppersConfig hoppersConfig)) {
            throw new RuntimeException("Next step is not AstroConfig!");
        }
        this.currentConfig = hoppersConfig;
        notifyObservers("Next step!");
    }

    /**
     * Loads a new puzzle configuration from file
     * @param filename The filename to load the puzzle from
     */
    public void load(String filename) {
        try {
            this.filename = filename;
            this.currentConfig = new HoppersConfig(filename);
            notifyObservers("Loaded: " + filename);
        } catch(IOException e) {
            notifyObservers("Failed to load puzzle: " + filename);
        }
    }

    /**
     * Resets the puzzle to the initial configuration based on the current file
     */
    public void reset() {
        load(filename);
        notifyObservers("Puzzle reset!");
    }

    /**
     * Get cell value of row and column of a cell
     *
     * @param coord coordinates (row, col)
     * @return the cell value
     */
    public char getCellValue(Coordinates coord) {
        int row = coord.row();
        int col = coord.col();
        if(row < 0 || row >= getRow() || col < 0 || col >= getCol()) {
            return '\0';  // empty
        }
        return currentConfig.getBoard()[row][col];
    }

    /**
     * Displays model grid
     *
     * @return string representation of astro game grid
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("   ");
        for(int col = 0; col < getCol(); col++) {  // columns
            sb.append(col + " ");
        }
        sb.append("\n" + "  ");
        for(int col = 0; col < getCol(); col++) {
            sb.append("--");
        }
        sb.append("\n");
        for(int row = 0; row < getRow(); row++) {  // rows
            sb.append(row + "| ");
            for(int col = 0; col < getCol(); col++) {  // display grid
                sb.append(currentConfig.getBoard()[row][col] + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}