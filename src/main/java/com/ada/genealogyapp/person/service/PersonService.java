package com.ada.genealogyapp.person.service;


import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class PersonService {
    private final PersonRepository personRepository;

    private final PersonSearchService personSearchService;

    public PersonService(PersonRepository personRepository, PersonSearchService personSearchService) {
        this.personRepository = personRepository;
        this.personSearchService = personSearchService;
    }

    public Person create(Person person) {
        Person savedPerson = personRepository.save(person);
        log.info("Person created successfully: {}", savedPerson);
        return savedPerson;
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

}
