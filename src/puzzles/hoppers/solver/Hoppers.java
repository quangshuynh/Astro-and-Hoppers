package puzzles.hoppers.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;

import java.io.IOException;
import java.util.List;

/**
 * A class used to call the common solver and solve the puzzle
 *
 * @author Kai Fan
 */
public class Hoppers {
    /**
     * The main method used to call the common solver and display solution and step to standard output
     *
     * @param args - the command line argument (expect a file)
     * @throws IOException -
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Hoppers filename");
        }else{
            System.out.println("File: data/hoppers/" + args[0]);
            HoppersConfig hoppersConfig = new HoppersConfig(args[0]);
            System.out.println(hoppersConfig);
            Solver solver = new Solver();
            List<Configuration> solution = solver.solve(hoppersConfig);
            System.out.println("Total configs: " + solver.getTotalConfigs());
            System.out.println("Unique configs: " + solver.getUniqueConfigs());
            if(!solution.isEmpty() && solution.get(solution.size() - 1).isSolution()) {
                for(int stepNum = 0; stepNum < solution.size(); stepNum++) {
                    System.out.println("Step " + stepNum + ": \n" + solution.get(stepNum) + "\n");
                }
            } else {
                System.out.println("No solution");
            }
        }
    }
}
