package com.ada.genealogyapp.person.service;


import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.person.dto.PersonFilterRequest;
import com.ada.genealogyapp.person.dto.PersonResponse;
import com.ada.genealogyapp.person.repostitory.PersonRepository;

import com.ada.genealogyapp.person.type.GenderType;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.*;

import static java.util.Objects.nonNull;

@Service
@Slf4j
public class PersonViewService {


    private final PersonRepository personRepository;

    private final ObjectMapper objectMapper;

    private final TreeRepository treeRepository;

    public PersonViewService(PersonRepository personRepository, ObjectMapper objectMapper, TreeRepository treeRepository) {
        this.personRepository = personRepository;
        this.objectMapper = objectMapper;
        this.treeRepository = treeRepository;
    }

    public Page<PersonResponse> getPersons(UUID treeId, String filter, Pageable pageable) throws JsonProcessingException {
        PersonFilterRequest filterRequest = objectMapper.readValue(filter, PersonFilterRequest.class);
        treeRepository.findById(treeId).orElseThrow(() ->
                new EntityNotFoundException("Tree with ID " + treeId + " not found"));
        return personRepository.findByTreeIdAndFilteredFirstnameLastnameAndGender(
                treeId,
                Optional.ofNullable(filterRequest.getFirstname()).orElse(""),
                Optional.ofNullable(filterRequest.getLastname()).orElse(""),
                Optional.ofNullable(filterRequest.getGender()).orElse(""),
                pageable
        );
    }

    public PersonResponse getPerson(UUID treeId, UUID personId) {
        return personRepository.findPersonResponseByTreeIdAndPersonId(treeId, personId)
                .orElseThrow(() -> new NodeNotFoundException("Person " + personId.toString() + " not found for tree " + treeId.toString()));
    }
}
