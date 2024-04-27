package puzzles.astro.ptui;

import puzzles.common.Coordinates;
import puzzles.common.Direction;
import puzzles.common.Observer;
import puzzles.astro.model.AstroModel;
import puzzles.common.solver.Move;

import java.io.IOException;
import java.util.Scanner;
/**
 * The plain text UI for the Astro puzzle.
 * This class encapsulates both View and Controller portions of MVC architecture
 *
 * @author CS RIT
 * @author Quang Huynh (qth9368)
 */
public class AstroPTUI implements Observer<AstroModel, String> {
    private AstroModel model;
    private String filename;

    /**
     * Create the PTUI
     *
     * @param filename filename of Astro puzzle
     */
    public void init(String filename) throws IOException {
        this.filename = filename;
        this.model = new AstroModel(filename);
        this.model.addObserver(this);
        displayHelp();
    }

    /**
     * The update for the PTUI prints the model and other states
     *
     * @param model the model
     * @param data data from model sent to view to inform about what happened
     */
    @Override
    public void update(AstroModel model, String data) {
        // for demonstration purposes
        System.out.println(data);
        System.out.println(model.toString());
    }

    /**
     * Displays list of available commands in standard output
     */
    private void displayHelp() {
        System.out.println( "h(int)              -- hint next move" );
        System.out.println( "l(oad) filename     -- load new puzzle file" );
        System.out.println( "s(elect) r c        -- select cell at r, c" );
        System.out.println( "m(ove) n|s|e|w      -- move selected piece in direction" );
        System.out.println( "q(uit)              -- quit the game" );
        System.out.println( "r(eset)             -- reset the current game" );
    }

    /**
     * The run loop prompts for user input and makes calls into the Model.
     */
    public void run() {
        Scanner in = new Scanner( System.in );
        for ( ; ; ) {
            System.out.print( "> " );
            String line = in.nextLine();
            String[] words = line.split( "\\s+" );
            if (words.length > 0) {
                if (words[0].startsWith("q")) {  // quit
                    model.quit();
                    break;
                } else if(words[0].startsWith("r")) {  // reset
                    model.resetPuzzle();
                } else if(words[0].startsWith("l")) {  // load
                    model.loadPuzzle(words[1]);
                } else if(words[0].startsWith("m")) {  // make move
                    switch(words[1]) {
                        case "n" -> model.makeMove(Direction.NORTH);
                        case "s" -> model.makeMove(Direction.SOUTH);
                        case "e" -> model.makeMove(Direction.EAST);
                        case "w" -> model.makeMove(Direction.WEST);
                        default -> {
                            System.out.println("Invalid direction!");
                            displayHelp();
                        }
                    }
                } else if(words[0].startsWith("h")) {  // hint
                    model.getHint();
                } else if(words[0].startsWith("s")) {
                    try {
                        int row = Integer.parseInt(words[1]);
                        int col = Integer.parseInt(words[2]);
                        this.model.select(row, col);
                    } catch(Exception ignored) {
                        //
                    }
                } else {
                    displayHelp();
                }
            }
        }
    }

    /**
     * The main routine.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java AstroPTUI filename");
        } else {
            try {
                AstroPTUI ptui = new AstroPTUI();
                ptui.init(args[0]);
                ptui.run();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
}
