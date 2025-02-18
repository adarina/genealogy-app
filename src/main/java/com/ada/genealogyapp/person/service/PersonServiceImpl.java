package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.person.dto.PersonResponse;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import com.ada.genealogyapp.query.QueryResultProcessor;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    private final QueryResultProcessor queryResultProcessor;

    public PersonServiceImpl(PersonRepository personRepository, QueryResultProcessor queryResultProcessor) {
        this.personRepository = personRepository;
        this.queryResultProcessor = queryResultProcessor;
    }

    public Person findPersonById(String personId) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new NodeNotFoundException("Person not found with ID: " + personId));
    }

    public PersonResponse findPersonResponseByTreeIdAndId(String treeId, String personId) {
        return personRepository.findByTreeIdAndId(treeId, personId)
                .orElseThrow(() -> new NodeNotFoundException("Person not found with ID: " + personId));
    }

    @TransactionalInNeo4j
    public void savePerson(Person person) {
        String result = personRepository.save(person.getTree().getId(), person.getId(), person.getFirstname(), person.getLastname(), person.getGender().name());
        queryResultProcessor.process(result, Map.of("treeId", person.getTree().getId(), "personId", person.getId()));
    }

    @TransactionalInNeo4j
    public void updatePerson(String treeId, String personId, Person person) {
        String result = personRepository.update(treeId, personId, person.getFirstname(), person.getLastname(), person.getGender().name());
        queryResultProcessor.process(result, Map.of("personId", person.getId()));
    }

    @TransactionalInNeo4j
    public void deletePerson(String treeId, String personId) {
        String result = personRepository.delete(treeId, personId);
        queryResultProcessor.process(result, Map.of("personId", personId));
    }

    @TransactionalInNeo4j
    public void addParentChildRelationship(String treeId, String personId, String childId, String relationshipType) {
        personRepository.addParentChildRelationship(treeId, personId, childId, relationshipType);
    }

    public void ensurePersonExists(String personId) {
        if (!personRepository.existsById(personId)) {
            throw new NodeNotFoundException("Person not found with ID: " + personId);
        }
    }


    public Person findPersonByTreeIdAndPersonId(String treeId, String personId) {
        return personRepository.findPersonByTreeIdAndPersonId(treeId, personId);
    }

}
