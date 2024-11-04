package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class TreeSearchService {
    private final TreeRepository treeRepository;

    public TreeSearchService(TreeRepository treeRepository) {
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

    public Optional<Tree> findTreeByIdOrThrowNodeNotFoundException(UUID treeId) {
        Optional<Tree> tree = treeRepository.findById(treeId);
        if (tree.isPresent()) {
            log.info("Tree found: {}", tree.get());
        } else {
            log.error("No tree found with id: {}", treeId);
            throw new NodeNotFoundException("No tree found with id: " + treeId);
        }
        return tree;
    }

    public List<Tree> getTreesOrThrowNodeNotFoundException() {
        List<Tree> trees = treeRepository.findAll();
        if (!trees.isEmpty()) {
            log.info("Trees found for userId: {}", trees);
        } else {
            log.error("No trees found for userId");
            throw new NodeNotFoundException("No trees found for userId: ");
        }
        return trees;
    }
}
