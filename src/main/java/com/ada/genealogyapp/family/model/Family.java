package com.ada.genealogyapp.family.model;

import com.ada.genealogyapp.participant.model.Participant;
import com.ada.genealogyapp.family.type.StatusType;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.*;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.*;

@Node
@Getter
@Setter
@Builder
@NoArgsConstructor
public class Family extends Participant {

    private String name;

    private StatusType status;

    @Relationship(type = "HAS_FATHER", direction = Relationship.Direction.OUTGOING)
    private Person father;

    @Relationship(type = "HAS_MOTHER", direction = Relationship.Direction.OUTGOING)
    private Person mother;

    @Relationship(type = "HAS_CHILD", direction = Relationship.Direction.OUTGOING)
    private List<Person> children = new ArrayList<>();

    @Relationship(type = "HAS_FAMILY", direction = Relationship.Direction.INCOMING)
    private Tree tree;


    public Family(String name, StatusType status, Person father, Person mother, List<Person> children, Tree tree) {
        this.name = name;
        this.status = status;
        this.father = father;
        this.mother = mother;
        this.children = children;
        this.tree = tree;
    }
}