package com.ada.genealogyapp.family.model;

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

import static java.util.Objects.nonNull;

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

    @Relationship(type = "HAS_FAMILY", direction = Relationship.Direction.INCOMING)
    private Tree tree;

    public boolean hasChild(Person child) {
        return children.contains(child);
    }

    public boolean hasMother(Person mother) {
        return nonNull(this.mother) && this.mother.equals(mother);
    }

    public boolean hasFather(Person father) {
        return nonNull(this.father) && this.father.equals(father);
    }

    public void addChild(Person child) {
        children.add(child);
    }

    public void addMother(Person mother) {
        this.mother = mother;
    }

    public void addFather(Person father) {
        this.father = father;
    }

    public void removeChild(Person child) {
        children.remove(child);
    }

    public void removeFather() {
        this.father = null;
    }

    public void removeMother() {
        this.mother = null;
    }
}