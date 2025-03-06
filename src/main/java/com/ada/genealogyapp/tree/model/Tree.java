package com.ada.genealogyapp.tree.model;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.graphuser.model.GraphUser;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.source.model.Source;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;


import java.util.HashSet;
import java.util.Set;

@Node
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tree {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    private String name;

    @Relationship(type = "HAS_FAMILY", direction = Relationship.Direction.OUTGOING)
    private Set<Family> families = new HashSet<>();

    @Relationship(type = "HAS_PERSON", direction = Relationship.Direction.OUTGOING)
    private Set<Person> persons = new HashSet<>();

    @Relationship(type = "HAS_EVENT", direction = Relationship.Direction.OUTGOING)
    private Set<Event> events = new HashSet<>();

    @Relationship(type = "HAS_CITATION", direction = Relationship.Direction.OUTGOING)
    private Set<Citation> citations = new HashSet<>();

    @Relationship(type = "HAS_SOURCE", direction = Relationship.Direction.OUTGOING)
    private Set<Source> sources = new HashSet<>();

    @Relationship(type = "HAS_FILE", direction = Relationship.Direction.OUTGOING)
    private Set<File> files = new HashSet<>();

    @Relationship(type = "HAS_TREE", direction = Relationship.Direction.INCOMING)
    private GraphUser graphUser;

    public Tree(String name, Set<Family> families, Set<Person> persons, Set<Event> events, Set<Citation> citations, Set<Source> sources, Set<File> files) {
        this.name = name;
        this.families = families;
        this.persons = persons;
        this.events = events;
        this.citations = citations;
        this.sources = sources;
        this.files = files;
    }
}



