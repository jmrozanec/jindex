package com.jindex.analytics;

public class Record {
    private String id;
    private int citations;
    private int hindex;
    private int gindex;
    private double yongIndex;

    public Record(String id, int citations, int hindex, int gindex, double yongIndex) {
        this.id = id;
        this.citations = citations;
        this.hindex = hindex;
        this.gindex = gindex;
        this.yongIndex = yongIndex;
    }

    public String getId() {
        return id;
    }

    public int getCitations() {
        return citations;
    }

    public int getHindex() {
        return hindex;
    }

    public int getGindex() {
        return gindex;
    }

    public double getYongIndex() {
        return yongIndex;
    }

    @Override
    public String toString(){
        return String.format("id:%s #c:%s h:%s g:%s y:%s", id, citations, hindex, gindex, yongIndex);
    }
}
