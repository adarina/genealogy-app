package com.ada.genealogyapp.citation.model;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
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

public class Citation {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    private String page;

    private LocalDate date;

    @Relationship(type = "HAS_CITATION", direction = Relationship.Direction.INCOMING)
    private Tree tree;

    @Relationship(type = "HAS_SOURCE", direction = Relationship.Direction.OUTGOING)
    private Set<Source> sources = new HashSet<>();

    @Relationship(type = "HAS_FILE", direction = Relationship.Direction.OUTGOING)
    private Set<File> files = new HashSet<>();

    @Relationship(type = "HAS_EVENT_CITATION", direction = Relationship.Direction.INCOMING)
    private Set<Event> events;

    public Citation(UUID id) {
        this.id = id;
    }

}
