package puzzles.dice;

import java.io.*;
import java.util.*;

/**
 * Die class that stores filename, faces and neighbors
 *
 * @author Quang Huynh
 */
public class Die {
    private String fileName;  // dice file name
    private int numFaces; // number of faces a die has
    private LinkedHashMap<Character, List<Character>> neighbors; // face and its neighbors

    /**
     * Read in die information
     *
     * @param fileName number of die file
     * @throws IOException thrown if there is a problem opening or reading the file
     */
    public Die(String fileName) throws IOException {
        this.fileName = "die-" + fileName + ".txt";  // file name
        this.neighbors = new LinkedHashMap<>();
        try(BufferedReader in = new BufferedReader(new FileReader(this.fileName))) {
            String line = in.readLine();
            this.numFaces = Integer.parseInt(line);
            while((line = in.readLine()) != null) {  // when line is not empty
                String[] parts = line.split(" ");
                if(parts.length > 1) {
                    char face = parts[0].charAt(0);
                    List<Character> faceNeighbors = new ArrayList<>();
                    for(int row = 1; row < parts.length; row++) {
                        faceNeighbors.add(parts[row].charAt(0));
                    }
                    this.neighbors.put(face, faceNeighbors);
                }
            }
        }
    }

    /**
     * Get name of dice file
     *
     * @return string representation of dice file
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Get number of faces a die file has
     *
     * @return number of faces
     */
    public int getNumFaces() {
        return numFaces;
    }

    /**
     * Get neighbors of die face
     *
     * @param face the die face
     * @return list of neighbors of die face
     */
    public List<Character> getNeighbors(char face) {
        return this.neighbors.get(face);
    }

    /**
     * Face and neighbors of die
     *
     * @return LinkedHashMap of face and neighbors
     */
    public LinkedHashMap<Character, List<Character>> getFacesAndNeighbors() {
        return this.neighbors;
    }
}