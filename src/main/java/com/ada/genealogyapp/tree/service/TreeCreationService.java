package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.exceptions.NodeAlreadyExistsException;
import com.ada.genealogyapp.source.dto.SourceRequest;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.tree.dto.TreeRequest;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class TreeCreationService {

    private final TreeRepository treeRepository;

    private final TreeService treeService;

    private final TreeSearchService treeSearchService;

    public TreeCreationService(TreeRepository treeRepository, TreeService treeService, TreeSearchService treeSearchService) {
        this.treeRepository = treeRepository;
        this.treeService = treeService;
        this.treeSearchService = treeSearchService;
    }

    //TODO validation
    @TransactionalInNeo4j
    public void createTree(TreeRequest treeRequest) {

        Tree tree = Tree.builder()
                .name(treeRequest.getName())
                .userId(treeRequest.getUserId())
                .build();

        Optional<Tree> existingTree = treeService.findTreeByNameAndUserId(tree.getName(), tree.getUserId());

        if (existingTree.isPresent()) {
            throw new NodeAlreadyExistsException("Tree with name " + tree.getName() + " already exists");
        }
        treeService.saveTree(tree);
    }
}