package com.jindex.graph;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DependenciesGraphLoader {
    private static final Logger log = Logger.getLogger(DependenciesGraphLoader.class);

    public DependenciesGraph loadGraph(File citations) throws IOException {
        DependenciesGraph dependenciesGraph = new DependenciesGraph();
        Files.lines(Paths.get(citations.toURI())).forEach(line -> {
            String [] array = line.split(",");
            String origin = array[0];
            String cited = array[1];
            try {
                dependenciesGraph.addVertex(origin, cited);
            } catch (IOException e) {
                log.info(String.format("Failed to add vertex for '%s'", line), e);
            }
        });
        return dependenciesGraph;
    }
}
