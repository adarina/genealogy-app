package com.ada.genealogyapp.file.repository;


import com.ada.genealogyapp.file.dto.FileResponse;
import com.ada.genealogyapp.file.model.File;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FileRepository extends Neo4jRepository<File, String> {


    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
                RETURN count(user) > 0 AS userExist, count(tree) > 0 AS treeExist, tree
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
                MATCH (f:File {id: $fileId})
                MATCH (t:Tree {id: $treeId})
                MERGE (t)-[:HAS_FILE]->(f)
                WITH f
                RETURN f.id AS id,
                       f.type AS type,
                       f.filename AS filename,
                       $baseUrl + f.filename AS path,
                       f.name AS name
            """)
    Optional<FileResponse> findByTreeIdAndFileId(@Param("treeId") String treeId, @Param("fileId") String fileId, String baseUrl);

    @Query(value = """
            MATCH (t:Tree)-[:HAS_FILE]->(f:File)
            WHERE t.id = $treeId
            AND (toLower(f.name) CONTAINS toLower($name) OR $name = '')
            AND (toLower(f.type) CONTAINS toLower($type) OR $type = '')
            WITH f
            RETURN f.id AS id,
                   f.type AS type,
                   f.filename AS filename,
                   $baseUrl + f.filename AS path,
                   f.name AS name
                   :#{orderBy(#pageable)}
                   SKIP $skip
                   LIMIT $limit
                   """,
            countQuery = """
                    MATCH (t:Tree)-[:HAS_FILE]->(f:File)
                    WHERE t.id = $treeId
                    RETURN count(f)
                    """)
    Page<FileResponse> findByTreeIdAndFilteredNameAndType(@Param("treeId") String treeId, String name, String type, @Param("baseUrl") String baseUrl, Pageable pageable);
}
