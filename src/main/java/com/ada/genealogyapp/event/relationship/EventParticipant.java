package com.ada.genealogyapp.event.relationship;

import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import com.ada.genealogyapp.person.model.Participant;
import lombok.*;

import org.springframework.data.neo4j.core.schema.*;


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