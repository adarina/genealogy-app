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

    public Tree findTreeByIdOrThrowNodeNotFoundException(UUID treeId) {
        Optional<Tree> tree = treeRepository.findById(treeId);
        if (tree.isPresent()) {
            log.info("Tree found: {}", tree.get());
        } else {
            log.error("No tree found with id: {}", treeId);
            throw new NodeNotFoundException("Tree with id " + treeId + " does not exist");
        }
        return tree.get();
    }

    public void saveTree(Tree tree) {
        Tree savedTree = treeRepository.save(tree);
        log.info("Tree saved successfully: {}", savedTree);
    }


}
