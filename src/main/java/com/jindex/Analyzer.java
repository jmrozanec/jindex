package com.jindex;

import com.google.common.collect.Lists;
import com.jindex.graph.DependenciesGraph;
import com.jindex.graph.RelTypes;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterable;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Analyzer {
    private static final String ID = "id";

    public void analyze(DependenciesGraph dependenciesGraph){
        Iterator<Node> nodes = dependenciesGraph.listNodes();
        while(nodes.hasNext()){
            analyzeNode(nodes.next());
        }
    }

    public void analyzeNode(Node me){
        Iterable<Relationship> whoCitesMe = me.getRelationships(RelTypes.USED_BY);
        List<Tuple> citations = Lists.newArrayList();
        whoCitesMe.forEach(citation -> {
            Node othernode = citation.getOtherNode(me);
            citations.add(new Tuple(othernode.getProperty(ID).toString(), numberOfCitations(othernode)));
        });
        Collections.sort(citations, Comparator.reverseOrder());
        System.out.println(String.format("%s: #citations=%s h=%s g=%s y=%s", me.getProperty(ID), numberOfCitations(me), hindex(citations), gindex(citations), yongIndex(citations)));
    }

    int hindex(List<Tuple> tuples){
        int count = 1;
        Iterator<Tuple> iterator = tuples.iterator();
        while (iterator.hasNext() && iterator.next().count>count){
            count++;
        }
        return count;
    }

    int gindex(List<Tuple> tuples){
        int count = 0;
        int g = 0;
        for(Tuple tuple : tuples){
            count += tuple.count;
            if(Math.pow(g+1, 2)>count){
                break;
            }
            g++;
        }
        return g;
    }

    double yongIndex(List<Tuple> tuples){
        return Math.sqrt(tuples.size());
    }

    //http://introcs.cs.princeton.edu/java/16pagerank/
    void pagerank(){

    }

    int numberOfCitations(Node node){
        AtomicInteger count = new AtomicInteger(0);
        node.getRelationships(RelTypes.USED_BY).forEach(citation -> count.incrementAndGet());
        return count.get();
    }

    private static class Tuple implements Comparable<Tuple> {
        private String id;
        private int count;

        private Tuple(String id, int count) {
            this.id = id;
            this.count = count;
        }

        @Override
        public int compareTo(Tuple o) {
            return this.count-o.count;
        }
    }
}
