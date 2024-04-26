package puzzles.hoppers.solver;

import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;

import java.io.IOException;

public class Hoppers {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Hoppers filename");
        }else{
            HoppersConfig test = new HoppersConfig(args[0]);
            System.out.println(test);
        }
    }
}
