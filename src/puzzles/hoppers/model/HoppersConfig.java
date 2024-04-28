package puzzles.hoppers.model;

import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * The configuration class for the board game Hoppers, used to work with common BFS solver
 *
 * @author Kai Fan
 */
public class HoppersConfig implements Configuration{ //todo hopper config is f***ed skip to work on MVC
    private char[][] board; //the board of the game
    private static int row; //the number of row start from 1
    private static int col; //the number of col start from 1

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
    public boolean isSolution() { //todo returning true when it isn't suppose to for test 1, and for the rest, it stuck in infinite loop
        boolean result = false;
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                if(this.board[i][j] == 'R'){
                    result = true;
                }else if(this.board[i][j] == 'G'){
                    result = false;
                }
            }
        }
        return result;
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
                if(board[i][j] == 'G' || board[i][j] == 'R') {
                    if(i % 2 == 0){ //if even row and is a frog
                        //doing vertical and horizontal moves (aka generating new configs)
                        if (isMoveValid(i ,j, i - 4, j, true)) {
                            result.add(new HoppersConfig(move(i, j, i - 4, j, true, board[i][j])));
                        } else if (isMoveValid(i, j, i + 4, j, true)) {
                            result.add(new HoppersConfig(move(i, j, i + 4, j, true, board[i][j])));
                        } else if (isMoveValid(i, j, i, j - 4, true)) {
                            result.add(new HoppersConfig(move(i, j, i, j - 4, true, board[i][j])));
                        } else if (isMoveValid(i, j, i, j + 4, true)) {
                            result.add(new HoppersConfig(move(i, j, i, j + 4, true, board[i][j])));
                        }
                        //doing diagonal moves (also generating new configs)
                    }if(isMoveValid(i, j, i - 2, j - 2, false)){
                        result.add(new HoppersConfig(move(i, j, i - 2, j - 2, false, board[i][j])));
                    }else if(isMoveValid(i, j, i - 2, j + 2, false)){
                        result.add(new HoppersConfig(move(i, j, i - 2, j + 2, false, board[i][j])));
                    }else if(isMoveValid(i, j, i + 2, j - 2, false)){
                        result.add(new HoppersConfig(move(i, j, i + 2, j - 2, false, board[i][j])));
                    }else if(isMoveValid(i, j, i + 2, j + 2, false)){
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

        //process deleting of the in between frog
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
        }
        //make the jumped over frog space available, and the original place available
        copyBoard[deleteFrogRow][deleteFrogCol] = '.';
        copyBoard[originalRow][originalCol] = '.';
        return copyBoard;
    }

    /**
     * A method used in get neighbors to see if a move is valid
     *
     * @param oldRow - the old row
     * @param oldCol - the old col
     * @param newRow - the target row to move to
     * @param newCol - the target col to move to
     * @return if that targeted row, col is valid
     */
    private boolean isMoveValid(int oldRow, int oldCol, int newRow, int newCol, boolean longJump){
        int detectInBetweenRow = 0;
        int detectInBetweenCol = 0;
        if(longJump){ //detect to make sure it is jumping over a frog
            if(newRow > oldRow && newCol == oldCol){
                detectInBetweenRow = oldRow + 2;
                detectInBetweenCol = oldCol;
            }else if(newRow < oldRow && newCol == oldCol){
                detectInBetweenRow = oldRow - 2;
                detectInBetweenCol = oldCol;
            }else if(newCol > oldCol && newRow == oldRow){
                detectInBetweenCol = oldCol + 2;
                detectInBetweenRow = oldRow;
            }else if(newCol < oldCol && newRow == oldRow){
                detectInBetweenCol = oldCol - 2;
                detectInBetweenRow = oldRow;
            }
        }else{
            if(newRow > oldRow && newCol > oldCol){
                detectInBetweenRow = oldRow + 1;
                detectInBetweenCol = oldCol + 1;
            }else if(newRow > oldRow && newCol < oldCol){
                detectInBetweenRow = oldRow + 1;
                detectInBetweenCol = oldCol - 1;
            }else if(newRow < oldRow && newCol > oldCol){
                detectInBetweenRow = oldRow - 1;
                detectInBetweenCol = oldCol + 1;
            }else if(newRow < oldRow && newCol < oldCol){
                detectInBetweenRow = oldRow - 1;
                detectInBetweenCol = oldCol - 1;
            }
        }
        //continue detecting to make sure the new place to move is not outside the game board
        if(newRow >= row){
            return false;
        }else if(newRow < 0){
            return false;
        }else if(newCol >= col){
            return false;
        }else if(newCol < 0){
            return false;
        }
        //detecting if the target location to move is valid
        else if(board[newRow][newCol] != '.'){
            return false;
        }
        //only allow jump when a frog is in between (video didn't tell if we are allow to jump without frog in between)
        return board[detectInBetweenRow][detectInBetweenCol] == 'G';
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
            HoppersConfig otherHoppers = (HoppersConfig) other;
            result = Arrays.deepEquals(board, otherHoppers.board);
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
}
