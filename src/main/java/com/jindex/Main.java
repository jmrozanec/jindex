package com.jindex;

import com.google.common.collect.Queues;
import com.jindex.model.Artifact;
import org.apache.log4j.Logger;

import java.util.Queue;

public class Main {
    private static final Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        double maxdependencies = 7000000;
        ArtifactUtil artifactUtil = new ArtifactUtil();
        Queue<Artifact> projectsqueue = Queues.newConcurrentLinkedQueue();

        CitationProcessor citationProcessor = new CitationProcessor();
        DependenciesGraphWalker dependenciesGraphWalker = new DependenciesGraphWalker(projectsqueue, citationProcessor);
        int projectscount = 0;
        do{
            try{
                if(projectsqueue.isEmpty()){
                    projectsqueue.add(artifactUtil.random());
                }
                projectscount = dependenciesGraphWalker.process(projectsqueue.poll());
                if(projectscount%1000==0){
                    log.info(String.format("projects so far: %s", projectscount));
                }
            }catch (Exception e){
                e.printStackTrace();
                log.info(String.format("projects so far: %s", projectscount));
            }
        }while(projectscount<maxdependencies);

        log.info(String.format("Evaluated %s projects", projectscount));
        citationProcessor.dump();
        System.exit(0);
    }
}
