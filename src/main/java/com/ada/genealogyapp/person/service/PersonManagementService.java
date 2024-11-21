package com.ada.genealogyapp.person.service;


import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.event.service.EventService;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Objects.nonNull;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonManagementService {

    private final PersonService personService;

    private final TreeService treeService;


    //TODO validation
    @TransactionalInNeo4j
    public void updatePerson(UUID treeId, UUID personId, PersonRequest personRequest) {
        treeService.ensureTreeExists(treeId);
        Person person = personService.findPersonById(personId);

        if (nonNull(personRequest.getFirstname())) {
            person.setFirstname(personRequest.getFirstname());
        }
        if (nonNull(personRequest.getLastname())) {
            person.setLastname(personRequest.getLastname());
        }
        if (nonNull(personRequest.getFirstname()) && nonNull(personRequest.getLastname())) {
            person.setName(personRequest.getFirstname() + " " + personRequest.getLastname());
        }
        if (nonNull(personRequest.getBirthdate())) {
            person.setBirthdate(personRequest.getBirthdate());
        }
        if (nonNull(personRequest.getGender())) {
            person.setGender(personRequest.getGender());
        }

        personService.savePerson(person);
        log.info("Person updated successfully: {}", personId);
    }

    @TransactionalInNeo4j
    public void deletePerson(UUID treeId, UUID personId) {
        treeService.ensureTreeExists(treeId);
        Person person = personService.findPersonById(personId);

        personService.deletePerson(person);
    }
}
