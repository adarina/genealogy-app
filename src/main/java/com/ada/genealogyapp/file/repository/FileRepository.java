package com.ada.genealogyapp.file.repository;


import com.ada.genealogyapp.file.model.File;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;
import java.util.UUID;

public interface FileRepository extends Neo4jRepository<File, UUID> {

    List<File> findAllByFileTree_Id(UUID treeId);
}
