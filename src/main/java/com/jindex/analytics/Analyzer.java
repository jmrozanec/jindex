package com.jindex.analytics;

import com.google.common.collect.Lists;
import com.jindex.graph.DependenciesGraph;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.neo4j.graphdb.Node;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Analyzer {
    private static final String ID = "id";

    public void analyze(DependenciesGraph dependenciesGraph, ResultCollector resultCollector){
        Iterator<Node> nodes = dependenciesGraph.listNodes();
        SummaryStatistics citations = new SummaryStatistics();
        SummaryStatistics hindex = new SummaryStatistics();
        SummaryStatistics gindex = new SummaryStatistics();
        SummaryStatistics yongIndex = new SummaryStatistics();
        while(nodes.hasNext()){
            Record record = analyzeNode(dependenciesGraph, nodes.next());

            citations.addValue(record.getCitations());
            hindex.addValue(record.getHindex());
            gindex.addValue(record.getGindex());
            yongIndex.addValue(record.getYongIndex());

            resultCollector.report(record);
        }
        System.out.println("citations: "+citations.toString());
        System.out.println("hindex: "+hindex.toString());
        System.out.println("gindex: "+gindex.toString());
        System.out.println("yongIndex: "+yongIndex.toString());
    }

    public Record analyzeNode(DependenciesGraph dependenciesGraph, Node me){
        List<Tuple> citations = Lists.newArrayList();
        dependenciesGraph
                .getUsages(me)
                .forEach(
                        citation ->
                                citations.add(
                                        new Tuple(citation.getProperty(ID).toString(), numberOfCitations(dependenciesGraph, citation))
                                )
                );
        System.out.println(me.getProperty(ID).toString());
        return new Record(me.getProperty(ID).toString(), numberOfCitations(dependenciesGraph, me), hindex(citations), gindex(citations), yongIndex(citations));
    }

    int hindex(List<Tuple> tuples){
        Collections.sort(tuples, Comparator.reverseOrder());
        int count = 1;
        int lastcount = 0;
        Iterator<Tuple> iterator = tuples.iterator();
        while (iterator.hasNext() && (lastcount=iterator.next().count)>count){
            count++;
        }
        return lastcount;
    }

    int gindex(List<Tuple> tuples){
        Collections.sort(tuples, Comparator.reverseOrder());
        int count = 0;
        int g = 0;
        for(Tuple tuple : tuples){
            g++;
            count += tuple.count;
            if(Math.pow(g+1, 2)>count){
                break;
            }
        }
        return g;
    }

    double yongIndex(List<Tuple> tuples){
        return 0.54*Math.sqrt(tuples.size());
    }

    //http://introcs.cs.princeton.edu/java/16pagerank/
    void pagerank(){

    }

    int numberOfCitations(DependenciesGraph dependenciesGraph, Node node){
        return dependenciesGraph.getUsages(node).size();
    }

    public static class Tuple implements Comparable<Tuple> {
        private String id;
        private int count;

        public Tuple(String id, int count) {
            this.id = id;
            this.count = count;
        }

        @Override
        public int compareTo(Tuple o) {
            return this.count-o.count;
        }
    }
}
