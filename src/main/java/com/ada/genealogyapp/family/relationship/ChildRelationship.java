package com.ada.genealogyapp.family.relationship;

import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.family.type.ChildRelationshipType;
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
public class ChildRelationship {

    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private Person child;

    private ChildRelationshipType childRelationshipType;

}

