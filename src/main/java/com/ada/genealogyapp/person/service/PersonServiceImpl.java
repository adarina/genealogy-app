package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PersonServiceImpl implements PersonService{

    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person findPersonByIdOrThrowNodeNotFoundException(UUID personId) {
        Optional<Person> person = personRepository.findById(personId);
        if (person.isPresent()) {
            log.info("Person found: {}", person.get());
        } else {
            log.error("No person found with id: {}", personId);
            throw new NodeNotFoundException("No person found with id: " + personId);
        }
        return person.get();
    }
}
