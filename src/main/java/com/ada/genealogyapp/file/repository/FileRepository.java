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
