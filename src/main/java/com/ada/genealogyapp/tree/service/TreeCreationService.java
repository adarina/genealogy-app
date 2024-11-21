package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.exceptions.NodeAlreadyExistsException;
import com.ada.genealogyapp.tree.dto.TreeRequest;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class TreeCreationService {


    private final TreeService treeService;

    public TreeCreationService(TreeService treeService) {
        this.treeService = treeService;
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