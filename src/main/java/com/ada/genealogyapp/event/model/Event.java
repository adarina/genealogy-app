package com.ada.genealogyapp.event.model;

import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@ToString
@Node
@Builder
@Getter
@Setter
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue
    private UUID id;

    public EventType eventType;

    private LocalDate date;

    private String place;

    private String description;


    @Relationship(type = "HAS_SOURCE", direction = Relationship.Direction.OUTGOING)
    private Set<Source> sources = new HashSet<>();

    @Relationship(type = "HAS_EVENT", direction = Relationship.Direction.INCOMING)
    private Tree tree;



    public Event(EventType eventType, LocalDate date, String place, String description, Tree tree) {
        this.eventType = eventType;
        this.date = date;
        this.place = place;
        this.description = description;
        this.tree = tree;
    }
}
