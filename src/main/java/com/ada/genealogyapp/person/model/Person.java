package com.ada.genealogyapp.person.model;

import com.ada.genealogyapp.participant.model.Participant;
import com.ada.genealogyapp.person.relationship.PersonRelationship;
import com.ada.genealogyapp.person.type.GenderType;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.*;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;

import java.util.HashSet;
import java.util.Set;

@Node
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person extends Participant {

    private String name;

    private String firstname;

    private String lastname;

    private LocalDate birthdate;

    private GenderType gender;

    @Relationship(type = "PARENT_OF", direction = Relationship.Direction.OUTGOING)
    private Set<PersonRelationship> relationships = new HashSet<>();

    @Relationship(type = "HAS_PERSON", direction = Relationship.Direction.INCOMING)
    private Tree tree;

    public Person(String name, String firstname, String lastname, LocalDate birthDate, GenderType genderType, Tree tree) {
        this.name = name;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthDate;
        this.gender = genderType;
        this.tree = tree;
    }
}
