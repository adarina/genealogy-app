package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class TreeServiceImpl implements TreeService {

    private final TreeRepository treeRepository;

    public TreeServiceImpl(TreeRepository treeRepository) {
        this.treeRepository = treeRepository;
    }

    public Optional<Tree> findTreeByNameAndUserId(String name, Long userId) {
        Optional<Tree> tree = treeRepository.findByNameAndUserId(name, userId);
        if (tree.isPresent()) {
            log.info("Tree found: {}", tree.get());
        } else {
            log.error("No tree found with userId: {}", userId);
        }
        return tree;
    }

    public void saveTree(Tree tree) {
        Tree savedTree = treeRepository.save(tree);
        log.info("Tree saved successfully: {}", savedTree);
    }

    public void ensureTreeExists(UUID treeId) {
        if (!treeRepository.existsById(treeId)) {
            throw new NodeNotFoundException("Tree not found with ID: " + treeId);
        }
    }

    public Tree findTreeById(UUID treeId) {
        return treeRepository.findById(treeId)
                .orElseThrow(() -> new NodeNotFoundException("Tree not found with ID: " + treeId));
    }
}
