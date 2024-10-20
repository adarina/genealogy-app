package com.ada.genealogyapp.source.model;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.tree.model.Tree;
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
public class Source {

    @Id
    @GeneratedValue
    private UUID id;

    public String name;

    @Relationship(type = "HAS_SOURCE", direction = Relationship.Direction.INCOMING)
    private Tree tree;


}
