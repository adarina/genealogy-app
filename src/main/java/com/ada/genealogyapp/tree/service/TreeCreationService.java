package com.ada.genealogyapp.tree.service;


import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import com.ada.genealogyapp.tree.dto.params.CreateTreeImportParams;
import com.ada.genealogyapp.tree.dto.params.CreateTreeRequestParams;
import com.ada.genealogyapp.tree.dto.params.SaveTreeParams;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TreeCreationService {

    private final TreeService treeService;

    private final TreeValidationService treeValidationService;

    public void validateAndSaveTree(String userId, Tree tree) {
        treeValidationService.validateTree(tree);
        treeService.saveTree(SaveTreeParams.builder()
                .userId(userId)
                .treeId(tree.getId())
                .tree(tree)
                .build());
    }

    @TransactionalInNeo4j
    public void createTree(CreateTreeRequestParams params) {
        Tree tree = Tree.builder()
                .id(UUID.randomUUID().toString())
                .name(params.getTreeRequest().getName())
                .build();
        validateAndSaveTree(params.getUserId(), tree);
    }

    @TransactionalInNeo4j
    public Tree createTreeImport(CreateTreeImportParams params) {
        Tree tree = Tree.builder()
                .id(UUID.randomUUID().toString())
                .name(params.getName())
                .build();
        validateAndSaveTree(params.getUserId(), tree);
        return tree;
    }
}
