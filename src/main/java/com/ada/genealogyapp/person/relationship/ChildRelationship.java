package com.ada.genealogyapp.person.relationship;

import com.ada.genealogyapp.person.model.Person;
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

    public ChildRelationship(Person child, ChildRelationshipType childRelationshipType) {
        this.child = child;
        this.childRelationshipType = childRelationshipType;
    }
}

