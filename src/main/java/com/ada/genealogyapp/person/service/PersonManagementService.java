package com.ada.genealogyapp.person.service;


import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.service.FamilyService;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonManagementService {

    private final PersonService personService;

    private final TreeService treeService;

    private final PersonValidationService personValidationService;

    private final FamilyService familyService;


    @TransactionalInNeo4j
    public void updatePerson(UUID treeId, UUID personId, @NonNull PersonRequest personRequest) {
        treeService.ensureTreeExists(treeId);
        Person person = personService.findPersonById(personId);

        person.setFirstname(personRequest.getFirstname());
        person.setLastname(personRequest.getLastname());
        person.setName(personRequest.getFirstname() + " " + personRequest.getLastname());
        person.setBirthdate(personRequest.getBirthdate());
        person.setGender(personRequest.getGender());

        personValidationService.validatePerson(person);

        personService.savePerson(person);

        List<Family> families = familyService.findFamiliesByPerson(person);
        familyService.updateFamiliesNames(families, person);
        log.info("Person updated: {}", person);
    }

    @TransactionalInNeo4j
    public void deletePerson(UUID treeId, UUID personId) {
        treeService.ensureTreeExists(treeId);
        Person person = personService.findPersonById(personId);

        personService.deletePerson(person);
    }
}
