package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class PersonSearchService {

    private final PersonRepository personRepository;

    public PersonSearchService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person findPersonByIdOrThrowNodeNotFoundException(UUID personId) {
        Optional<Person> person = personRepository.findById(personId);
        if (person.isPresent()) {
            log.info("Person found: {}", person.get());
            return person.get();
        } else {
            log.error("No person found with id: {}", personId);
            throw new NodeNotFoundException("No person found with id: " + personId);
        }
    }

    public Person findPersonById(UUID personId) {
        Optional<Person> person = personRepository.findById(personId);
        if (person.isPresent()) {
            log.info("Person found: {}", person.get());
            return person.get();
        } else {
            log.error("No person found with id: {}", personId);
            return null;
        }
    }

    public Set<Person> findPersonsByIds(Set<UUID> personIds) {
        Set<Person> persons = new HashSet<>();
        for (UUID personId : personIds) {
            Optional<Person> person = personRepository.findById(personId);
            if (person.isPresent()) {
                persons.add(person.get());
            } else {
                log.error("No person found with id: {}", personId);
            }
        }
        return persons;
    }
}
