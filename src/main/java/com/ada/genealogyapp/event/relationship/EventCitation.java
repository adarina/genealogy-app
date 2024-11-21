package com.ada.genealogyapp.event.relationship;

import com.ada.genealogyapp.citation.model.Citation;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RelationshipProperties
public class EventCitation {

    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private Citation citation;

}