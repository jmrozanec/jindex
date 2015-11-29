package com.jindex.analytics;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class ResultCollector {
    private PrintWriter printWriter;

    public ResultCollector() throws FileNotFoundException {
        printWriter = new PrintWriter("analysis.csv");
    }

    public void report(Record record) {
        printWriter.println(
                String.format("%s,%s,%s,%s,%s",
                        record.getId(), record.getCitations(), record.getHindex(),
                        record.getGindex(), record.getYongIndex()
                )
        );
        printWriter.flush();
    }


}
