package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.person.dto.PersonFilterRequest;
import com.ada.genealogyapp.person.dto.PersonResponse;
import com.ada.genealogyapp.person.repository.PersonRepository;

import com.ada.genealogyapp.query.QueryResultProcessor;
import com.ada.genealogyapp.tree.repository.TreeRepository;
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

    private final TreeRepository treeRepository;

    private final PersonService personService;

    private final QueryResultProcessor queryResultProcessor;

    public PersonViewService(PersonRepository personRepository, ObjectMapper objectMapper, TreeService treeService, TreeRepository treeRepository, PersonService personService, QueryResultProcessor queryResultProcessor) {
        this.personRepository = personRepository;
        this.objectMapper = objectMapper;
        this.treeService = treeService;
        this.treeRepository = treeRepository;
        this.personService = personService;
        this.queryResultProcessor = queryResultProcessor;
    }


    public Page<PersonResponse> getPersons(String userId, String treeId, String filter, Pageable pageable) throws JsonProcessingException {
        PersonFilterRequest filterRequest = objectMapper.readValue(filter, PersonFilterRequest.class);
        Page<PersonResponse> page = personRepository.find(
                userId, treeId, filterRequest.getFirstname(), filterRequest.getLastname(), filterRequest.getGender(), pageable
        );
        if (page.isEmpty()) {
            String result = treeRepository.checkTreeAndUserExistence(userId, treeId);
            queryResultProcessor.process(result, Map.of("userId", userId, "treeId", treeId));
        }
        return page;
    }

    public PersonResponse getPerson(String treeId, String personId) {
        return personService.findPersonResponseByTreeIdAndId(treeId, personId);
    }
}
