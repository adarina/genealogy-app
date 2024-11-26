package com.ada.genealogyapp.participant.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.UUID;

@Node
@Getter
@Setter
public abstract class Participant {

    @Id
    @GeneratedValue
    private UUID id;

}