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
            HoppersConfig hoppersConfig = new HoppersConfig(args[0]);
            System.out.println(hoppersConfig);
            Solver solver = new Solver();
            List<Configuration> solution = solver.solve(hoppersConfig);
            System.out.println("Total configs: " + solver.getTotalConfigs());
            System.out.println("Unique configs: " + solver.getUniqueConfigs());
            if(solution == null || solution.isEmpty()){
                System.out.println("No solution");
            }else{
                int counter = 0;
                for(Configuration hopperConfig : solution){
                    System.out.println("Step " + counter + ":\n" + hopperConfig.toString() + "\n");
                    counter++;
                }
            }
        }
    }
}
