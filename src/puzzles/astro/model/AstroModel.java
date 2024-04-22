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
    private Coordinates selectedPieceCoordinates;
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

    public void getHint() {
        try {
            if(!currentConfig.isSolution()) {
                notifyObservers("Next step!");
                Solver solver = new Solver();
                List<Configuration> solution = solver.solve(currentConfig);
                // todo
            } else {
                notifyObservers("Already solved");
            }
        } catch(Exception e) {
            notifyObservers("No solution!");
        }
    }

    /**
     * Resets the puzzle to the initial configuration based on the current file.
     */
    public void resetPuzzle() {
        try {
            this.currentConfig = new AstroConfig(this.filename);
            notifyObservers("Puzzle reset!");
        } catch(IOException e) {
            notifyObservers("Failed to reset puzzle");
        }
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
        return currentConfig.getGrid()[row][col];
    }

    /**
     * Make move
     *
     * @param dir direction (n, s, e, w)
     */
    public void makeMove(Direction dir){
        Coordinates coord = new Coordinates(0, 0);
        int row = getRow();
        int col = getCol();
        int i = 1;
        String symbol = ".";  // empty
        boolean blocked = false;
        //gameState to later
        if(selectedPieceCoordinates != null) {
            try {
                if (currentConfig.getGrid()[row][col].equals(EMPTY_SYMBOL)) {
                    notifyObservers("Can't move piece at " + selectedPieceCoordinates.toString() + " " + dir.toString());
                    blocked = true;
                } else {
                    symbol = currentConfig.grid[row][col];
                }
            } catch (IndexOutOfBoundsException ibe) {
                notifyObservers("Can't move piece at " + selectedPieceCoordinates.toString() + " " + dir.toString());
                blocked = true;
            }
            if (dir == Direction.EAST) {
                while (!blocked) {
                    try {
                        if (currentConfig.grid[row][col + i].equals(EMPTY_SYMBOL)) {
                            i++;
                        } else if (!Objects.equals(currentConfig.grid[row][col + i], EMPTY_SYMBOL)) {
                            blocked = true;
                            currentConfig.grid[row][col] = EMPTY_SYMBOL;
                            if (!Objects.equals(currentConfig.grid[row][col + i], EARTH_SYMBOL)) {
                                currentConfig.grid[row][col + i - 1] = symbol;
                                coord = new Coordinates(row, (col + i - 1));
                                // maybe increment move count?
                            } else if (currentConfig.grid[row][col + i].equals(EARTH_SYMBOL)) {
                                //gameState won
                                //System.out.println("You have won!");
                                blocked = true;
                                currentConfig.grid[row][col + i] = symbol;
                                // maybe increment move count?
                            }
                            i = 1;
                        }
                    } catch (IndexOutOfBoundsException ibe) {
                        i = 1;
                        System.out.println("Fell off!");
                        blocked = true;
                    }
                }
                blocked = false;
            }
            if (dir == Direction.WEST) {
                while (!blocked) {
                    try {
                        if (currentConfig.grid[row][col - i].equals(EMPTY_SYMBOL)) {
                            blocked = false;
                            i++;
                        } else if (!Objects.equals(currentConfig.grid[row][col - i], EMPTY_SYMBOL)) {
                            blocked = true;
                            currentConfig.grid[row][col] = EMPTY_SYMBOL;
                            if (!Objects.equals(currentConfig.grid[row][col - i], EARTH_SYMBOL)) {
                                currentConfig.grid[row][col - i + 1] = symbol;
                                coord = new Coordinates(row, (col - i + 1));
                                // maybe increment move count?
                            } else if (currentConfig.grid[row][col - i].equals(EARTH_SYMBOL)) {
                                //game state won
                                // System.out.println("You have won!");
                                blocked = true;
                                currentConfig.grid[row][col - i] = symbol;
                            }
                            i = 1;
                        }
                    } catch (IndexOutOfBoundsException ibe) {
                        i = 1;
                        System.out.println("Fell off!");
                        blocked = true;
                    }
                }
                blocked = false;
            }
            if (dir == Direction.SOUTH) {
                while (!blocked) {
                    try {
                        if (Objects.equals(currentConfig.grid[row + i][col], EMPTY_SYMBOL)) {
                            i++;
                        } else if (!currentConfig.grid[row + i][col].equals(EMPTY_SYMBOL)) {
                            blocked = true;
                            currentConfig.grid[row][col] = EMPTY_SYMBOL;
                            if (!Objects.equals(currentConfig.grid[row + i][col], EARTH_SYMBOL)) {
                                //notify observer
                                currentConfig.grid[row + i + 1][col] = symbol;
                                coord = new Coordinates((row + i + 1), col);
                                // maybe increment move count?
                            } else if (currentConfig.grid[row + i][col].equals(EARTH_SYMBOL)) {
                                //game statsu won
                                // System.out.println("You have won!");
                                blocked = true;
                                currentConfig.grid[row + i][col] = symbol;
                            }
                            i = 1;
                        }
                    } catch(IndexOutOfBoundsException ibe) {
                        i = 1;
                        System.out.println("You fell off!");
                        blocked = true;
                    }
                }
                blocked = false;
            }
            if (dir == Direction.NORTH) {
                while (!blocked) {
                    try {
                        if (currentConfig.grid[row - i][col].equals(EMPTY_SYMBOL)) {
                            i++;
                        } else if (!Objects.equals(currentConfig.grid[row - i][col], EMPTY_SYMBOL)) {
                            blocked = true;
                            currentConfig.grid[row][col] = EMPTY_SYMBOL;
                            if(!Objects.equals(currentConfig.grid[row - i][col], EARTH_SYMBOL)) {
                                //notify observer
                                currentConfig.grid[row - i + 1][col] = symbol;
                                coord = new Coordinates((row - i + 1), col);
                                // maybe increment move count?
                            } else if (currentConfig.grid[row - i][col].equals(EARTH_SYMBOL)) {
                                //gameState won
                                //System.out.println("You have won!");
                                blocked = true;
                                currentConfig.grid[row - i][col] = symbol;
                            }
                            i = 1;
                        }
                    } catch (IndexOutOfBoundsException ibe) {
                        i = 1;
                        System.out.println("Fell off!");
                        blocked = true;
                    }
                }
                blocked = false;
            }
        } else {
            notifyObservers("You must select a piece before you move");
        }
    }

    /**
     * Updates status to selected tile
     * Only can select astronaut or robots
     *
     * @param row row selected
     * @param col colummn selected
     */
    public void select_status(int row, int col) {
        String content = getContent(new Coordinates(row, col));
        if(content.equals("A") || content.equals("B") || content.equals("C") ||
                content.equals("D") || content.equals("E") || content.equals("F") ||
                content.equals("G") || content.equals("H") || content.equals("I")) {
            selectedPieceCoordinates = new Coordinates(row, col);
            notifyObservers("Selected (" + row + ", " + col + ")");
        } else {  // no piece selected
            notifyObservers("No piece at (" + row + ", " + col + ")");
        }
    }

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
}
