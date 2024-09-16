package com.ada.genealogyapp.family.repostitory;

import com.ada.genealogyapp.family.model.Family;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.UUID;


public interface FamilyRepository extends Neo4jRepository<Family, UUID> {
}
