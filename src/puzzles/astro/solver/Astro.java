package puzzles.astro.solver;

import puzzles.astro.model.AstroConfig;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.util.List;

/**
 * Entry point for Astro Puzzle Solver
 * Validates command-line arguments and initiates solving process
 *
 * @author RIT CS
 * @author Quang Huynh (qth9368)
 */
public class Astro {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Astro filename");
        }
        String fileName = args[0];
        System.out.println("File: data/astro/" + fileName);
        AstroConfig astroConfig = new AstroConfig(fileName);
        System.out.println(astroConfig);
        Solver solver = new Solver();
        List<Configuration> solution = solver.solve(astroConfig);
        System.out.println("Total configs: " + solver.getTotalConfigs());
        System.out.println("Unique configs: " + solver.getUniqueConfigs());
        if(solution != null && !solution.isEmpty()) {  // print steps
            for(int stepNum = 0; stepNum < solution.size(); stepNum++) {
                System.out.println("Step " + stepNum + ":\n" + solution.get(stepNum));
            }
        } else {
            System.out.println("No solution");
        }
    }
}
