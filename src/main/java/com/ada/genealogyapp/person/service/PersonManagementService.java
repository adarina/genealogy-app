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
    public void updatePerson(String userId, String treeId, String personId, @NonNull PersonRequest personRequest) {

        Person person = Person.builder()
                .firstname(personRequest.getFirstname())
                .lastname(personRequest.getLastname())
                .gender(personRequest.getGender())
                .build();
        personValidationService.validatePerson(person);
        personService.updatePerson(userId, treeId, personId, person);
    }

    @TransactionalInNeo4j
    public void deletePerson(String userId, String treeId, String personId) {
        personService.deletePerson(userId, treeId, personId);
    }
}
