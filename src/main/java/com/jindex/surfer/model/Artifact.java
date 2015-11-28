package com.jindex.surfer.model;

import com.google.common.annotations.VisibleForTesting;

public class Artifact {
    private String groupid;
    private String artifactid;
    private String version;

    public Artifact(String groupid, String artifactid, String version) {
        this.groupid = groupid;
        this.artifactid = artifactid;
        this.version = cleanupVersion(version);
    }

    public String getGroupid() {
        return groupid;
    }

    public String getArtifactid() {
        return artifactid;
    }

    public String getVersion() {
        return version;
    }

    public String toString(){
        return String.format("%s:%s:%s", groupid, artifactid, version);
    }


    //due to projects specifying version as [v1, v2)
    @VisibleForTesting
    String cleanupVersion(String version){
        String s = version
                .replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .replaceAll("\\)", "")
                .replaceAll("\\(", "");
        String[]array = s.split(",");
        return array[array.length-1].trim();
    }
}
