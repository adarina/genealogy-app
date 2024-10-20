package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.exceptions.NodeAlreadyExistsException;
import com.ada.genealogyapp.tree.dto.TreeRequest;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class TreeCreationService {

    private final TreeRepository treeRepository;

    private final TreeSearchService treeSearchService;

    public TreeCreationService(TreeRepository treeRepository, TreeSearchService treeSearchService) {
        this.treeRepository = treeRepository;
        this.treeSearchService = treeSearchService;
    }

    public void createTree(TreeRequest treeRequest) {
        Tree tree = TreeRequest.dtoToEntityMapper().apply(treeRequest);
        Optional<Tree> existingTree = treeSearchService.findTreeByNameAndUserId(tree.getName(), tree.getUserId());

        if (existingTree.isPresent()) {
            log.error("Tree with name {} already exists", tree.getName());
            throw new NodeAlreadyExistsException("Tree with name " + tree.getName() + " already exists");
        }

        treeRepository.save(tree);
        log.info("Tree created successfully: {}", tree.getName());
    }
}
