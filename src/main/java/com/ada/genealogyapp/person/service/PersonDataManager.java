package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.person.dto.params.*;
import com.ada.genealogyapp.person.repository.PersonRepository;
import com.ada.genealogyapp.query.QueryResultProcessor;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonDataManager implements PersonService {

    private final PersonRepository personRepository;

    private final QueryResultProcessor processor;

    @TransactionalInNeo4j
    public void savePerson(SavePersonParams params) {
        String result = personRepository.save(params.getUserId(), params.getTreeId(), params.getPersonId(), params.getPerson().getFirstname(), params.getPerson().getLastname(), params.getPerson().getGender().name());
        processor.process(result, Map.of(IdType.TREE_ID, params.getTreeId(), IdType.PERSON_ID, params.getPersonId()));
    }

    @TransactionalInNeo4j
    public void updatePerson(UpdatePersonParams params) {
        String result = personRepository.update(params.getUserId(), params.getTreeId(), params.getPersonId(), params.getPerson().getFirstname(), params.getPerson().getLastname(), params.getPerson().getGender().name());
        processor.process(result, Map.of(IdType.PERSON_ID, params.getPersonId()));
    }

    @TransactionalInNeo4j
    public void deletePerson(DeletePersonParams params) {
        String result = personRepository.delete(params.getUserId(), params.getTreeId(), params.getPersonId());
        processor.process(result, Map.of(IdType.PERSON_ID, params.getPersonId()));
    }

    @TransactionalInNeo4j
    public void addParentChildRelationship(AddParentChildRelationshipParams params) {
        personRepository.addParentChildRelationship(params.getUserId(), params.getTreeId(), params.getParentId(), params.getChildId(), params.getRelationshipType());
    }
}
