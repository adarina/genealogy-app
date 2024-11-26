package com.ada.genealogyapp.event.relationship;


import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import com.ada.genealogyapp.participant.model.Participant;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@RelationshipProperties
@Builder
public class EventParticipant {

    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private Participant participant;


    private EventParticipantRelationshipType relationship;


}
