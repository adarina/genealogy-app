package com.ada.genealogyapp.person.service;


import com.ada.genealogyapp.person.Gender;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import com.ada.genealogyapp.tree.service.TreeSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

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


    public boolean deletePersonById(UUID id) {
        Optional<Person> person = personSearchService.find(id);
        if (person.isPresent()) {
            delete(person.get());
            return true;
        } else {
            log.warn("Person with Id {} not found", id);
            return false;
        }
    }

    public void delete(Person person) {
        personRepository.delete(person);
        log.info("Person with id {} deleted successfully", person);
    }

    public void updatePerson(UUID treeId, UUID personId, PersonRequest personRequest) {

        treeSearchService.findTreeById(treeId);
        Person existingPerson = personSearchService.findPersonById(personId);

        updateFirstname(existingPerson, personRequest.getFirstname());
        updateLastname(existingPerson, personRequest.getLastname());
        updateBirthDate(existingPerson, personRequest.getBirthDate());
        updateGender(existingPerson, personRequest.getGender());


        Person updatedPerson = personRepository.save(existingPerson);
        log.info("Person updated successfully: {}", updatedPerson.getId());
    }

    private void updateFirstname(Person person, String firstname) {
        if (firstname != null) {
            person.setFirstname(firstname);
        }
    }

    private void updateLastname(Person person, String lastname) {
        if (lastname != null) {
            person.setLastname(lastname);
        }
    }

    private void updateBirthDate(Person person, LocalDate birthDate) {
        if (birthDate != null) {
            person.setBirthDate(birthDate);
        }
    }

    private void updateGender(Person person, Gender gender) {
        if (gender != null) {
            person.setGender(gender);
        }
    }
}
