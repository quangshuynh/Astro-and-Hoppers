package puzzles.clock;

import puzzles.common.solver.Solver;
import puzzles.common.solver.Configuration;
import java.util.*;


/**
 * Entry point for Clock Puzzle Solver
 * Validates command-line arguments and initiates solving process
 *
 * @author RIT CS
 * @author Quang Huynh (qth9368)
 */

public class Clock {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java Clock hours start end");
        } else {
            int hours = Integer.parseInt(args[0]);  // hours
            int start = Integer.parseInt(args[1]);  // starting hour
            int end = Integer.parseInt(args[2]);  // ending hour

            System.out.println("Hours: " + hours + ", Start: " + start + ", End: " + end);
            ClockConfig iConfig = new ClockConfig(hours, start, end);  // initial clock config
            Solver solver = new Solver();  // instantiate solver
            List<Configuration> solution = solver.solve(iConfig);
            System.out.println("Total configs: " + solver.getTotalConfigs());
            System.out.println("Unique configs: " + solver.getUniqueConfigs());
            if(solution != null && !solution.isEmpty()) {  // print steps
                for(int stepNum = 0; stepNum < solution.size(); stepNum++) {
                    System.out.println("Step " + stepNum + ": " + solution.get(stepNum));
                }
            } else {
                System.out.println("No solution");
            }
        }
    }
}
