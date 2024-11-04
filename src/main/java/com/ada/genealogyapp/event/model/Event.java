package com.ada.genealogyapp.event.model;

import com.ada.genealogyapp.person.model.Participant;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.event.type.EventType;
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

    private EventType eventType;

    private LocalDate date;

    private String place;

    private String description;

    @Relationship(type = "HAS_CITATION", direction = Relationship.Direction.OUTGOING)
    private Set<Citation> citations = new HashSet<>();

    @Relationship(type = "HAS_PARTICIPANT", direction = Relationship.Direction.OUTGOING)
    private Set<Participant> participants = new HashSet<>();

    @Relationship(type = "BELONGS_TO_TREE", direction = Relationship.Direction.OUTGOING)
    private Tree tree;

    public Event(EventType eventType, LocalDate date, String place, String description, Tree tree) {
        this.eventType = eventType;
        this.date = date;
        this.place = place;
        this.description = description;
        this.tree = tree;
    }
}
