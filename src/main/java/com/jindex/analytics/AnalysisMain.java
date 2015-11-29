package com.jindex.analytics;

import com.jindex.graph.DependenciesGraph;
import com.jindex.graph.DependenciesGraphLoader;

import java.io.File;
import java.io.IOException;

public class AnalysisMain {
    public static void main(String[] args) throws IOException {
        DependenciesGraphLoader loader = new DependenciesGraphLoader();
        DependenciesGraph dependenciesGraph = loader.loadGraph(new File("graphs/citations.txt.bk"));
        Analyzer analyzer = new Analyzer();
        ResultCollector resultCollector = new ResultCollector();
        analyzer.analyze(dependenciesGraph, resultCollector);

        Record record = analyzer.analyzeNode(dependenciesGraph, dependenciesGraph.getNode("com.cronutils:cron-utils"));
        System.out.println(record);
    }
}
