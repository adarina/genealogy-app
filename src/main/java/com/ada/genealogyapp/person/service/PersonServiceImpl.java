package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.person.dto.PersonResponse;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PersonServiceImpl implements PersonService{

    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person findPersonById(String personId) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new NodeNotFoundException("Person not found with ID: " + personId));
    }

    public PersonResponse findPersonResponseById(String personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new NodeNotFoundException("Person not found with ID: " + personId));
        return PersonResponse.builder()
                .id(person.getId())
                .firstname(person.getFirstname())
                .lastname(person.getLastname())
                .name(person.getName())
                .birthdate(person.getBirthdate())
                .gender(person.getGender())
                .build();
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

    public void ensurePersonExists(String personId) {
        if (!personRepository.existsById(personId)) {
            throw new NodeNotFoundException("Person not found with ID: " + personId);
        }
    }
}
