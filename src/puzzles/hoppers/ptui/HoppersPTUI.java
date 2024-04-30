package puzzles.hoppers.ptui;

import javafx.scene.control.Label;
import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;

import java.io.IOException;
import java.util.Scanner;

public class HoppersPTUI implements Observer<HoppersModel, String> {
    private HoppersModel model;
    private Coordinates selectedLabel_1_Coordinate; //the first label's coordinate
    private Coordinates selectedLabel_2_Coordinate; //the second label's coordinate
    private boolean validFirstSelect; //keep track if first select is valid

    public void init(String filename) throws IOException {
        this.model = new HoppersModel(filename);
        this.model.addObserver(this);
        this.selectedLabel_1_Coordinate = null;
        this.selectedLabel_2_Coordinate = null;
        this.validFirstSelect = false;
        displayHelp();
    }

    @Override
    public void update(HoppersModel model, String data) {
        // for demonstration purposes
        System.out.println(data);
        System.out.println(model);
    }

    private void displayHelp() {
        System.out.println( "h(int)              -- hint next move" );
        System.out.println( "l(oad) filename     -- load new puzzle file" );
        System.out.println( "s(elect) r c        -- select cell at r, c" );
        System.out.println( "q(uit)              -- quit the game" );
        System.out.println( "r(eset)             -- reset the current game" );
    }

    public void run() {
        Scanner in = new Scanner( System.in );
        for ( ; ; ) {
            System.out.print( "> " );
            String line = in.nextLine();
            String[] input = line.split( "\\s+" );
            if (input.length > 0) {
                if (input[0].startsWith( "q" )) {
                    quit();
                }else if(input[0].startsWith("l")) {  // load
                    if(input.length != 2){ //error checking
                        System.out.println("Wrong File Input Detected, Exiting");
                        quit();
                    }else{
                        model.load(input[1]);
                    }
                }else if(input[0].startsWith("r")) {
                    model.reset();
                }else if(input[0].startsWith("s")){
                    if(input.length != 3){
                        System.out.println("Wrong format for selecting, try again");
                        displayHelp();
                    }else{
                        select(Integer.parseInt(input[1]), Integer.parseInt(input[2]));
                    }
                }else if(input[0].startsWith("h")){
                    model.hint();
                }
                else {
                    displayHelp();
                }
            }
        }
    }
    private void select(int row, int col) {
        if(selectedLabel_1_Coordinate == null){
            selectedLabel_1_Coordinate = new Coordinates(row, col); //first select
            validFirstSelect = model.select(selectedLabel_1_Coordinate);
        }
        if(!validFirstSelect){ //reset if first select is invalid
            selectedLabel_1_Coordinate = null;
        }
        if(validFirstSelect && !new Coordinates(row, col).equals(selectedLabel_1_Coordinate)){ //second select
            selectedLabel_2_Coordinate = new Coordinates(row, col);
            moveIt();
        }
    }

    private void moveIt(){
        model.move(selectedLabel_1_Coordinate, selectedLabel_2_Coordinate);  // notify observer & select
        //resetting the select
        selectedLabel_1_Coordinate = null;
        selectedLabel_2_Coordinate = null;
        validFirstSelect = false;
    }

    public void quit(){
        model.quit();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            try {
                HoppersPTUI ptui = new HoppersPTUI();
                ptui.init(args[0]);
                ptui.run();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
}
