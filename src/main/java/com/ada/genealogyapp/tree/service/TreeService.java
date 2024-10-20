package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.tree.model.Tree;

import java.util.UUID;

public interface TreeService {
    Tree findTreeByIdOrThrowNodeNotFoundException(UUID treeId);

    void saveTree(Tree tree);


}
