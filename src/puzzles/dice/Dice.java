package puzzles.dice;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.util.*;

/**
 * Entry point for Dice Puzzle Solver
 * Validates command-line arguments and initiates solving process
 *
 * @author RIT CS
 * @author Quang Huynh (qth9368)
 */
public class Dice {
    /**
     * Main method used to call common solver and display solution and steps to output
     *
     * @param args command line argument (expect for file)
     * @throws IOException if file not found
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.out.println("Usage: java Dice start end die1 die2...");
        } else {
            String start = args[0];
            String end = args[1];
            List<Integer> numFaces = new ArrayList<>();
            List<Die> dice = new ArrayList<>();  // store dice
            for(int i = 2; i < args.length; i++) {   // Create die objects
                Die die = new Die(args[i]);
                dice.add(die);
                int faces = Integer.parseInt(args[i]);
                numFaces.add(faces);
                System.out.println("Die #" + (i - 2) + ": File: " + die.getFileName() + ", Faces: " + die.getNumFaces());
                HashMap<Character, List<Character>> facesAndNeighbors = die.getFacesAndNeighbors();
                for(Map.Entry<Character, List<Character>> entry : facesAndNeighbors.entrySet()) {  // print nieghbors
                    Character face = entry.getKey();
                    List<Character> neighbors = entry.getValue();
                    StringBuilder neighborsString = new StringBuilder();
                    for(int j = 0; j < neighbors.size(); j++) {
                        neighborsString.append(neighbors.get(j));
                        if(j < neighbors.size() - 1) {
                            neighborsString.append(", ");  // put commas between neighbors
                        }
                    }
                    System.out.println("\t" + face + "=[" + neighborsString + "]");
                }
            }

            System.out.println("Start: " + start + ", End: " + end);
            DiceConfig initialConfig = new DiceConfig(dice, start, end);  // initial dice config
            Solver solver = new Solver();  // instantiate solver
            List<Configuration> solution = solver.solve(initialConfig);
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