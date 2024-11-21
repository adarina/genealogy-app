package com.ada.genealogyapp.citation.repository;

import com.ada.genealogyapp.citation.dto.CitationResponse;
import com.ada.genealogyapp.citation.dto.CitationsResponse;
import com.ada.genealogyapp.citation.model.Citation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface CitationRepository extends Neo4jRepository<Citation, UUID> {

    @Query("""
                MATCH (c:Citation {id: $citationId})
                SET c.page = COALESCE($page, c.page),
                    c.date = COALESCE($date, c.date)
                RETURN c
            """)
    void updateCitation(UUID citationId, String page, LocalDate date);

    @Query(value = """
            MATCH (t:Tree)-[:HAS_CITATION]->(c:Citation)
            WHERE t.id = $treeId
            AND (toLower(c.page) CONTAINS toLower($page) OR $page = '')
            WITH c
            OPTIONAL MATCH (c)-[:HAS_CITATION_SOURCE]->(s:Source)
            WITH c, s
            RETURN c.id AS id,
                   c.page AS page,
                   c.date AS date,
                   s.name AS name
                   :#{orderBy(#pageable)}
                   SKIP $skip
                   LIMIT $limit
                   """,
            countQuery = """
                    MATCH (t:Tree)-[:HAS_CITATION]->(c:Citation)
                    WHERE t.id = $treeId
                    RETURN count(c)
                    """)
    Page<CitationsResponse> findByTreeIdAndFilteredPage(@Param("treeId") UUID treeId, String page, Pageable pageable);

    @Query("""
                MATCH (c:Citation {id: $citationId})
                MATCH (t:Tree {id: $treeId})
                MERGE (t)-[:HAS_CITATION]->(c)
                WITH c
                OPTIONAL MATCH (c)-[:HAS_CITATION_SOURCE]->(s:Source)
                WITH c, s
                RETURN c.id AS id,
                       c.page AS page,
                       c.date AS date,
                       s.name AS name,
                       s.id AS sourceId
            """)
    Optional<CitationResponse> findByTreeIdAndCitationId(@Param("treeId") UUID treeId, @Param("citationId") UUID citationId);

}
