package com.ada.genealogyapp.person.relationship;

import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.type.PersonRelationshipType;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;


@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@RelationshipProperties
@Builder
public class PersonRelationship {

    @Id
    @GeneratedValue
    private String id;

    @TargetNode
    private Person child;

    private PersonRelationshipType relationship;

}

