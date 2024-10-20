package com.ada.genealogyapp.family.model;

import com.ada.genealogyapp.person.model.Participant;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.event.relationship.EventRelationship;
import com.ada.genealogyapp.event.type.EventRelationshipType;
import com.ada.genealogyapp.family.type.FamilyRelationshipType;
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
public class Family implements Participant {

    @Id
    @GeneratedValue
    private UUID id;

    private FamilyRelationshipType familyRelationshipType;

    private EventRelationshipType eventRelationshipType;

    @Relationship(type = "HAS_FATHER", direction = Relationship.Direction.OUTGOING)
    private Person father;

    @Relationship(type = "HAS_MOTHER", direction = Relationship.Direction.OUTGOING)
    private Person mother;

    @Relationship(type = "HAS_CHILD", direction = Relationship.Direction.OUTGOING)
    private Set<Person> children = new HashSet<>();

    @Relationship(type = "HAS_EVENT", direction = Relationship.Direction.OUTGOING)
    private Set<EventRelationship> events = new HashSet<>();

    @Relationship(type = "HAS_CITATION", direction = Relationship.Direction.OUTGOING)
    private Set<Citation> citations = new HashSet<>();

    @Relationship(type = "HAS_FAMILY", direction = Relationship.Direction.INCOMING)
    private Tree familyTree;

    @Override
    public UUID getParticipantId() {
        return this.id;
    }

}