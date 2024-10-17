package com.ada.genealogyapp.tree.model;

import com.ada.genealogyapp.event.model.Event;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;
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

    @Relationship(type = "HAS_EVENT", direction = Relationship.Direction.OUTGOING)
    private Set<Event> events = new HashSet<>();
}
