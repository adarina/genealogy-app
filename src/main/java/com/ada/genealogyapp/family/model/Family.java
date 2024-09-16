package com.ada.genealogyapp.family.model;

import com.ada.genealogyapp.person.model.Person;
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

    @Relationship(type = "HAS_MEMBER", direction = Relationship.Direction.OUTGOING)
    private Set<Person> members = new HashSet<>();

    public void addMember(Person person) {
        members.add(person);
    }
}