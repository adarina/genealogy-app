package com.ada.genealogyapp.citation.repository;

import com.ada.genealogyapp.citation.model.Citation;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.UUID;

public interface CitationRepository extends Neo4jRepository<Citation, UUID> {


}
