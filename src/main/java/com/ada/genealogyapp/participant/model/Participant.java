package com.ada.genealogyapp.participant.model;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.UUID;

@Node
@Getter
@Setter
@EqualsAndHashCode
public abstract class Participant {

    @Id
    private String id;

    public Participant() {
        this.id = UUID.randomUUID().toString();
    }
}
