package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Slf4j
@Service
public class PersonCreationService {


    private final TreeService treeService;

    private final PersonRepository personRepository;

    public PersonCreationService(TreeService treeService, PersonRepository personRepository) {
        this.treeService = treeService;
        this.personRepository = personRepository;
    }

    @Transactional
    public Person createPerson(UUID treeId, PersonRequest personRequest) {
        Person person = PersonRequest.dtoToEntityMapper().apply(personRequest);

        Tree tree = treeService.findTreeByIdOrThrowNodeNotFoundException(treeId);
        person.setTree(tree);

        personRepository.save(person);
        treeService.saveTree(tree);

        log.info("Person created successfully: {}", person.getFirstname());
        return person;
    }
}
