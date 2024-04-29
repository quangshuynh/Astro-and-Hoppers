package puzzles.hoppers.model;

import puzzles.common.Coordinates;
import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * The configuration class for the board game Hoppers, used to work with common BFS solver
 *
 * @author Kai Fan
 */
public class HoppersConfig implements Configuration{
    private char[][] board; //the board of the game
    public int row; //the number of row start from 1
    public int col; //the number of col start from 1

    /**
     * the main constructor of the HoppersConfig
     * @param filename - the file used
     * @throws IOException - IOE
     */
    public HoppersConfig(String filename) throws IOException {
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String[] dims = reader.readLine().split("\\s+");
            row = Integer.parseInt(dims[0]);
            col = Integer.parseInt(dims[1]);
            board = new char[row][col];
            //writing into the 2D array
            for(int i = 0; i < row; i++){
                String[] aRow = reader.readLine().split("\\s+");
                for(int j = 0; j < col; j++){
                    board[i][j] = aRow[j].charAt(0);
                }
            }
        }
    }

    /**
     * The copy constructor used to easily create another config
     *
     * @param other - the parent config
     */
    private HoppersConfig(HoppersConfig other){
        this.row = other.row;
        this.col = other.col;
        this.board = new char[row][col];
        for(int i = 0; i < this.row; i++){
            System.arraycopy(other.board[i], 0, this.board[i], 0, col);
        }
    }

    /**
     * It is solution when there's only red frog left
     *
     * @return true if only red frog is left
     */
    @Override
    public boolean isSolution() {
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                if(board[i][j] == 'G'){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * the get neighbors methods used to generate new configurations for the BFS solver
     *
     * @return a collection of configurations
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        HashSet<Configuration> result = new HashSet<>();

        //go through the frogs and move them, each move generate a new config
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                if(board[i][j] == 'G' || board[i][j] == 'R'){ //This executes anyway regardless of even odd coordinate
                    if(isMoveValid(i, j, i - 2, j - 2, false)){
                        result.add(new HoppersConfig(move(i, j, i - 2, j - 2, false, board[i][j])));
                    }else if(isMoveValid(i, j, i - 2, j + 2, false)){
                        result.add(new HoppersConfig(move(i, j, i - 2, j + 2, false, board[i][j])));
                    }else if(isMoveValid(i, j, i + 2, j - 2, false)){
                        result.add(new HoppersConfig(move(i, j, i + 2, j - 2, false, board[i][j])));
                    }else if(isMoveValid(i, j, i + 2, j + 2, false)){
                        result.add(new HoppersConfig(move(i, j, i + 2, j + 2, false, board[i][j])));
                    }
                }if((i % 2 == 0 && j % 2 == 0) && (board[i][j] == 'G' || board[i][j] == 'R')){ //if even row and is a frog
                    //doing vertical and horizontal moves
                    if(isMoveValid(i, j, i - 4, j, true)){
                        result.add(new HoppersConfig(move(i, j, i - 4, j, true, board[i][j])));
                    }else if(isMoveValid(i, j, i + 4, j, true)){
                        result.add(new HoppersConfig(move(i, j, i + 4, j, true, board[i][j])));
                    }else if(isMoveValid(i, j, i, j - 4, true)){
                        result.add(new HoppersConfig(move(i, j, i, j - 4, true, board[i][j])));
                    }else if(isMoveValid(i, j, i, j + 4, true)){
                        result.add(new HoppersConfig(move(i, j, i, j + 4, true, board[i][j])));
                    }
                    //doing diagonal moves if is a frog
                }
            }
        }
        return result;
    }

    /**
     * The method used my getNeighbors to generate a new board with frogs moved
     *
     * @param originalRow - original row of the frog
     * @param originalCol - original col of the frog
     * @param newRow - target row to jump to
     * @param newCol - target col to jump to
     * @param longJump - if is even cell
     * @param color - the color of the frog
     * @return - a board with frogs moved
     */
    private HoppersConfig move(int originalRow, int originalCol, int newRow, int newCol, boolean longJump, char color){
        HoppersConfig result = new HoppersConfig(this);
        char[][] copyBoard = result.board; //creating a copy of the board from this config to move frogs

        copyBoard[newRow][newCol] = color; //moving the frog

        //process deleting
        int deleteFrogRow = -1;
        int deleteFrogCol = -1;
        if(longJump){ //deleting even row col
            if(newRow == originalRow + 4 && newCol == originalCol){
                deleteFrogRow = originalRow + 2;
                deleteFrogCol = originalCol;
            }else if(newRow == originalRow - 4 && newCol == originalCol){
                deleteFrogRow = originalRow - 2;
                deleteFrogCol = originalCol;
            }else if(newCol == originalCol + 4 && newRow == originalRow){
                deleteFrogCol = originalCol + 2;
                deleteFrogRow = originalRow;
            }else if(newCol == originalCol - 4 && newRow == originalRow){
                deleteFrogCol = originalCol - 2;
                deleteFrogRow = originalRow;
            }
        }else{ //deleting odd row col
            if(newRow == originalRow + 2 && newCol == originalCol + 2){
                deleteFrogRow = originalRow + 1;
                deleteFrogCol = originalCol + 1;
            }else if(newRow == originalRow + 2 && newCol == originalCol - 2){
                deleteFrogRow = originalRow + 1;
                deleteFrogCol = originalCol - 1;
            }else if(newRow == originalRow - 2 && newCol == originalCol + 2){
                deleteFrogRow = originalRow - 1;
                deleteFrogCol = originalCol + 1;
            }else if(newRow == originalRow - 2 && newCol == originalCol - 2){
                deleteFrogRow = originalRow - 1;
                deleteFrogCol = originalCol - 1;
            }
        }copyBoard[deleteFrogRow][deleteFrogCol] = '.';
        copyBoard[originalRow][originalCol] = '.'; //make the original position valid to jump again
        return result;
    }

    /**
     *  the method used to check if a move is valid
     *  1. a move is valid if the targeted row and col are not outside the board
     *  2. a move is valid if the targeted row and col are not into an invalid space i.e.'*'
     *  3. a move is valid if the frog is indeed jumping over another green frog (Cannot be red frog)
     *
     * @param originalRow - the original row of the frog
     * @param originalCol - the original col of the frog
     * @param newRow - the targeted row the frog tries to jump to
     * @param newCol - the targeted col the frog tries to jump to
     * @param longJump - if even cell
     * @return returns true grants the permission for frog to jump and not violate puzzle rule
     */
    private boolean isMoveValid(int originalRow, int originalCol, int newRow, int newCol, boolean longJump){
        //process for if out of bound (valid check 1 from Javadoc)
        boolean result = true;
        if(newRow > row - 1){
            return false;
        }else if(newRow < 0){
            return false;
        }else if(newCol > col - 1){
            return false;
        }else if(newCol < 0){
            return false;
        }else if(board[newRow][newCol] != '.'){
            return false;
        }

        //check to see if targeted row col for the frog to jump is '*' (valid check 2 from Javadoc)
        if(board[newRow][newCol] == '*'){
            return false;
        }

        //Check to make sure the original frog is going to jump over a green frog (valid check 3 from javadoc)
        int checkFrogRow = 0;
        int checkFrogCol = 0;
        if(longJump){ //get coordinate to check even row col
            if(newRow == originalRow + 4 && newCol == originalCol){
                checkFrogRow = originalRow + 2;
                checkFrogCol = originalCol;
            }else if(newRow == originalRow - 4 && newCol == originalCol){
                checkFrogRow = originalRow - 2;
                checkFrogCol = originalCol;
            }else if(newCol == originalCol + 4 && newRow == originalRow){
                checkFrogCol = originalCol + 2;
                checkFrogRow = originalRow;
            }else if(newCol == originalCol - 4 && newRow == originalRow){
                checkFrogCol = originalCol - 2;
                checkFrogRow = originalRow;
            }
        }else { //get coordinate to check odd row col
            if (newRow == originalRow + 2 && newCol == originalCol + 2) {
                checkFrogRow = originalRow + 1;
                checkFrogCol = originalCol + 1;
            } else if (newRow == originalRow + 2 && newCol == originalCol - 2) {
                checkFrogRow = originalRow + 1;
                checkFrogCol = originalCol - 1;
            } else if (newRow == originalRow - 2 && newCol == originalCol + 2) {
                checkFrogRow = originalRow - 1;
                checkFrogCol = originalCol + 1;
            } else if (newRow == originalRow - 2 && newCol == originalCol - 2) {
                checkFrogRow = originalRow - 1;
                checkFrogCol = originalCol - 1;
            }
        }
        if(board[checkFrogRow][checkFrogCol] != 'G'){
            return false;
        }

        return result;
    }

    /**
     * the equal method compares the game board of both config
     *
     * @param other - the HoppersConfig that needs to be compared
     * @return true if two configs' game board are the same
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if(other instanceof HoppersConfig){
            HoppersConfig otherConfig = (HoppersConfig) other;
            result = Arrays.deepEquals(board, otherConfig.board);
        }
        return result;
    }

    /**
     * returns the hashcode of the id
     *
     * @return hash code of config id
     */
    @Override
    public int hashCode(){
        return Arrays.deepHashCode(board);
    }

    /**
     * the to String method for the Hoppers configuration. It prints out the board
     *
     * @return the string representation of the board
     */
    @Override
    public String toString() {
        String result = "";
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                result += board[i][j] + " ";
            }
            result = result.substring(0, result.length() - 1);
            result += "\n";
        }
        result = result.substring(0, result.length() - 1);
        return result;
    }

    /**
     * Returns the game board
     * This method is used by the MVC model
     *
     * @return the board of the game
     */
    public char[][] getBoard() {
        return this.board;
    }

    /**
     * Get the value of a cell
     * This method is used by the MVC model
     *
     * @return cell value at coordinates
     */
    public char getCellValue(Coordinates coord) {
        int row = coord.row();
        int col = coord.col();
        return board[row][col];
    }
}
