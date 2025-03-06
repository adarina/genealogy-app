package com.ada.genealogyapp.citation.model;

import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.nonNull;


@Node
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Citation {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    private String page;

    private String date;

    @Relationship(type = "HAS_CITATION", direction = Relationship.Direction.INCOMING)
    private Tree tree;

    @Relationship(type = "HAS_CITATION_SOURCE", direction = Relationship.Direction.OUTGOING)
    private Source source;

    @Relationship(type = "HAS_CITATION_FILE", direction = Relationship.Direction.OUTGOING)
    private Set<File> files = new HashSet<>();

}
