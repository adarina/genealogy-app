package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.exceptions.NodeAlreadyExistsException;
import com.ada.genealogyapp.person.service.PersonService;
import com.ada.genealogyapp.tree.dto.TreeRequest;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class TreeCreationService {

    private final TreeRepository treeRepository;

    private final PersonService personService;

    private final TreeSearchService treeSearchService;

    public TreeCreationService(TreeRepository treeRepository, PersonService personService, TreeSearchService treeSearchService) {
        this.treeRepository = treeRepository;
        this.personService = personService;
        this.treeSearchService = treeSearchService;
    }

    public void createTree(TreeRequest treeRequest) {
        Tree tree = TreeRequest.dtoToEntityMapper().apply(treeRequest);
        Optional<Tree> existingTree = treeSearchService.find(tree.getName(), tree.getUserId());

        if (existingTree.isPresent()) {
            log.error("Tree with name {} already exists", tree.getName());
            throw new NodeAlreadyExistsException("Tree with name " + tree.getName() + " already exists");
        }

        tree = create(tree);
        log.info("Tree created successfully: {}", tree.getName());
    }

    public Tree create(Tree tree) {
        Tree savedTree = treeRepository.save(tree);
        log.info("Tree created successfully: {}", savedTree);
        return savedTree;
    }

//    public boolean createPerson(Long treeId, PersonRequest personRequest) {
//        Person person = PersonRequest.dtoToEntityMapper().apply(personRequest);
//        Optional<Tree> existingTree = treeSearchService.find(treeId);
//
//        if (existingTree.isEmpty()) {
//            log.error("Tree with ID {} does not exist", treeId);
//            throw new NodeNotFoundException("Tree with ID " + treeId + " does not exist");
//        }
//        person = personService.create(person);
//        log.info("Person created successfully: {}", person.getFirstname());
//
//        return true;
//    }

}
