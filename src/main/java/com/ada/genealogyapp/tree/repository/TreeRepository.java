package com.ada.genealogyapp.tree.repository;


import com.ada.genealogyapp.tree.dto.TreeResponse;
import com.ada.genealogyapp.tree.model.Tree;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreeRepository extends Neo4jRepository<Tree, String> {


    @Query("""
            MATCH (user:GraphUser {id: $userId})
            OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree)
            RETURN tree.id AS id,
                   tree.name AS name
            """)
    List<TreeResponse> find(String userId);

    @Query("""
            MATCH (user:GraphUser {id: $userId})
            OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
            RETURN tree.id AS id,
                   tree.name AS name
            """)
    TreeResponse find(String userId, String treeId);

    @Query("""
                CALL {
                    OPTIONAL MATCH (user:GraphUser {id: $userId})
                    WITH count(user) > 0 AS userExist
                    
                    OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                    RETURN count(tree) > 0 AS treeExist, userExist, tree
                }
                CALL apoc.do.case(
                    [
                        userExist AND treeExist, 'RETURN "SUCCESS" AS message',
                        userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                    ],
                    'RETURN "USER_NOT_EXIST" AS message',
                    {tree: tree}
                ) YIELD value
                RETURN value.message
                LIMIT 1
            """)
    String checkTreeAndUserExistence(String userId, String treeId);

    @Query("""
               RETURN CASE
                    WHEN EXISTS { MATCH (user:GraphUser {id: $userId}) }
                    THEN "SUCCESS"
                    ELSE "USER_NOT_EXIST"
                END AS message
                LIMIT 1
            """)
    String checkUserExistence(String userId);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                RETURN count(user) > 0 AS userExist, user
            }
                        
            CALL apoc.do.case(
                [
                    userExist, '
                        MERGE (user)-[:HAS_TREE]->(tree:Tree {id: treeId})
                        SET tree.name = name
                            
                        RETURN "TREE_CREATED" AS message
                    '
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {user: user, treeId: $treeId, name: $name}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String save(String userId, String treeId, String name);

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
                        SET tree.name = $name
                            
                        RETURN "TREE_UPDATED" AS message
                    ',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {tree: tree, name: $name}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String update(String userId, String treeId, String name);

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
                        CALL apoc.periodic.iterate(
                            "MATCH (t:Tree {id: $treeId})-[:HAS_PERSON]->(p:Person) RETURN p",
                            "DETACH DELETE p",
                            {batchSize: 1000, parallel: false, params: {treeId: $treeId}}
                        ) YIELD batches AS personsBatches, total AS personsTotal
                        
                        CALL apoc.periodic.iterate(
                            "MATCH (t:Tree {id: $treeId})-[:HAS_FAMILY]->(f:Family) RETURN f",
                            "DETACH DELETE f",
                            {batchSize: 1000, parallel: false, params: {treeId: $treeId}}
                        ) YIELD batches AS familiesBatches, total AS familiesTotal
                        
                        CALL apoc.periodic.iterate(
                            "MATCH (t:Tree {id: $treeId})-[:HAS_EVENT]->(e:Event) RETURN e",
                            "DETACH DELETE e",
                            {batchSize: 1000, parallel: false, params: {treeId: $treeId}}
                        ) YIELD batches AS eventsBatches, total AS eventsTotal
                        
                        CALL apoc.periodic.iterate(
                            "MATCH (t:Tree {id: $treeId})-[:HAS_CITATION]->(c:Citation) RETURN c",
                            "DETACH DELETE c",
                            {batchSize: 1000, parallel: false, params: {treeId: $treeId}}
                        ) YIELD batches AS citationsBatches, total AS citationsTotal
                        
                        CALL apoc.periodic.iterate(
                            "MATCH (t:Tree {id: $treeId})-[:HAS_SOURCE]->(s:Source) RETURN s",
                            "DETACH DELETE s",
                            {batchSize: 1000, parallel: false, params: {treeId: $treeId}}
                        ) YIELD batches AS sourcesBatches, total AS sourcesTotal
                        
                        CALL apoc.periodic.iterate(
                            "MATCH (t:Tree {id: $treeId})-[:HAS_FILE]->(file:File) RETURN file",
                            "DETACH DELETE file",
                            {batchSize: 1000, parallel: false, params: {treeId: $treeId}}
                        ) YIELD batches AS filesBatches, total AS filesTotal
                        
                        CALL apoc.periodic.iterate(
                            "MATCH (t:Tree {id: $treeId})-[:HAS_LOCATION]->(l:Location) RETURN l",
                            "DETACH DELETE l",
                            {batchSize: 1000, parallel: false, params: {treeId: $treeId}}
                        ) YIELD batches AS locationsBatches, total AS locationsTotal
                        
                        MATCH (t:Tree {id: $treeId})
                        DETACH DELETE t
                        
                        RETURN "TREE_DELETED" AS message
                    ',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {tree: tree, treeId: $treeId}
            ) YIELD value
            RETURN value.message
            LIMIT 1""")
    String delete(String userId, String treeId);
}
