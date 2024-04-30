package puzzles.hoppers.model;

import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.util.*;

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

    private String filename; // filename
    private Coordinates selectedCoords; //the coordinate selected

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
     * Creates a new model for the Hoppers
     *
     * @param filename - name of Hopper puzzle
     */
    public HoppersModel(String filename) throws IOException {
        this.filename = filename; //setting the file name
        currentConfig = new HoppersConfig(this.filename); //creating the initial config of HopperConfig to work with the solver for hints
    }

    /**
     * returns the total rows
     *
     * @return the total rows
     */
    public int getTotalRow() {
        return currentConfig.row;
    }

    /**
     * returns the total cols
     *
     * @return the total cols
     */
    public int getTotalCol() {
        return currentConfig.col;
    }

    /**
     * Does the next correct move for the user
     * based on the starting config using solver
     */
    public void hint() {
        Solver solver = new Solver(); //initialize the solver
        List<Configuration>path = solver.solve(currentConfig); //solve the current config (been moved by user)
        if(path.isEmpty()) {
            notifyObservers("No solution!");
            return;
        }else if(path.size() == 1) {
            notifyObservers("Already solved!");
            return;
        }

        if(!(path.get(1) instanceof HoppersConfig hoppersConfig)) { //error checking the solver, and getting the next correct step
            throw new RuntimeException("None HoppersConfig detected!");
        }
        this.currentConfig = hoppersConfig; //setting the config to the config with next move
        notifyObservers("Next Step!");
    }
    public boolean select(Coordinates selectedCoords){
        boolean result = false; //tells the observer if the selection is valid
        //process selected
        char content = currentConfig.getBoard()[selectedCoords.row()][selectedCoords.col()];
        Set<Character> validContents = Set.of('R', 'G');
        if(validContents.contains(content)) {
            notifyObservers("Selected \"" + content + "\" at (" + selectedCoords.row() + ", " + selectedCoords.col() + ")");
            result = true;
            this.selectedCoords = selectedCoords;
        } else {  // no piece selected
            notifyObservers("Invalid selection (" + selectedCoords.row() + ", " + selectedCoords.col() + ")");
            selectedCoords = null;
        }
        return result;
    }

    /**
     * Updates status to selected tile
     * Only can select frogs
     *
     * @param fromCoordinate - the coordinate from
     * @param toCoordinate - the coordinate to move to
     */
    public void move(Coordinates fromCoordinate, Coordinates toCoordinate) {
        //process from coordinate
        char content = currentConfig.getBoard()[fromCoordinate.row()][fromCoordinate.col()];
        //process to coordinate
        if(selectedCoords.row() + selectedCoords.col() % 2 == 0){ //process even row col
            //if can jump
            if(currentConfig.isMoveValid(fromCoordinate.row(), fromCoordinate.col(), toCoordinate.row(), toCoordinate.col(), true)){
                currentConfig = currentConfig.move(fromCoordinate.row(), fromCoordinate.col(), toCoordinate.row(), toCoordinate.col(), true, content);
                notifyObservers("Jumped from (" + fromCoordinate.row() + ", " + fromCoordinate.col() + ") to (" + toCoordinate.row() + ", " + toCoordinate.col() + ")");
                selectedCoords = null;
            }else{ //else
                selectedCoords = null;
                notifyObservers("Can't jump from (" + fromCoordinate.row() + ", " + fromCoordinate.col() + ") to (" + toCoordinate.row() + ", " + toCoordinate.col() + ")");
            }
        }else{ //process odd row col
            //if can jump
            if(currentConfig.isMoveValid(fromCoordinate.row(), fromCoordinate.col(), toCoordinate.row(), toCoordinate.col(), false)){
                currentConfig = currentConfig.move(fromCoordinate.row(), fromCoordinate.col(), toCoordinate.row(), toCoordinate.col(), false, content);
                notifyObservers("Jumped from (" + fromCoordinate.row() + ", " + fromCoordinate.col() + ") to (" + toCoordinate.row() + ", " + toCoordinate.col() + ")");
                selectedCoords = null;
            }else{ //else
                selectedCoords = null;
                notifyObservers("Can't jump from (" + fromCoordinate.row() + ", " + fromCoordinate.col() + ") to (" + toCoordinate.row() + ", " + toCoordinate.col() + ")");
            }
        }
    }

    /**
     * Loads a new puzzle board from file
     * @param file - The filename to load the puzzle from
     */
    public void load(String file) {
        try { //create a new Hoppers config and make it the new UI
            this.filename = file;
            this.currentConfig = new HoppersConfig(filename);
            notifyObservers("Loaded: " + filename);
        } catch(IOException e) {
            notifyObservers("Failed to load puzzle: " + filename);
        }
    }

    /**
     * Resets the puzzle board to the initial configuration based on the current file
     */
    public void reset() {
        load(filename); //call the load method to
        notifyObservers("Puzzle reset!");
    }

    /**
     * Get the value of row and column of a cell
     *
     * @param coordinate - the coordinate representing the row and col of the cell
     * @return the cell value
     */
    public char getCellValue(Coordinates coordinate) {
        int row = coordinate.row();
        int col = coordinate.col();
        if(row < 0 || row >= getTotalRow() || col < 0 || col >= getTotalCol()) {
            System.out.println("Coordinate out of the board");
            return '\0';
        }
        return currentConfig.getBoard()[row][col];
    }

    /**
     * The method to quit game
     * it shuts down the model and terminate the task
     */
    public void quit(){
        System.out.println("Goodbye");
        System.exit(0);
    }

    /**
     * Displays the board
     *
     * @return string representation of the Hoppers game board
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("   ");
        for(int col = 0; col < getTotalCol(); col++) {  // columns
            result.append(col + " ");
        }
        result.append("\n" + "  ");
        for(int col = 0; col < getTotalCol(); col++) {
            result.append("--");
        }
        result.append("\n");
        for(int row = 0; row < getTotalRow(); row++) {  // rows
            result.append(row + "| ");
            for(int col = 0; col < getTotalCol(); col++) {  // display grid
                result.append(currentConfig.getBoard()[row][col] + " ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    /**
     * Notify the observer about loading file on start
     *
     * @param file - filename
     */
    public void notifyLoad(String file) {
        notifyObservers("Loaded " + file);
    }
}