package com.ada.genealogyapp.person.model;

import com.ada.genealogyapp.event.relationship.EventRelationship;
import com.ada.genealogyapp.family.relationship.ChildRelationship;
import com.ada.genealogyapp.family.relationship.FamilyRelationship;
import com.ada.genealogyapp.person.type.GenderType;
import com.ada.genealogyapp.family.type.ChildRelationshipType;
import com.ada.genealogyapp.family.type.FamilyRelationshipType;
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
public class Person {

    @Id
    @GeneratedValue
    private UUID id;

    private String firstname;

    private String lastname;

    private LocalDate birthDate;

    private GenderType genderType;

    private FamilyRelationshipType familyRelationshipType;

    private ChildRelationshipType childRelationshipType;


    @Relationship(type = "PARENT_OF", direction = Relationship.Direction.OUTGOING)
    private Set<ChildRelationship> children = new HashSet<>();

    @Relationship(type = "PARTNER_OF", direction = Relationship.Direction.OUTGOING)
    private Set<FamilyRelationship> partners = new HashSet<>();

    @Relationship(type = "HAS_PERSONAL_EVENT", direction = Relationship.Direction.OUTGOING)
    private Set<EventRelationship> events = new HashSet<>();

    @Relationship(type = "HAS_PERSON", direction = Relationship.Direction.INCOMING)
    private Tree tree;

    public Person(String firstname, String lastname, LocalDate birthDate, GenderType genderType, Tree tree) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthDate = birthDate;
        this.genderType = genderType;
        this.tree = tree;
    }
}
