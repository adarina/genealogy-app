package com.ada.genealogyapp.source.repository;

import com.ada.genealogyapp.source.model.Source;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SourceRepository extends Neo4jRepository<Source, UUID> {


}
