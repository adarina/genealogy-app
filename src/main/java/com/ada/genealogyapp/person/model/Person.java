package com.ada.genealogyapp.person.model;

import com.ada.genealogyapp.person.Gender;
import com.ada.genealogyapp.person.relationship.ChildRelationship;
import com.ada.genealogyapp.person.relationship.ChildRelationshipType;
import com.ada.genealogyapp.person.relationship.FamilyRelationshipType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.transaction.annotation.Transactional;

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

    private Gender gender;

    private FamilyRelationshipType familyRelationshipType;

    @Relationship(type = "PARENT_OF", direction = Relationship.Direction.OUTGOING)
    private Set<ChildRelationship> children = new HashSet<>();

    @Relationship(type = "PARTNER_OF", direction = Relationship.Direction.OUTGOING)
    private Set<Person> partners = new HashSet<>();


    public Person(String firstname, String lastname, LocalDate birthDate, Gender gender) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthDate = birthDate;
        this.gender = gender;
    }


    public void addChild(Person child, ChildRelationshipType childRelationshipType) {
        this.children.add(new ChildRelationship(child, childRelationshipType));
    }

    public void addPartner(Person partner, FamilyRelationshipType familyRelationshipType) {
        this.partners.add(partner);
        partner.setFamilyRelationshipType(familyRelationshipType);
    }

    @Override
    public String toString() {
        return this.firstname + this.lastname + " (born " + this.birthDate + ")";
    }
}
