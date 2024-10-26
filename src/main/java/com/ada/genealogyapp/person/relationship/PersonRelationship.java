package com.ada.genealogyapp.person.relationship;

import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.type.PersonRelationshipType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.*;


@NoArgsConstructor
@ToString
@Getter
@Setter
@RelationshipProperties
public class PersonRelationship {

    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private Person child;

    private PersonRelationshipType personRelationshipType;

}

