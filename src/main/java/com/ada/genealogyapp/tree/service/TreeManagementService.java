package com.ada.genealogyapp.tree.service;


import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import com.ada.genealogyapp.tree.dto.params.DeleteTreeParams;
import com.ada.genealogyapp.tree.dto.params.UpdateTreeParams;
import com.ada.genealogyapp.tree.dto.params.UpdateTreeRequestParams;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class TreeManagementService {

    private final TreeValidationService treeValidationService;

    private final TreeService treeService;

    @TransactionalInNeo4j
    public void updateTree(UpdateTreeRequestParams params) {
        Tree tree = Tree.builder()
                .name(params.getTreeRequest().getName())
                .build();

        treeValidationService.validateTree(tree);
        treeService.updateTree(UpdateTreeParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .tree(tree)
                .build());
    }

    @TransactionalInNeo4j
    public void deleteTree(DeleteTreeParams params) {
        treeService.deleteTree(params);
    }
}
