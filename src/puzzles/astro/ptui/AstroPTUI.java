package puzzles.astro.ptui;

import puzzles.common.Observer;
import puzzles.astro.model.AstroModel;

import java.io.IOException;
import java.util.Scanner;

public class AstroPTUI implements Observer<AstroModel, String> {
    private AstroModel model;

    public void init(String filename) throws IOException {
        this.model = new AstroModel(filename);
        this.model.addObserver(this);
        displayHelp();
    }

    @Override
    public void update(AstroModel model, String data) {
        // for demonstration purposes
        System.out.println(data);
        System.out.println(model);
    }

    private void displayHelp() {
        System.out.println( "h(int)              -- hint next move" );
        System.out.println( "l(oad) filename     -- load new puzzle file" );
        System.out.println( "s(elect) r c        -- select cell at r, c" );
        System.out.println( "m(ove) n|s|e|w      -- move selected piece in direction" );
        System.out.println( "q(uit)              -- quit the game" );
        System.out.println( "r(eset)             -- reset the current game" );
    }

    public void run() {
        Scanner in = new Scanner( System.in );
        for ( ; ; ) {
            System.out.print( "> " );
            String line = in.nextLine();
            String[] words = line.split( "\\s+" );
            if (words.length > 0) {
                if (words[0].startsWith( "q" )) {
                    break;
                }
                else {
                    displayHelp();
                }
            }
        }
    }

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
