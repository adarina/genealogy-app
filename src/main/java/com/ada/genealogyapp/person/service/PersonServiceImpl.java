package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class PersonServiceImpl implements PersonService{

    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person findPersonById(UUID childId) {
        return personRepository.findById(childId)
                .orElseThrow(() -> new NodeNotFoundException("Person not found with ID: " + childId));
    }

    @TransactionalInNeo4j
    public void savePerson(Person person) {
        Person savedPerson = personRepository.save(person);
        log.info("Person saved successfully: {}", savedPerson);
    }

    @TransactionalInNeo4j
    public void deletePerson(Person person) {
        personRepository.delete(person);
        log.info("Person deleted successfully: {}", person.getId());
    }

    public void ensurePersonExists(UUID personId) {
        if (!personRepository.existsById(personId)) {
            throw new NodeNotFoundException("Family not found with ID: " + personId);
        }
    }
}
