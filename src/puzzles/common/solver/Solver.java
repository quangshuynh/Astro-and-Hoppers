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
        Map<Configuration,Configuration> predecessors = new HashMap<>();
        Queue<Configuration> queue = new LinkedList<>();
        queue.add(initial);
        predecessors.put(initial, null);
        Configuration current = null;
        totalConfigs++;
        uniqueConfigs++;
        while(!queue.isEmpty()){
            current = queue.remove();
            if(current.isSolution()){
                break;
            }
            for(Configuration nbr : current.getNeighbors()){
                totalConfigs++;
                if(!predecessors.containsKey(nbr)){
                    predecessors.put(nbr, current);
                    queue.add(nbr);
                    uniqueConfigs++;
                }
            }
        }
        List<Configuration> path = new LinkedList<>();
        while(current != null){
            path.addFirst(current);
            current = predecessors.get(current);
        }
        return path;
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
