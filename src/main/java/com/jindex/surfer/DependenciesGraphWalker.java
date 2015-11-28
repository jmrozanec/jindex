package com.jindex.surfer;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jindex.surfer.connector.Booter;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.resolution.ArtifactDescriptorRequest;
import org.eclipse.aether.resolution.ArtifactDescriptorResult;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class DependenciesGraphWalker {
    private RepositorySystem system;
    private RepositorySystemSession session;
    private Set<String> dependencies;
    private Queue<com.jindex.surfer.model.Artifact> projectsqueue;
    private CitationProcessor citationProcessor;
    private PrintWriter printWriter;
    private DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");

    public DependenciesGraphWalker(Queue<com.jindex.surfer.model.Artifact> projectsqueue, CitationProcessor citationProcessor) throws FileNotFoundException {
        system = Booter.newRepositorySystem();
        session = Booter.newRepositorySystemSession(system);
        dependencies = Sets.newHashSet();
        this.projectsqueue = projectsqueue;
        this.citationProcessor = citationProcessor;
        printWriter = new PrintWriter("projects.txt");
    }

    public int process(com.jindex.surfer.model.Artifact project) throws Exception {
        String artifactstring = id(project);
        if(!dependencies.contains(artifactstring)){
            dependencies.add(artifactstring);
            printWriter.println(String.format("%s,%s", fmt.print(DateTime.now()), project.toString()));
            printWriter.flush();
            dependencies(ArtifactUtil.instance().latest(project).toString())
                    .stream()
                    .forEach(dependency -> {
                        Artifact dep = dependency.getArtifact();
                        com.jindex.surfer.model.Artifact a = new com.jindex.surfer.model.Artifact(dep.getGroupId(), dep.getArtifactId(), dep.getVersion());
                        if(!dependencies.contains(id(a))){
                            projectsqueue.add(a);
                        }
                        citationProcessor.process(project, a);
                    });
        }
        return dependencies.size();
    }

    List<Dependency> dependencies(String artifactstring){
        Artifact artifact = new DefaultArtifact(artifactstring);
        ArtifactDescriptorRequest descriptorRequest = new ArtifactDescriptorRequest();
        descriptorRequest.setArtifact(artifact);
        descriptorRequest.setRepositories(Booter.newRepositories(system,session));
        try {
            ArtifactDescriptorResult descriptorResult = system.readArtifactDescriptor(session, descriptorRequest);
            return descriptorResult.getDependencies();
        }catch (Exception e){
            System.out.println(String.format("Failed to process artifact %s", artifactstring));
            e.printStackTrace();
        }
        return Lists.newArrayList();
    }

    String id(com.jindex.surfer.model.Artifact artifact){
        return String.format("%s:%s", artifact.getGroupid(), artifact.getArtifactid());
    }
}
