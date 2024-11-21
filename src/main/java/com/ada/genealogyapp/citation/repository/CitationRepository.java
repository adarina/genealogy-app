package com.ada.genealogyapp.citation.repository;

import com.ada.genealogyapp.citation.model.Citation;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.time.LocalDate;
import java.util.UUID;

public interface CitationRepository extends Neo4jRepository<Citation, UUID> {

    @Query("""
                MATCH (c:Citation {id: $citationId})
                SET c.page = COALESCE($page, c.page),
                    c.date = COALESCE($date, c.date)
                RETURN c
            """)
    void updateCitation(UUID citationId, String page, LocalDate date);

}
