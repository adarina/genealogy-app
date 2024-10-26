package com.ada.genealogyapp.family.relationship;

import com.ada.genealogyapp.family.model.Family;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@NoArgsConstructor
@ToString
@Getter
@Setter
@RelationshipProperties
public class FamilyRelationship {

    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private Family family;


}