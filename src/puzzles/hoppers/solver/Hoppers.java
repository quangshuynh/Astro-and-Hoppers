package puzzles.hoppers.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;

import java.io.IOException;
import java.util.List;

public class Hoppers {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Hoppers filename");
        }else{
            System.out.println("File: data/hoppers/" + args[0]);
            HoppersConfig start = new HoppersConfig(args[0]);
            System.out.println(start);
            Solver solver = new Solver();
            List<Configuration> result = solver.solve(start);
            System.out.println("Total configs: " + solver.getTotalConfigs());
            System.out.println("Unique configs: " + solver.getUniqueConfigs());
            if(result != null && !result.isEmpty()) {  // print steps
                for(int stepNum = 0; stepNum < result.size(); stepNum++) {
                    System.out.println("Step " + stepNum + ": \n" + result.get(stepNum) + "\n");
                }
            } else {
                System.out.println("No solution");
            }
        }
    }
}
