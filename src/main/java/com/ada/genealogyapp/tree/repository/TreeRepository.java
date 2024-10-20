package com.ada.genealogyapp.tree.repository;


import com.ada.genealogyapp.tree.model.Tree;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;
import java.util.UUID;

public interface TreeRepository extends Neo4jRepository<Tree, UUID> {

    Optional<Tree> findByNameAndUserId(String name, Long userId);

}
