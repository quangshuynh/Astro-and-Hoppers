package puzzles.astro.model;

import puzzles.common.Coordinates;
import puzzles.common.Direction;
import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Move;
import puzzles.common.solver.Solver;
import puzzles.common.Direction.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * The model for the Astro puzzle
 *
 * @author CS RIT
 * @author Quang Huynh (qth9368)
 */
public class AstroModel {
    private String EARTH_SYMBOL = "*";  // goal
    public String EMPTY_SYMBOL = ".";  // empty cell
    public String ASTRONAUT_SYMBOL = "A";
    private String filename;  // filename
    private Coordinates selectedCoords;  // selected tile coords
    /** the collection of observers of this model */
    private final List<Observer<AstroModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private AstroConfig currentConfig;

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<AstroModel, String> observer) {
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
     * @param filename name of Astro puzzle
     */
    public AstroModel(String filename) throws IOException {
        this.filename = filename;
        currentConfig = new AstroConfig(this.filename);
    }

    /**
     * Get rows
     *
     * @return rows of grid
     */
    public int getRow() {
        return currentConfig.rows;
    }

    /**
     * Get columns
     *
     * @return columns of grid
     */
    public int getCol() {
        return currentConfig.cols;
    }

    /**
     * returns current config
     *
     * @return current config
     */
    public AstroConfig getCurrentConfig() {
        return currentConfig;
    }

    /**
     * Loads a new puzzle configuration from a file.
     * @param filename The file name to load the puzzle from.
     */
    public void loadPuzzle(String filename) {
        try {
            this.filename = filename;
            this.currentConfig = new AstroConfig(filename);
            notifyObservers("Loaded: " + filename);
        } catch(IOException e) {
            notifyObservers("Failed to load puzzle: " + filename);
        }
    }

    /**
     * Does the next move for user
     */
    public void getHint() {
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
        if(!(path.get(1) instanceof AstroConfig astroConfig)) {  // if next step is not part of astroconfig
            throw new RuntimeException("Next step is not AstroConfig!");
        }
        this.currentConfig = astroConfig;
        notifyObservers("Next step!");
    }

    /**
     * Resets the puzzle to the initial configuration based on the current file.
     */
    public void resetPuzzle() {
        loadPuzzle(filename);
        notifyObservers("Puzzle reset!");
    }

    /**
     * Get content of row and column of a cell
     *
     * @param coord coordinates (row, col)
     * @return the cell value
     */
    public String getContent(Coordinates coord) {
        int row = coord.row();
        int col = coord.col();
        if(row < 0 || row >= getRow() || col < 0 || col >= getCol()) {
            return "";
        }
        return currentConfig.getGrid()[row][col];
    }

    /**
     * Updates status to selected tile
     * Only can select astronaut or robots
     *
     * @param row row selected
     * @param col colummn selected
     */
    public void select(int row, int col) {
        String content = getContent(new Coordinates(row, col));
        Set<String> validContents = Set.of("A", "B", "C", "D", "E", "F", "G", "H", "I");
        if(validContents.contains(content)) {
            notifyObservers("Selected " + content + " at (" + row + ", " + col + ")");
            selectedCoords = new Coordinates(row, col);
        } else {  // no piece selected
            selectedCoords = null;
            notifyObservers("No piece at (" + row + ", " + col + ")");
        }
    }

    /**
     * Make move
     * Can only make move towards another piece
     *
     * @param dir direction (n, s, e, w)
     */
    public void makeMove(Direction dir) {
        if(selectedCoords != null) {
            Coordinates nextMove = findNextObstacle(selectedCoords, dir);
            if(isValidMove(nextMove)) {
                currentConfig.moveSelected(selectedCoords, nextMove);
                notifyObservers("Moved \"" + getContent(nextMove) + " from " + selectedCoords + " to " + nextMove);
                selectedCoords = nextMove;   // update the selected coordinates after the move
                if(currentConfig.getCellValue(nextMove).equals(ASTRONAUT_SYMBOL) && nextMove.equals(currentConfig.getGoalCoords())) {
                    notifyObservers("Astronaut has reached the goal! Hooray!");
                }
            } else {
                notifyObservers("Can't move piece at " + selectedCoords + " " + dir);
            }
        } else {
            notifyObservers("Nothing selected to move!");
        }
    }

    /**
     * Helper method to move and find obstacles
     *
     * @param start starting coordinates (row, col)
     * @param dir cardinal direction (n, s, e, w)
     * @return coordinates to move
     */
    private Coordinates findNextObstacle(Coordinates start, Direction dir) {
        int cursorRow = 0;
        int cursorCol = 0;
        switch(dir) {
            case NORTH -> cursorRow = -1;
            case SOUTH -> cursorRow = 1;
            case EAST -> cursorCol = 1;
            case WEST -> cursorCol = -1;
        }
        int newRow = start.row() + cursorRow;
        int newCol = start.col() + cursorCol;
        while(newRow >= 0 && newRow < getRow() && newCol >= 0 && newCol < getCol()) {
            String cellContent = currentConfig.getCellValue(new Coordinates(newRow, newCol));
            if(!cellContent.equals(EMPTY_SYMBOL) && !cellContent.equals(EARTH_SYMBOL)) {  // stop before the obstacle or goal
                return new Coordinates(newRow - cursorRow, newCol - cursorCol);
            }
            newRow += cursorRow;
            newCol += cursorCol;
        }
        return null;
    }

    /**
     * Checks if the move is valid.
     *
     * @param coord the coordinates of the target cell
     * @return whether a move is valid or not
     */
    private boolean isValidMove(Coordinates coord) {
        if(coord == null) return false;
        String cellValue = currentConfig.getCellValue(coord);
        return cellValue.equals(EMPTY_SYMBOL) || cellValue.equals(EARTH_SYMBOL);
    }

    /**
     * The method to quit window/game
     */
    public void quit(){
        System.exit(0);
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
                sb.append(currentConfig.getGrid()[row][col] + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Notify loading file on start
     *
     * @param file filename
     */
    public void notifyLoad(String file) {
        notifyObservers("Loaded " + file);
    }
}