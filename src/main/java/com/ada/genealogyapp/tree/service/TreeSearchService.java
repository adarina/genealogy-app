package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class TreeSearchService {
    private final TreeRepository treeRepository;

    public TreeSearchService(TreeRepository treeRepository) {
        this.treeRepository = treeRepository;
    }

    public List<Tree> getTreesOrThrowNodeNotFoundException() {
        List<Tree> trees = treeRepository.findAllTrees();
        if (!trees.isEmpty()) {
            log.info("Trees found for userId: {}", trees);
        } else {
            log.error("No trees found for userId");
            throw new NodeNotFoundException("No trees found for userId: ");
        }
        return trees;
    }
}
