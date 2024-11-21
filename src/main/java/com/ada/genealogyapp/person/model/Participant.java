package com.ada.genealogyapp.person.model;

import lombok.EqualsAndHashCode;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.UUID;

@Node
public interface Participant {
    UUID getId();

    String getName();
}