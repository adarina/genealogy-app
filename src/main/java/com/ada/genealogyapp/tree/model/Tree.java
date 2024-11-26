package com.ada.genealogyapp.tree.model;

import com.ada.genealogyapp.userneo4j.model.UserNeo4j;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;


import java.util.UUID;

@Node
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tree {


    @Id
    @GeneratedValue
    private UUID id;

    private Long userId;

    private String name;

//    @Relationship(type = "HAS_TREE", direction = Relationship.Direction.INCOMING)
//    private UserNeo4j userNeo4j;


}
