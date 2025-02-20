package com.ada.genealogyapp.source.repository;

import com.ada.genealogyapp.source.dto.SourceResponse;
import com.ada.genealogyapp.source.dto.SourcesResponse;
import com.ada.genealogyapp.source.model.Source;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SourceRepository extends Neo4jRepository<Source, String> {

    @Query("""
             CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
                RETURN count(tree) > 0 AS treeExist, count(user) > 0 AS userExist, tree
            }
                        
            CALL apoc.do.case(
                [
                    userExist AND treeExist, '
                        MERGE (tree)-[:HAS_SOURCE]->(source:Source {id: sourceId})
                        SET source.name = name
                            
                        RETURN "SOURCE_CREATED" AS message
                    ',
                     userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {tree: tree, sourceId: $sourceId, name: $name}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String save(String userId, String treeId, String sourceId, String name);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH count(tree) > 0 AS treeExist, count(user) > 0 AS userExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_SOURCE]->(source:Source {id: $sourceId})
                RETURN userExist, treeExist, tree, count(source) > 0 AS sourceExist, source
            }
                        
            CALL apoc.do.case(
                [
                    treeExist AND sourceExist, '
                        SET source.name = $name
                            
                        RETURN "SOURCE_UPDATED" AS message
                    ',
                    userExist AND treeExist, 'RETURN "SOURCE_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {source: source, name: $name}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String update(String userId, String treeId, String sourceId, String name);

    @Query(value = """
            MATCH (t:Tree)-[:HAS_SOURCE]->(s:Source)
            WHERE t.id = $treeId
            AND (toLower(s.name) CONTAINS toLower($name) OR $name = '')
            WITH s
            RETURN s.id AS id,
                   s.name AS name
                   :#{orderBy(#pageable)}
                   SKIP $skip
                   LIMIT $limit
                   """,
            countQuery = """
                    MATCH (t:Tree)-[:HAS_SOURCE]->(s:Source)
                    WHERE t.id = $treeId
                    RETURN count(s)
                    """)
    Page<SourcesResponse> findByTreeIdAndFilteredName(@Param("treeId") String treeId, String name, Pageable pageable);

    @Query("""
                MATCH (s:Source {id: $sourceId})
                MATCH (t:Tree {id: $treeId})
                MERGE (t)-[:HAS_SOURCE]->(s)
                WITH s
                RETURN s.id AS id,
                       s.name AS name
            """)
    Optional<SourceResponse> findByTreeIdAndSourceId(@Param("treeId") String treeId, @Param("sourceId") String sourceId);

}
