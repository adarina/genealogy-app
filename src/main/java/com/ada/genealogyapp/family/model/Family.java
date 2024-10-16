package com.ada.genealogyapp.family.model;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@ToString
@Node
@Builder
@Getter
@Setter
@AllArgsConstructor
public class Family {

    @Id
    @GeneratedValue
    private UUID id;

    @Relationship(type = "HAS_FATHER", direction = Relationship.Direction.OUTGOING)
    private Person father;

    @Relationship(type = "HAS_MOTHER", direction = Relationship.Direction.OUTGOING)
    private Person mother;

    @Relationship(type = "HAS_CHILD", direction = Relationship.Direction.OUTGOING)
    private Set<Person> children = new HashSet<>();

    @Relationship(type = "HAS_FAMILY_EVENT", direction = Relationship.Direction.OUTGOING)
    private Set<Event> events = new HashSet<>();

    @Relationship(type = "HAS_FAMILY", direction = Relationship.Direction.INCOMING)
    private Tree familyTree;



}