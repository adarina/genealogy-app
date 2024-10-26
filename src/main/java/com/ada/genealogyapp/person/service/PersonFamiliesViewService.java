package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.family.relationship.FamilyRelationship;
import com.ada.genealogyapp.person.dto.PersonFamiliesResponse;
import com.ada.genealogyapp.person.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class PersonFamiliesViewService {

    private final PersonManagementService personManagementService;

    private final PersonSearchService personSearchService;

    public PersonFamiliesViewService(PersonManagementService personManagementService, PersonSearchService personSearchService) {
        this.personManagementService = personManagementService;
        this.personSearchService = personSearchService;
    }

    public PersonFamiliesResponse getPersonalFamilies(UUID treeId, UUID personId) {
        personManagementService.validateTreeAndPerson(treeId, personId);

        Person person = personSearchService.findPersonByIdOrThrowNodeNotFoundException(personId);

        Set<FamilyRelationship> familyRelationships = person.getFamilies();
        return PersonFamiliesResponse.entityToDtoMapper().apply(familyRelationships);
    }
}
