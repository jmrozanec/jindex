package com.jindex;

import com.jindex.graph.DependenciesGraph;
import com.jindex.graph.DependenciesGraphLoader;

import java.io.File;
import java.io.IOException;

public class AnalysisMain {
    public static void main(String[] args) throws IOException {
        DependenciesGraphLoader loader = new DependenciesGraphLoader();
        DependenciesGraph dependenciesGraph = loader.loadGraph(new File("graphs/citations.txt.bk"));
        Analyzer analyzer = new Analyzer();
        analyzer.analyze(dependenciesGraph);
    }
}
