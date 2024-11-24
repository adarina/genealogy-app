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

@Node
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Event {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
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

    public boolean isParticipantAlreadyInEvent(UUID participantId) {
        return participants.stream()
                .anyMatch(ep -> ep.getParticipant().getId().equals(participantId));
    }

    public boolean isCitationAlreadyInEvent(UUID citationId) {
        return citations.stream()
                .anyMatch(ec -> ec.getCitation().getId().equals(citationId));
    }

}
