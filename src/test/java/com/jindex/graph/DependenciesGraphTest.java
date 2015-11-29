package com.jindex.graph;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Node;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class DependenciesGraphTest {
    private DependenciesGraph dependenciesGraph;

    @Before
    public void setUp() throws Exception {
        this.dependenciesGraph = new DependenciesGraph();
    }


    @Test
    public void testAddVertex() throws Exception {
        dependenciesGraph.addVertex("origin", "cited-1");
        dependenciesGraph.addVertex("origin", "cited-2");

        dependenciesGraph.getNode("origin").getRelationships(RelTypes.DEPENDS_ON);
        dependenciesGraph.getNode("origin").getRelationships(RelTypes.USED_BY);
    }

    @Test
    public void testGetDependencies() throws IOException {
        dependenciesGraph.addVertex("origin", "cited-1");
        dependenciesGraph.addVertex("origin", "cited-2");
        dependenciesGraph.addVertex("cited-1", "cited-2");

        List<Node> dependencies = dependenciesGraph.getDependencies(dependenciesGraph.getNode("origin"));

        assertEquals(2, dependencies.size());
        assertTrue(Sets.newHashSet("cited-1", "cited-2").contains(dependencies.get(0).getProperty("id").toString()));
        assertTrue(Sets.newHashSet("cited-1", "cited-2").contains(dependencies.get(1).getProperty("id").toString()));

        dependencies = dependenciesGraph.getDependencies(dependenciesGraph.getNode("cited-1"));
        assertEquals(1, dependencies.size());
        assertEquals("cited-2", dependencies.get(0).getProperty("id").toString());
    }

    @Test
    public void testGetUsages() throws IOException {
        dependenciesGraph.addVertex("origin", "cited-1");
        dependenciesGraph.addVertex("origin", "cited-2");
        dependenciesGraph.addVertex("cited-1", "cited-2");

        assertEquals(0, dependenciesGraph.getUsages(dependenciesGraph.getNode("origin")).size());

        List<Node> usages = dependenciesGraph.getUsages(dependenciesGraph.getNode("cited-1"));
        assertEquals(1, usages.size());
        assertEquals("origin", usages.get(0).getProperty("id").toString());
    }
}