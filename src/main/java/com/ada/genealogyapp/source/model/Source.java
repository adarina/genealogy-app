package com.ada.genealogyapp.source.model;

import com.ada.genealogyapp.tree.model.Tree;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;


@NoArgsConstructor
@ToString
@Node
@Builder
@Getter
@Setter
@AllArgsConstructor
public class Source {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    private String name;

    @Relationship(type = "HAS_SOURCE", direction = Relationship.Direction.INCOMING)
    private Tree tree;


}
