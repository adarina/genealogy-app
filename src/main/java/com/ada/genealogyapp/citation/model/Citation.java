package com.ada.genealogyapp.citation.model;

import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;
import java.util.UUID;


@Node
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Citation {

    @Id
    @GeneratedValue
    private UUID id;

    public String page;

    public LocalDate creationDate;

    @Relationship(type = "HAS_CITATION", direction = Relationship.Direction.INCOMING)
    private Tree tree;

}
