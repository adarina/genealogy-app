package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.family.relationship.FamilyRelationship;


import com.ada.genealogyapp.person.dto.PersonFamiliesResponse;
import com.ada.genealogyapp.person.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PersonFamiliesViewService {

    private final PersonManagementService personManagementService;

    private final PersonSearchService personSearchService;

    public PersonFamiliesViewService(PersonManagementService personManagementService, PersonSearchService personSearchService) {
        this.personManagementService = personManagementService;
        this.personSearchService = personSearchService;
    }

    public Page<PersonFamiliesResponse> getPersonalFamilies(UUID treeId, UUID personId, Pageable pageable) {
        personManagementService.validateTreeAndPerson(treeId, personId);

        Person person = personSearchService.findPersonByIdOrThrowNodeNotFoundException(personId);

        Set<FamilyRelationship> familyRelationships = person.getFamilies();

        List<PersonFamiliesResponse> familiesResponses = familyRelationships.stream()
                .map(familyRelationship -> {
                    PersonFamiliesResponse response = new PersonFamiliesResponse();
                    response.setId(familyRelationship.getFamily().getId());
                    response.setFatherId(familyRelationship.getFamily().getFather().getId());
                    response.setFatherName(familyRelationship.getFamily().getFather().getName());
                    response.setFatherBirthdate(familyRelationship.getFamily().getFather().getBirthdate());
                    response.setMotherId(familyRelationship.getFamily().getMother().getId());
                    response.setMotherName(familyRelationship.getFamily().getMother().getName());
                    response.setMotherBirthdate(familyRelationship.getFamily().getMother().getBirthdate());

                    List<PersonFamiliesResponse.Child> children = familyRelationship.getFamily().getChildren().stream()
                            .sorted(Comparator.comparing(Person::getBirthdate))
                            .map(child -> new PersonFamiliesResponse.Child(child.getId(), child.getName(), child.getBirthdate()))
                            .collect(Collectors.toList());
                    response.setChildren(children);

                    return response;
                }).collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), familiesResponses.size());
        List<PersonFamiliesResponse> paginatedFamilies = familiesResponses.subList(start, end);

        return new PageImpl<>(paginatedFamilies, pageable, familiesResponses.size());
    }
}