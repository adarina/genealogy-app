package com.ada.genealogyapp.tree.repository;


import com.ada.genealogyapp.tree.dto.TreeExportJsonResponse;
import com.ada.genealogyapp.tree.model.Tree;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TreeRepository extends Neo4jRepository<Tree, String> {

    Optional<Tree> findByName(String name);

    @Query("""
            MATCH (t:Tree)
            RETURN t
            """)
    List<Tree> findAllTrees();

    @Query("""
            MATCH (t:Tree {id: $treeId})-[:HAS_PERSON]->(p:Person)
            WITH t, COLLECT(p) AS persons
            RETURN t, persons
            """)
    TreeExportJsonResponse findTree(String treeId);

    @Query("""
            MATCH (t:Tree {id: $treeId})
            RETURN t
            """)
    Optional<Tree> findTreeById(String treeId);


    @Query("""
            MATCH (t:Tree {id: $treeId})
            OPTIONAL MATCH (t)-[:HAS_PERSON]->(p:Person)
            RETURN t, COLLECT(p) AS persons
            """)
    Map<String, Object> findTreeWithPersons(String treeId);


    @Query("""
            MATCH (tree:Tree {id: $treeId})-[:HAS_PERSON]->(person:Person),
                  (tree)-[:HAS_FAMILY]->(family:Family),
                  (tree)-[:HAS_EVENT]->(event:Event)
            WITH tree, person, family, event
            OPTIONAL MATCH (person)-[:PARENT_OF]->(child:Person)
            OPTIONAL MATCH (family)-[:HAS_FATHER]->(father:Person),
                             (family)-[:HAS_MOTHER]->(mother:Person),
                             (family)-[:HAS_CHILD]->(child:Person)
            OPTIONAL MATCH (event)-[:HAS_PARTICIPANT]->(participant:Person),
                             (event)-[:HAS_EVENT_CITATION]->(citation)
            RETURN tree,
                   person,
                   family,
                   event,
                   father,
                   mother,
                   participant,
                   citation
            """)
    Tree fetchTreeWithDetails(String treeId);


}


