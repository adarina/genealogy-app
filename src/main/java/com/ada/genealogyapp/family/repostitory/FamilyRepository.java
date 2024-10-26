package com.ada.genealogyapp.family.repostitory;

import com.ada.genealogyapp.family.model.Family;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface FamilyRepository extends Neo4jRepository<Family, UUID> {

    List<Family> findAllByFamilyTree_Id(UUID treeId);

    @Query("MATCH (f:Family)-[:HAS_FATHER]->(p:Person {id: $personId}) RETURN f")
    Set<Family> findByFatherId(UUID personId);


    @Query("MATCH (f:Family)-[:HAS_MOTHER]->(p:Person {id: $personId}) RETURN f")
    Set<Family> findByMotherId(UUID personId);


    @Query("MATCH (f:Family)-[:HAS_CHILD]->(p:Person {id: $personId}) RETURN f")
    Set<Family> findByChildId(UUID personId);

}