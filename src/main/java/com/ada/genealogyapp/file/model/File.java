package com.ada.genealogyapp.file.model;


import com.ada.genealogyapp.tree.model.Tree;
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
public class File {


    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String type;

    private String path;

    @Relationship(type = "HAS_FILE", direction = Relationship.Direction.INCOMING)
    private Tree fileTree;

}
