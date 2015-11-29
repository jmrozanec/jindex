package com.jindex.analytics;

import com.google.common.collect.Lists;
import com.jindex.graph.DependenciesGraph;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class AnalyzerTest {
    private Analyzer analyzer;

    @Before
    public void setUp() throws Exception {
        this.analyzer = new Analyzer();
    }

    @Test//example taken from https://en.wikipedia.org/wiki/H-index
    public void testHindex() throws Exception {
        List<Analyzer.Tuple> tuples = Lists.newLinkedList();
        tuples.add(new Analyzer.Tuple("A", 10));
        tuples.add(new Analyzer.Tuple("B", 8));
        tuples.add(new Analyzer.Tuple("C", 5));
        tuples.add(new Analyzer.Tuple("D", 4));
        tuples.add(new Analyzer.Tuple("E", 3));

        assertEquals(4, analyzer.hindex(tuples));
    }

    @Test//example taken from
    public void testGindex() throws Exception {
        List<Analyzer.Tuple> tuples = Lists.newLinkedList();
        tuples.add(new Analyzer.Tuple("A", 102));
        tuples.add(new Analyzer.Tuple("B", 101));
        tuples.add(new Analyzer.Tuple("C", 100));
        tuples.add(new Analyzer.Tuple("D", 10));
        tuples.add(new Analyzer.Tuple("E", 9));
        tuples.add(new Analyzer.Tuple("F", 8));
        tuples.add(new Analyzer.Tuple("G", 7));
        tuples.add(new Analyzer.Tuple("H", 6));
        tuples.add(new Analyzer.Tuple("I", 5));
        tuples.add(new Analyzer.Tuple("J", 4));

        assertEquals(10, analyzer.gindex(tuples));
    }

    @Test
    public void testYongIndex() throws Exception {
        List<Analyzer.Tuple> tuples = Lists.newLinkedList();
        tuples.add(new Analyzer.Tuple("A", 10));
        tuples.add(new Analyzer.Tuple("B", 8));
        tuples.add(new Analyzer.Tuple("C", 5));
        tuples.add(new Analyzer.Tuple("D", 4));
        tuples.add(new Analyzer.Tuple("E", 3));

        assertEquals(Math.sqrt(tuples.size()), analyzer.yongIndex(tuples), 0);
    }

    @Test
    public void testAnalyzeNode() throws Exception {
        DependenciesGraph dependenciesGraph = new DependenciesGraph();
        dependenciesGraph.addVertex("A", "B");
        dependenciesGraph.addVertex("A", "C");
        dependenciesGraph.addVertex("A", "D");
        dependenciesGraph.addVertex("A", "E");
        dependenciesGraph.addVertex("B", "C");
        dependenciesGraph.addVertex("B", "D");
        dependenciesGraph.addVertex("C", "X");
        dependenciesGraph.addVertex("C", "Y");
        dependenciesGraph.addVertex("C", "Z");
        dependenciesGraph.addVertex("D", "B");
        dependenciesGraph.addVertex("D", "K");
        dependenciesGraph.addVertex("D", "L");
        dependenciesGraph.addVertex("E", "M");

        Record record = analyzer.analyzeNode(dependenciesGraph, dependenciesGraph.getNode("B"));
        System.out.println(record);
    }
}