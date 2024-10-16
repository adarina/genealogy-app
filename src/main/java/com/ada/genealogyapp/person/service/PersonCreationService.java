package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import com.ada.genealogyapp.tree.service.TreeSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Slf4j
@Service
public class PersonCreationService {

    private final TreeRepository treeRepository;

    private final PersonManagementService personManagementService;

    private final TreeSearchService treeSearchService;

    private final PersonRepository personRepository;

    public PersonCreationService(TreeRepository treeRepository, PersonManagementService personManagementService, TreeSearchService treeSearchService, PersonRepository personRepository) {
        this.treeRepository = treeRepository;
        this.personManagementService = personManagementService;
        this.treeSearchService = treeSearchService;
        this.personRepository = personRepository;
    }


    public Person create(Person person) {
        Person savedPerson = personRepository.save(person);
        log.info("Person created successfully: {}", savedPerson);
        return savedPerson;
    }


    public void createPerson(UUID treeId, PersonRequest personRequest) {
        Person person = PersonRequest.dtoToEntityMapper().apply(personRequest);

        Tree tree = treeSearchService.findTreeById(treeId);
        person.setTree(tree);

        create(person);
        treeRepository.save(tree);

        log.info("Person created successfully: {}", person.getFirstname());
    }

}
