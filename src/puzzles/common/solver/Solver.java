package puzzles.common.solver;

import java.util.LinkedList;
import java.util.*;

/**
 * A generic solver class for solving clock and dice puzzles
 *
 * @author Quang Huynh (qth9368)
 */
public class Solver {
    private int uniqueConfigs;  // number of unique configurations
    private int totalConfigs;  // total number of configurations

    public Solver() {
        uniqueConfigs = 0;
        totalConfigs = 0;
    }

    /**
     * Solves puzzle starting with initial config using Breadth First Search
     *
     * @param initial starting config
     * @return list of configs from start to solution
     */
    public List<Configuration> solve(Configuration initial) {
        Map<Configuration, Configuration> predecessors = new HashMap<>();
        List<Configuration> queue = new LinkedList<>();
        queue.add(initial);
        predecessors.put(initial, null);
        totalConfigs++;
        uniqueConfigs++;
        while(!queue.isEmpty()) {
            Configuration current = queue.remove(0);
            if(current.isSolution()) {
                return constructPath(predecessors, initial, current);
            }
            for(Configuration nbr : current.getNeighbors()) {
                totalConfigs++;
                if(!predecessors.containsKey(nbr)) {
                    uniqueConfigs++;
                    predecessors.put(nbr, current);
                    queue.add(nbr);
                }
            }
        }
        return null;
    }

    /**
     * Constructs path the initial config to the solution.
     *
     * @param predecessors a map from each config to its predecessor
     * @param start initial config
     * @param end finishing config
     * @return the list of configurations from start to solution
     */
    private List<Configuration> constructPath(Map<Configuration, Configuration> predecessors, Configuration start, Configuration end) {
        LinkedList<Configuration> path = new LinkedList<>();
        Configuration current = end;
        while(current != start) {
            path.addFirst(current);
            current = predecessors.get(current);
        }
        path.addFirst(start);
        List<Configuration> configPath = new ArrayList<>(path);
        return configPath;
    }

    /**
     * Gets total configs
     *
     * @return number of total configs
     */
    public int getTotalConfigs() {
        return totalConfigs;
    }

    /**
     * Gets unique configs
     *
     * @return number of unique configs
     */
    public int getUniqueConfigs() {
        return uniqueConfigs;
    }
}
