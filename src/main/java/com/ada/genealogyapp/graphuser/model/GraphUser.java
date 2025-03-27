package com.ada.genealogyapp.graphuser.model;

import com.ada.genealogyapp.tree.model.Tree;
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
public class GraphUser {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Relationship(type = "HAS_TREE", direction = Relationship.Direction.OUTGOING)
    private Set<Tree> trees = new HashSet<>();

}
