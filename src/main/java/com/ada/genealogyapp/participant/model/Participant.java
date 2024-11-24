package com.ada.genealogyapp.participant.model;

import org.springframework.data.neo4j.core.schema.Node;

import java.util.UUID;

@Node
public interface Participant {
    UUID getId();

    String getName();
}