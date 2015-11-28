package com.jindex;
import com.jindex.model.Artifact;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CitationProcessor {
    private PrintWriter printWriter;

    public CitationProcessor() throws FileNotFoundException {
        printWriter = new PrintWriter("citations.txt");
    }

    public void process(Artifact origin, Artifact artifact){
        try{
            printWriter.println(String.format("%s,%s,1", origin.toString(), artifact.toString()));
            printWriter.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void dump(){}
}


