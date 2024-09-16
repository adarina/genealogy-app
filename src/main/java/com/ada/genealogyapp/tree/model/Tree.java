package com.ada.genealogyapp.tree.model;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.person.model.Person;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Node
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tree {

    @Id
    @GeneratedValue
    private UUID id;

    private Long userId;

    private String name;

    @Relationship(type = "HAS_PERSON", direction = Relationship.Direction.OUTGOING)
    private Set<Person> persons = new HashSet<>();

    @Relationship(type = "HAS_FAMILY", direction = Relationship.Direction.OUTGOING)
    private Set<Family> families = new HashSet<>();

    @Relationship(type = "HAS_EVENT", direction = Relationship.Direction.OUTGOING)
    private Set<Event> events = new HashSet<>();

    public Tree(UUID id, Long userId, String name) {
        this.id = id;
        this.userId = userId;
        this.name = name;
    }
}
