package com.ada.genealogyapp.location.model;

import com.ada.genealogyapp.location.type.LocationType;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Node
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Location {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    private String name;

    private Boolean isMain;

    private LocationType type;

    private Double latitude;

    private Double longitude;

    @Relationship(type = "HAS_LOCATION", direction = Relationship.Direction.INCOMING)
    private Tree tree;

    @Relationship(type = "LOCATED_IN", direction = Relationship.Direction.OUTGOING)
    private Location location;
}