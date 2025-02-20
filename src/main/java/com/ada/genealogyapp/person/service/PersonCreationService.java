package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.person.type.GenderType;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class PersonCreationService {

    private final PersonService personService;

    private final PersonValidationService personValidationService;

    @TransactionalInNeo4j
    public Person createPerson(String userId, String treeId, PersonRequest personRequest) {

        Person person = Person.builder()
                .firstname(personRequest.getFirstname())
                .lastname(personRequest.getLastname())
                .gender(personRequest.getGender())
                .build();

        personValidationService.validatePerson(person);
        personService.savePerson(userId, treeId, person);

        return person;
    }

    @TransactionalInNeo4j
    public Person createPerson(String userId, Tree tree, String firstname, String lastname, GenderType gender) {

        Person person = Person.builder()
                .tree(tree)
                .firstname(firstname)
                .lastname(lastname)
                .gender(gender)
                .build();

        personValidationService.validatePerson(person);
        personService.savePerson(userId, tree.getId(), person);

        return person;
    }
}
