package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class TreeSearchService {
    private final TreeRepository treeRepository;

    public TreeSearchService(TreeRepository treeRepository) {
        this.treeRepository = treeRepository;
    }

    public Optional<Tree> find(String name, Long userId) {
        Optional<Tree> tree = treeRepository.findByNameAndUserId(name, userId);
        if (tree.isPresent()) {
            log.info("Tree found: {}", tree.get());
        } else {
            log.warn("No tree found with userId: {}", userId);
        }
        return tree;
    }

    public Optional<Tree> find(UUID treeId) {
        Optional<Tree> tree = treeRepository.findById(treeId);
        if (tree.isPresent()) {
            log.info("Tree found: {}", tree.get());
        } else {
            log.warn("No tree found with id: {}", treeId);
        }
        return tree;
    }

    public Tree findTreeById(UUID treeId) {
        return find(treeId)
                .orElseThrow(() -> {
                    log.error("Tree with ID {} does not exist", treeId);
                    return new NodeNotFoundException("Tree with ID " + treeId + " does not exist");
                });
    }
}
