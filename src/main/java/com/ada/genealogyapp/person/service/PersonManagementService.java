package com.ada.genealogyapp.person.service;


import com.ada.genealogyapp.person.type.GenderType;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import com.ada.genealogyapp.tree.service.TreeSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
@Slf4j
public class PersonManagementService {
    private final PersonRepository personRepository;

    private final PersonSearchService personSearchService;

    private final TreeSearchService treeSearchService;

    public PersonManagementService(PersonRepository personRepository, PersonSearchService personSearchService, TreeSearchService treeSearchService) {
        this.personRepository = personRepository;
        this.personSearchService = personSearchService;
        this.treeSearchService = treeSearchService;
    }

    public void updatePerson(UUID treeId, UUID personId, PersonRequest personRequest) {

        treeSearchService.findTreeByIdOrThrowNodeNotFoundException(treeId);
        Person person = personSearchService.findPersonByIdOrThrowNodeNotFoundException(personId);

        updateFirstname(person, personRequest.getFirstname());
        updateLastname(person, personRequest.getLastname());
        updateBirthDate(person, personRequest.getBirthDate());
        updateGender(person, personRequest.getGenderType());

        Person updatedPerson = personRepository.save(person);
        log.info("Person updated successfully: {}", updatedPerson.getId());
    }

    private void updateFirstname(Person person, String firstname) {
        if (nonNull(firstname)) {
            person.setFirstname(firstname);
        }
    }

    private void updateLastname(Person person, String lastname) {
        if (nonNull(lastname)) {
            person.setLastname(lastname);
        }
    }

    private void updateBirthDate(Person person, LocalDate birthDate) {
        if (nonNull(birthDate)) {
            person.setBirthDate(birthDate);
        }
    }

    private void updateGender(Person person, GenderType genderType) {
        if (nonNull(genderType)) {
            person.setGenderType(genderType);
        }
    }
}
