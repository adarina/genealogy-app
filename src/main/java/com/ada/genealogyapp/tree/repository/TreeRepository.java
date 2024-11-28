package com.ada.genealogyapp.tree.repository;


import com.ada.genealogyapp.tree.model.Tree;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface TreeRepository extends Neo4jRepository<Tree,String> {

    Optional<Tree> findByName(String name);

}
