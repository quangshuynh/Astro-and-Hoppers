package puzzles.hoppers.model;

import puzzles.common.Coordinates;
import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
     * @param board - the new board
     */
    private HoppersConfig(char[][]board){
        this.board = board;
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
                if(i % 2 == 0 && (board[i][j] == 'G' || board[i][j] == 'R')){ //if even row and is a green frog
                    //doing vertical and horizontal moves
                    if(isMoveValid(i - 4, j)){
                        result.add(new HoppersConfig(move(i, j, i - 4, j, true, board[i][j])));
                    }else if(isMoveValid(i + 4, j)){
                        result.add(new HoppersConfig(move(i, j, i + 4, j, true, board[i][j])));
                    }else if(isMoveValid(i, j - 4)){
                        result.add(new HoppersConfig(move(i, j, i, j - 4, true, board[i][j])));
                    }else if(isMoveValid(i, j + 4)){
                        result.add(new HoppersConfig(move(i, j, i, j + 4, true, board[i][j])));
                    }
                    //doing diagonal moves
                }else if(board[i][j] == 'G' || board[i][j] == 'R'){
                    if(isMoveValid(i - 2, j - 2)){
                        result.add(new HoppersConfig(move(i, j, i - 2, j - 2, false, board[i][j])));
                    }else if(isMoveValid(i - 2, j + 2)){
                        result.add(new HoppersConfig(move(i, j, i - 2, j + 2, false, board[i][j])));
                    }else if(isMoveValid(i + 2, j - 2)){
                        result.add(new HoppersConfig(move(i, j, i + 2, j - 2, false, board[i][j])));
                    }else if(isMoveValid(i + 2, j + 2)){
                        result.add(new HoppersConfig(move(i, j, i + 2, j + 2, false, board[i][j])));
                    }
                }
            }
        }
        return result;
    }

    private char[][] move(int originalRow, int originalCol, int newRow, int newCol, boolean longJump, char color){
        char[][] copyBoard = new char[row][col]; //creating a copy of the board from this config to move frogs
        System.arraycopy(board, 0, copyBoard, 0, row);
        copyBoard[newRow][newCol] = color; //moving the frog

        //process deleting
        int deleteFrogRow = 0;
        int deleteFrogCol = 0;
        if(longJump){ //deleting even row
            if(newRow > originalRow && newCol == originalCol){
                deleteFrogRow = originalRow + 2;
                deleteFrogCol = originalCol;
            }else if(newRow < originalRow && newCol == originalCol){
                deleteFrogRow = originalRow - 2;
                deleteFrogCol = originalCol;
            }else if(newCol > originalCol && newRow == originalRow){
                deleteFrogCol = originalCol + 2;
                deleteFrogRow = originalRow;
            }else if(newCol < originalCol && newRow == originalRow){
                deleteFrogCol = originalCol - 2;
                deleteFrogRow = originalRow;
            }
        }else{
            if(newRow > originalRow && newCol > originalCol){
                deleteFrogRow = originalRow + 1;
                deleteFrogCol = originalCol + 1;
            }else if(newRow > originalRow && newCol < originalCol){
                deleteFrogRow = originalRow + 1;
                deleteFrogCol = originalCol - 1;
            }else if(newRow < originalRow && newCol > originalCol){
                deleteFrogRow = originalRow - 1;
                deleteFrogCol = originalCol + 1;
            }else if(newRow < originalRow && newCol < originalCol){
                deleteFrogRow = originalRow - 1;
                deleteFrogCol = originalCol - 1;
            }
        }copyBoard[deleteFrogRow][deleteFrogCol] = '.';
        copyBoard[originalRow][originalCol] = '.';
        return copyBoard;
    }

    /**
     * A method used in get neighbors to see if a move is valid
     *
     * @param newRow - the target row to move to
     * @param newCol - the target col to move to
     * @return if that targeted row, col is valid
     */
    private boolean isMoveValid(int newRow, int newCol){
        boolean result = true;
        if(newRow > row - 1){
            result = false;
        }else if(newRow < 0){
            result = false;
        }else if(newCol > col - 1){
            result = false;
        }else if(newCol < 0){
            result = false;
        }else if(board[newRow][newCol] == 'G' || board[newRow][newCol] == 'R'){
            result = false;
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
        if(other instanceof HoppersConfig){
            HoppersConfig otherConfig = (HoppersConfig) other;
            for(int i = 0; i < row; i ++){
                for(int j = 0; j < col; j++){
                    if (this.board[i][j] != otherConfig.board[i][j]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * returns the hashcode of the id
     *
     * @return hash code of config id
     */
    @Override
    public int hashCode(){
        int result = 0;
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                result += Character.hashCode(board[i][j]);
            }
        }
        return result;
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
     * Returns matrix
     *
     * @return return grid matrix of astro
     */
    public char[][] getBoard() {
        return board;
    }

    /**
     * Get cell value
     *
     * @return cell value at coordinates
     */
    public char getCellValue(Coordinates coord) {
        int row = coord.row();
        int col = coord.col();
        return board[row][col];
    }
}
