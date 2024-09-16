package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.service.PersonService;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import com.ada.genealogyapp.tree.service.TreeSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
public class PersonCreationService {

    private final TreeRepository treeRepository;

    private final PersonService personService;

    private final TreeSearchService treeSearchService;

    public PersonCreationService(TreeRepository treeRepository, PersonService personService, TreeSearchService treeSearchService) {
        this.treeRepository = treeRepository;
        this.personService = personService;
        this.treeSearchService = treeSearchService;
    }

    public Tree create(Tree tree) {
        Tree savedTree = treeRepository.save(tree);
        log.info("Tree created successfully: {}", savedTree);
        return savedTree;
    }

    private Tree findTreeById(UUID treeId) {
        return treeSearchService.find(treeId)
                .orElseThrow(() -> {
                    log.error("Tree with ID {} does not exist", treeId);
                    return new NodeNotFoundException("Tree with ID " + treeId + " does not exist");
                });
    }


    public void createPerson(UUID treeId, PersonRequest personRequest) {
        Person person = PersonRequest.dtoToEntityMapper().apply(personRequest);

        Tree tree = findTreeById(treeId);
        tree.getPersons().add(person);

        person = personService.create(person);
        treeRepository.save(tree);

        log.info("Person created successfully: {}", person.getFirstname());
    }

}
