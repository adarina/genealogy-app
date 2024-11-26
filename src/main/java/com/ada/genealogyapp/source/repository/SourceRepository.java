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
import java.util.UUID;

@Repository
public interface SourceRepository extends Neo4jRepository<Source, UUID> {

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
    Page<SourcesResponse> findByTreeIdAndFilteredName(@Param("treeId") UUID treeId, String name, Pageable pageable);

    @Query("""
                MATCH (s:Source {id: $sourceId})
                MATCH (t:Tree {id: $treeId})
                MERGE (t)-[:HAS_SOURCE]->(s)
                WITH s
                RETURN s.id AS id,
                       s.name AS name
            """)
    Optional<SourceResponse> findByTreeIdAndSourceId(@Param("treeId") UUID treeId, @Param("sourceId") UUID sourceId);

}
