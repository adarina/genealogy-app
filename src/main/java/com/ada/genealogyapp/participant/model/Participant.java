package com.ada.genealogyapp.participant.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Node
@Getter
@Setter
@EqualsAndHashCode
public abstract class Participant {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

}