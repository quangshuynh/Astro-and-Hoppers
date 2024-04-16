package puzzles.astro.solver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

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
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            /** Read dimension info */
            String line = br.readLine();
            String dim[] = line.split("\\s+");
            int rows = Integer.parseInt(dim[0]);  // first line, first int is row
            int columns = Integer.parseInt(dim[1]); // first line, second int is col

            /** Read goal info */
            line = br.readLine();
            String[] goalLine = line.split("\\s+");
            char goalSymbol = goalLine[0].charAt(0);
            int goalRow = Integer.parseInt(goalLine[1].split(",")[0]);
            int goalColumn = Integer.parseInt(goalLine[1].split(",")[1]);

            /** Read astronaut info */
            line = br.readLine();
            String[] astroInfo = line.split("\\s+");
            char astroSymbol = astroInfo[0].charAt(0);
            int astroRow = Integer.parseInt(astroInfo[1].split(",")[0]);
            int astroColumn = Integer.parseInt(astroInfo[1].split(",")[1]);

            /** Read number of robots and robot info */
            line = br.readLine();
            int numRobots = Integer.parseInt(line);
            Collection<Robot> robots = new HashSet<>();
            for (int i = 0; i < numRobots; i++) {
                line = br.readLine();
                String[] robotInfo = line.split("\\s+");
                char robotSymbol = robotInfo[0].charAt(0);
                int robotRow = Integer.parseInt(robotInfo[1].split(",")[0]);
                int robotColumn = Integer.parseInt(robotInfo[1].split(",")[1]);
                robots.add(new Robot(robotSymbol, robotRow, robotColumn));
            }


        } catch(FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            System.exit(1);
        }
    }
}
