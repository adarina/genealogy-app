package com.ada.genealogyapp.family.model;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.person.model.Participant;
import com.ada.genealogyapp.family.type.StatusType;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.*;

@Node
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Family implements Participant {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    private String name;

    private StatusType status;

    @Relationship(type = "HAS_FATHER", direction = Relationship.Direction.OUTGOING)
    private Person father;

    @Relationship(type = "HAS_MOTHER", direction = Relationship.Direction.OUTGOING)
    private Person mother;

    @Relationship(type = "HAS_CHILD", direction = Relationship.Direction.OUTGOING)
    private List<Person> children = new ArrayList<>();

    @Relationship(type = "HAS_PARTICIPANT", direction = Relationship.Direction.INCOMING)
    private Set<Event> events;

    @Relationship(type = "HAS_FAMILY", direction = Relationship.Direction.INCOMING)
    private Tree tree;

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }
}