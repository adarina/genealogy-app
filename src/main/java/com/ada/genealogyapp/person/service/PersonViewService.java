package com.ada.genealogyapp.person.service;


import com.ada.genealogyapp.person.dto.PersonFilterRequest;
import com.ada.genealogyapp.person.dto.PersonExportResponse;
import com.ada.genealogyapp.person.dto.PersonResponse;
import com.ada.genealogyapp.person.dto.PersonRelationshipExportResponse;
import com.ada.genealogyapp.person.dto.params.GetPersonParams;
import com.ada.genealogyapp.person.dto.params.GetPersonsParams;
import com.ada.genealogyapp.person.repository.PersonRepository;
import com.ada.genealogyapp.tree.dto.params.BaseParams;
import com.ada.genealogyapp.tree.service.TreeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class PersonViewService {

    private final ObjectMapper objectMapper;

    private final TreeService treeService;

    private final PersonRepository personRepository;

    public Page<PersonResponse> getPersons(GetPersonsParams params) throws JsonProcessingException {
        PersonFilterRequest filterRequest = objectMapper.readValue(params.getFilter(), PersonFilterRequest.class);
        Page<PersonResponse> page = personRepository.find(params.getUserId(), params.getTreeId(), filterRequest.getFirstname(), filterRequest.getLastname(), filterRequest.getGender(), params.getPageable());
        treeService.ensureUserAndTreeExist(params, page);
        return page;
    }

    public PersonResponse getPerson(GetPersonParams params) {
        PersonResponse personResponse = personRepository.find(params.getUserId(), params.getTreeId(), params.getPersonId());
        treeService.ensureUserAndTreeExist(params, personResponse);
        return personResponse;
    }

    public Set<PersonExportResponse> findPersons(BaseParams params) {
        return personRepository.find(params.getUserId(), params.getTreeId()).stream()
                .map(person -> PersonExportResponse.builder()
                        .id(person.getId())
                        .firstname(person.getFirstname())
                        .lastname(person.getLastname())
                        .gender(person.getGender())
                        .name(person.getName())
                        .relationships(personRepository.findRelationships(params.getUserId(), params.getTreeId(), person.getId()).stream()
                                .map(rel -> PersonRelationshipExportResponse.builder()
                                        .childId(rel.getChildId())
                                        .relationship(rel.getRelationship())
                                        .build())
                                .collect(Collectors.toSet()))
                        .build())
                .collect(Collectors.toSet());
    }
}
