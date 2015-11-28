package com.jindex.graph;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.configuration.Config;
import org.neo4j.tooling.GlobalGraphOperations;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class DependenciesGraph {
    private static final String ID = "id";
    private GraphDatabaseService graphDb;
    private Index<Node> idIndex;

    public DependenciesGraph() throws IOException {
        File embedded = new File("neo4j");
        embedded.mkdirs();
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(embedded);
        Transaction tx = graphDb.beginTx();
        idIndex =  graphDb.index().forNodes("identifiers");
        tx.success();
    }

    public void addVertex(String origin, String cited) throws IOException {
        Node nodeA = getNodeOrCreate(origin);
        Node nodeB = getNodeOrCreate(cited);
        Transaction tx = graphDb.beginTx();
        setRelationship(nodeA, nodeB);
        tx.success();
    }

    public Node getNode(String id){
        return idIndex.get(ID, id).getSingle();
    }

    public Iterator<Node> listNodes(){
        return GlobalGraphOperations.at(graphDb).getAllNodes().iterator();
    }

    @VisibleForTesting
    void setRelationship(Node origin, Node cited){
        origin.createRelationshipTo(cited, RelTypes.DEPENDS_ON);
        cited.createRelationshipTo(origin, RelTypes.USED_BY);
    }

    @VisibleForTesting
    Node getNodeOrCreate(String id){
        Node node = getNode(id);
        if(node==null){
            Transaction tx = graphDb.beginTx();
            node = graphDb.createNode();
            node.setProperty(ID, id);
            idIndex.add(node, ID, node.getProperty(ID));
            tx.success();
        }
        return node;
    }
}
