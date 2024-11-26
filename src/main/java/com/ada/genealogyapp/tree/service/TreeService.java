package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.tree.model.Tree;

import java.util.Optional;
import java.util.UUID;

public interface TreeService {
    Optional<Tree> findTreeByName(String name);
    void saveTree(Tree tree);
    void ensureTreeExists(UUID treeId);
    Tree findTreeById(UUID treeId);
}
