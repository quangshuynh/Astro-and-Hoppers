package puzzles.astro.solver;

/**
 * Entry point for Astro Puzzle Solver
 * Validates command-line arguments and initiates solving process
 *
 * @author RIT CS
 * @author Quang Huynh (qth9368)
 */
public class Astro {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Astro filename");
        }
    }
}
