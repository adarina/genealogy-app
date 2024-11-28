package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class PersonCreationService {

    private final TreeService treeService;

    private final PersonService personService;

    private final PersonValidationService personValidationService;


    @TransactionalInNeo4j
    public Person createPerson(String treeId, PersonRequest personRequest) {
        Tree tree = treeService.findTreeById(treeId);

        Person person = Person.builder()
                .tree(tree)
                .firstname(personRequest.getFirstname())
                .lastname(personRequest.getLastname())
                .name(personRequest.getFirstname() + " " + personRequest.getLastname())
                .birthdate(personRequest.getBirthdate())
                .gender(personRequest.getGender())
                .build();

        personValidationService.validatePerson(person);
        personService.savePerson(person);
        log.info("Person created: {}", person);

        return person;
    }
}
