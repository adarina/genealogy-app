package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



import java.util.*;

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

    public Page<Person> getPersonsByTreeId(UUID treeId, Pageable pageable) {
        Page<Person> personsPage = personRepository.findAllByTree_Id(treeId, pageable);

        if (!personsPage.isEmpty()) {
            log.info("Persons found for treeId {}: {}", treeId, personsPage.getContent());
        } else {
            log.error("No persons found for treeId: {}", treeId);
            throw new NodeNotFoundException("No persons found for treeId: " + treeId);
        }

        return personsPage;
    }



    public Optional<Person> findOptionalPersonByIdOrThrowNodeNotFoundException(UUID personId) {
        Optional<Person> person = personRepository.findById(personId);
        if (person.isPresent()) {
            log.info("Person found: {}", person.get());
            return person;
        } else {
            log.error("No person found with id: {}", personId);
            throw new NodeNotFoundException("No person found with id: " + personId);
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
