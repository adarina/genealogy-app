package com.ada.genealogyapp.event.model;

import com.ada.genealogyapp.event.relationship.EventCitation;
import com.ada.genealogyapp.event.relationship.EventParticipant;
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

    private EventType type;

    private LocalDate date;

    private String place;

    private String description;


    @Relationship(type = "HAS_PARTICIPANT", direction = Relationship.Direction.OUTGOING)
    private Set<EventParticipant> participants = new HashSet<>();

    @Relationship(type = "HAS_EVENT_CITATION", direction = Relationship.Direction.OUTGOING)
    private Set<EventCitation> citations = new HashSet<>();

    @Relationship(type = "HAS_EVENT", direction = Relationship.Direction.INCOMING)
    private Tree tree;

    public Event(EventType event, LocalDate date, String place, String description, Tree tree) {
        this.type = event;
        this.date = date;
        this.place = place;
        this.description = description;
        this.tree = tree;
    }
}
