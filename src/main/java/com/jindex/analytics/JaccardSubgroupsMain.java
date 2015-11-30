package com.jindex.analytics;

import org.simmetrics.metrics.Jaccard;
import org.simmetrics.metrics.StringMetrics;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

public class JaccardSubgroupsMain {

    public static void main(String[] args) throws IOException {
        Set<String> citationcount = readFile(new File("top100", "citationcount.txt"));
        Set<String> hindex = readFile(new File("top100", "hindex.txt"));
        Set<String> gindex = readFile(new File("top100", "gindex.txt"));
        Set<String> yong = readFile(new File("top100", "yong.txt"));
        Jaccard<String> jaccard = new Jaccard<String>();
        System.out.println(jaccard.compare(citationcount, hindex));
        System.out.println(jaccard.compare(citationcount, gindex));
        System.out.println(jaccard.compare(citationcount, yong));
        System.out.println(jaccard.compare(hindex, gindex));
        System.out.println(jaccard.compare(hindex, yong));
        System.out.println(jaccard.compare(gindex, yong));
    }

    static Set<String> readFile(File file) throws IOException {
        return Files.lines(Paths.get(file.toURI())).collect(Collectors.toSet());
    }
}
