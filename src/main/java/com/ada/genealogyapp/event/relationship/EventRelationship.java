package com.ada.genealogyapp.event.relationship;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@NoArgsConstructor
@ToString
@Getter
@Setter
@RelationshipProperties
public class EventRelationship {

    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private Event event;

    private EventParticipantRelationshipType relationship;

}
