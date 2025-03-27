package com.ada.genealogyapp.file.repository;


import com.ada.genealogyapp.file.dto.FileResponse;
import com.ada.genealogyapp.file.model.File;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface FileRepository extends Neo4jRepository<File, String> {

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
                        MERGE (tree)-[:HAS_FILE]->(file:File {id: fileId})
                        SET file.name = name,
                            file.type = type,
                            file.path = path,
                            file.filename = filename
                            
                        RETURN "FILE_CREATED" AS message
                    ',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {tree: tree, fileId: $fileId, name: $name, type: $type, path: $path, filename: $filename}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String save(String userId, String treeId, String fileId, String name, String type, String path, String filename);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_FILE]->(file:File {id: $fileId})
                RETURN userExist, treeExist, tree, count(file) > 0 AS fileExist, file
            }
                        
            CALL apoc.do.case(
                [
                    treeExist AND fileExist, '
                        SET file.name = $name
  
                        RETURN "FILE_UPDATED" AS message
                    ',
                    userExist AND treeExist, 'RETURN "FILE_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {file: file, name: $name}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String update(String userId, String treeId, String fileId, String name);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_FILE]->(file:File {id: $fileId})
                RETURN userExist, treeExist, tree, count(file) > 0 AS fileExist, file
            }
                        
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND fileExist, '
                        OPTIONAL MATCH (file)-[rel]-()
                        DELETE rel, file
                        RETURN "FILE_DELETED" AS message
                    ',
                    userExist AND treeExist, 'RETURN "FILE_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {tree: tree, file: file}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String delete(String userId, String treeId, String fileId);
    @Query("""
                MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_FILE]->(file:File {id: $fileId})
                RETURN file.id AS id,
                       file.type AS type,
                       file.filename AS filename,
                       $baseUrl + file.filename AS path,
                       file.name AS name
            """)
    FileResponse find(String userId, String treeId, String fileId, String baseUrl);

    @Query(value = """
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
            OPTIONAL MATCH (tree)-[:HAS_FILE]->(file:File)
            WHERE (toLower(file.name) CONTAINS toLower($name) OR $name = '')
                AND (toLower(file.type) CONTAINS toLower($type) OR $type = '')
            WITH file
            RETURN file.id AS id,
                   file.type AS type,
                   file.filename AS filename,
                   $baseUrl + file.filename AS path,
                   file.name AS name
                   :#{orderBy(#pageable)}
                   SKIP $skip
                   LIMIT $limit
                   """,
            countQuery = """
                    MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
                    OPTIONAL MATCH (tree)-[:HAS_FILE]->(file:File)
                    WHERE (toLower(file.name) CONTAINS toLower($name) OR $name = '')
                       AND (toLower(file.type) CONTAINS toLower($type) OR $type = '')
                    RETURN count(file)
                    """)
    Page<FileResponse> find(String userId, String treeId, String name, String type, String baseUrl, Pageable pageable);
}
