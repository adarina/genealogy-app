package com.ada.genealogyapp.event.model;

import com.ada.genealogyapp.event.relationship.EventCitation;
import com.ada.genealogyapp.event.relationship.EventParticipant;
import com.ada.genealogyapp.event.type.EventType;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Node
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

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

    public boolean isParticipantAlreadyInEvent(String participantId) {
        return participants.stream()
                .anyMatch(ep -> ep.getParticipant().getId().equals(participantId));
    }

    public boolean isCitationAlreadyInEvent(String citationId) {
        return citations.stream()
                .anyMatch(ec -> ec.getCitation().getId().equals(citationId));
    }

}
