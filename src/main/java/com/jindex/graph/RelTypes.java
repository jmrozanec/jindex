package com.jindex.graph;

import org.neo4j.graphdb.RelationshipType;

public enum RelTypes implements RelationshipType {
    DEPENDS_ON, USED_BY;
}
