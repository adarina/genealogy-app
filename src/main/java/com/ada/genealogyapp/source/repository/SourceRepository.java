package com.ada.genealogyapp.source.repository;

import com.ada.genealogyapp.source.dto.SourceResponse;
import com.ada.genealogyapp.source.model.Source;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceRepository extends Neo4jRepository<Source, String> {

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                RETURN userExist, count(tree) > 0 AS treeExist, tree
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
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_SOURCE]->(source:Source {id: $sourceId})
                RETURN userExist, treeExist, tree, count(source) > 0 AS sourceExist, source
            }
                        
            CALL apoc.do.case(
                [
                     userExist AND treeExist AND sourceExist, '
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

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_SOURCE]->(source:Source {id: $sourceId})
                RETURN userExist, treeExist, tree, count(source) > 0 AS sourceExist, source
            }
                        
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND sourceExist, '
                        OPTIONAL MATCH (source)-[rel]-()
                        DELETE rel, source
                        RETURN "SOURCE_DELETED" AS message
                    ',
                    userExist AND treeExist, 'RETURN "SOURCE_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {tree: tree, source: source}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String delete(String userId, String treeId, String sourceId);

    @Query(value = """
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
            OPTIONAL MATCH (tree)-[:HAS_SOURCE]->(source:Source)
            WHERE toLower(source.name) CONTAINS toLower($name) OR $name = ''
            RETURN source.id AS id,
                   source.name AS name
                   :#{orderBy(#pageable)}
                   SKIP $skip
                   LIMIT $limit
            """,
            countQuery = """
                        MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_SOURCE]->(source:Source)
                        WHERE toLower(source.name) CONTAINS toLower($name) OR $name = ''
                        RETURN count(source)
                    """)
    Page<SourceResponse> find(String userId, String treeId, String name, Pageable pageable);

    @Query("""
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_SOURCE]->(source:Source {id: $sourceId})
            RETURN source.id AS id,
                source.name AS name
            """)
    SourceResponse find(String userId, String treeId, String sourceId);
}