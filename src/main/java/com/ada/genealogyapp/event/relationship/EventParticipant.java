package com.ada.genealogyapp.event.relationship;

import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import com.ada.genealogyapp.person.model.Participant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Getter
@Setter
@RelationshipProperties
public class EventParticipant {

    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private Participant participant;

    private EventParticipantRelationshipType relationship;

    public EventParticipant(Participant participant, EventParticipantRelationshipType relationship) {
        this.participant = participant;
        this.relationship = relationship;
    }
}