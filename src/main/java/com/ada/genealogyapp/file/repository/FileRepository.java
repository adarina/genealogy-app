package com.ada.genealogyapp.file.repository;


import com.ada.genealogyapp.file.dto.FileResponse;
import com.ada.genealogyapp.file.model.File;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends Neo4jRepository<File, UUID> {

    List<File> findAllByFileTree_Id(UUID treeId);

    @Query("""
                MATCH (f:File {id: $fileId})
                MATCH (t:Tree {id: $treeId})
                MERGE (t)-[:HAS_FILE]->(f)
                WITH f
                RETURN f.id AS id,
                       f.type AS type,
                       f.filename AS filename,
                       'http://localhost:8080/upload-dir/' + f.filename AS path,
                       f.name AS name
            """)
    Optional<FileResponse> findByTreeIdAndFileId(@Param("treeId") UUID treeId, @Param("fileId") UUID fileId);

    @Query("""
                MATCH (f:File {id: $fileId})
                SET f.name = COALESCE($name, f.name)
                RETURN f
            """)
    void updateFile(UUID fileId, String name);
}
