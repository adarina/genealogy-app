package com.ada.genealogyapp.person.service;


import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.person.dto.PersonFilterRequest;
import com.ada.genealogyapp.person.dto.PersonResponse;
import com.ada.genealogyapp.person.repostitory.PersonRepository;

import com.ada.genealogyapp.tree.service.TreeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.*;

@Service
@Slf4j
public class PersonViewService {


    private final PersonRepository personRepository;

    private final ObjectMapper objectMapper;

    private final TreeService treeService;

    private final PersonService personService;

    public PersonViewService(PersonRepository personRepository, ObjectMapper objectMapper, TreeService treeService, PersonService personService) {
        this.personRepository = personRepository;
        this.objectMapper = objectMapper;
        this.treeService = treeService;
        this.personService = personService;
    }

    public Page<PersonResponse> getPersons(UUID treeId, String filter, Pageable pageable) throws JsonProcessingException {
        PersonFilterRequest filterRequest = objectMapper.readValue(filter, PersonFilterRequest.class);
        treeService.ensureTreeExists(treeId);
        return personRepository.findByTreeIdAndFilteredFirstnameLastnameAndGender(
                treeId,
                Optional.ofNullable(filterRequest.getFirstname()).orElse(""),
                Optional.ofNullable(filterRequest.getLastname()).orElse(""),
                Optional.ofNullable(filterRequest.getGender()).orElse(""),
                pageable
        );
    }

    public PersonResponse getPerson(UUID treeId, UUID personId) {
        treeService.ensureTreeExists(treeId);
        personService.ensurePersonExists(personId);
        return personRepository.findByTreeIdAndPersonId(treeId, personId)
                .orElseThrow(() -> new NodeNotFoundException("Person " + personId.toString() + " not found for tree " + treeId.toString()));
    }
}
