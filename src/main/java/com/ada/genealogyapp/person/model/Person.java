package com.ada.genealogyapp.person.model;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.event.relationship.EventRelationship;
import com.ada.genealogyapp.person.relationship.PersonRelationship;
import com.ada.genealogyapp.family.relationship.FamilyRelationship;
import com.ada.genealogyapp.person.type.GenderType;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Node
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Person implements Participant {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    private String name;

    private String firstname;

    private String lastname;

    private LocalDate birthdate;

    private GenderType gender;

    @Relationship(type = "PARENT_OF", direction = Relationship.Direction.OUTGOING)
    private Set<PersonRelationship> children = new HashSet<>();

    @Relationship(type = "CHILD_OF", direction = Relationship.Direction.OUTGOING)
    private Set<PersonRelationship> parents = new HashSet<>();

    @Relationship(type = "HAS_EVENT", direction = Relationship.Direction.OUTGOING)
    private Set<EventRelationship> events = new HashSet<>();

    @Relationship(type = "HAS_FAMILY", direction = Relationship.Direction.OUTGOING)
    private Set<FamilyRelationship> families = new HashSet<>();

    @Relationship(type = "HAS_CITATION", direction = Relationship.Direction.OUTGOING)
    private Set<Citation> citations = new HashSet<>();

    @Relationship(type = "BELONGS_TO_TREE", direction = Relationship.Direction.OUTGOING)
    private Tree tree;

    @Override
    public UUID getParticipantId() {
        return this.id;
    }

    @Override
    public String getParticipantName() {
        return this.name;
    }


    public Person(String name, String firstname, String lastname, LocalDate birthDate, GenderType genderType, Tree tree) {
        this.name = name;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthDate;
        this.gender = genderType;
        this.tree = tree;
    }

    public Person(String firstname, String lastname, LocalDate birthDate, GenderType genderType, Tree tree) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthDate;
        this.gender = genderType;
        this.tree = tree;
    }
}
