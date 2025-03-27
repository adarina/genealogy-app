package com.ada.genealogyapp.tree.repository;


import com.ada.genealogyapp.tree.dto.TreeResponse;
import com.ada.genealogyapp.tree.model.Tree;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TreeRepository extends Neo4jRepository<Tree, String> {

    @Query("""
            MATCH (t:Tree {id: $treeId})
            RETURN t
            """)
    Optional<Tree> findTreeById(String treeId);

    @Query("""
            MATCH (user:GraphUser {id: $userId})
            OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree)
            RETURN tree.id AS id,
                   tree.name AS name
            """)
    List<TreeResponse> find(String userId);

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
                        OPTIONAL MATCH (tree)-[rel]-()
                        DELETE rel, tree
                        RETURN "TREE_DELETED" AS message
                    ',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {tree: tree}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String delete(String userId, String treeId);
}