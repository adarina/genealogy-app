package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import com.ada.genealogyapp.user.model.User;
;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PersonSearchService {

    private final PersonRepository personRepository;

    public PersonSearchService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Optional<Person> find(UUID id) {
        Optional<Person> person = personRepository.findById(id);
        if (person.isPresent()) {
            log.info("Person found: {}", person.get());
        } else {
            log.warn("No person found with ID: {}", id);
        }
        return person;
    }

    public Person findPersonById(UUID personId) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new NodeNotFoundException("No person found with ID: " + personId));
    }
}
