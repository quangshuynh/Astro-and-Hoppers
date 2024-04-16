package puzzles.astro.model;

import puzzles.common.solver.Configuration;

import java.io.IOException;
import java.util.Collection;

// TODO: implement your HoppersConfig for the common solver

public class AstroConfig implements Configuration{
    public AstroConfig(String filename) throws IOException {
    }

    @Override
    public boolean isSolution() {
        return false;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        return null;
    }

    @Override
    public boolean equals(Object other) {
        return false;
    }

    @Override
    public int hashCode() { return 0; }

    @Override
    public String toString() {
        return null;
    }
}
