package com.ada.genealogyapp.person.service;


import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.person.dto.PersonFilterRequest;
import com.ada.genealogyapp.person.dto.PersonResponse;
import com.ada.genealogyapp.person.repostitory.PersonRepository;

import com.ada.genealogyapp.person.type.GenderType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public PersonViewService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Page<PersonResponse> getPersons(UUID treeId, String filter, Pageable pageable) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        PersonFilterRequest filterRequest = objectMapper.readValue(filter, PersonFilterRequest.class);

        String firstname = filterRequest.getFirstname();
        String lastname = filterRequest.getLastname();
        GenderType gender = null;

        if (nonNull(filterRequest.getGender())) {
            try {
                gender = GenderType.valueOf(filterRequest.getGender().toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Invalid gender value: {}", filterRequest.getGender());
            }
        }

        Page<PersonResponse> personsPage;
        if (nonNull(gender)) {
            personsPage = personRepository.findByTreeIdAndFirstnameContainingIgnoreCaseAndLastnameContainingIgnoreCaseAndGenderContaining(treeId, firstname != null ? firstname : "", lastname != null ? lastname : "", gender, pageable);
        } else {
            personsPage = personRepository.findByTreeIdAndFirstnameContainingIgnoreCaseAndLastnameContainingIgnoreCase(treeId, firstname != null ? firstname : "", lastname != null ? lastname : "", pageable);
        }
        return personsPage;
    }

    public PersonResponse getPerson(UUID treeId, UUID personId) {
        return personRepository.findPersonResponseByTreeIdAndPersonId(treeId, personId)
                .orElseThrow(() -> new NodeNotFoundException("Person " + personId.toString() + " not found for tree " + treeId.toString()));
    }
}
