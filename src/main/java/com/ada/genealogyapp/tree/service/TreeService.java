package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.tree.model.Tree;

import java.util.Optional;

public interface TreeService {
    Optional<Tree> findTreeByName(String name);
    void saveTree(Tree tree);
    void ensureTreeExists(String treeId);
    Tree findTreeById(String treeId);
}
