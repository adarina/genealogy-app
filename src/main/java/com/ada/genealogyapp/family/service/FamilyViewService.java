package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.dto.FamilyResponse;
import com.ada.genealogyapp.family.model.Family;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Objects.nonNull;

@Service
@Slf4j
public class FamilyViewService {

    private final FamilySearchService familySearchService;

    public FamilyViewService(FamilySearchService familySearchService) {
        this.familySearchService = familySearchService;
    }

    public List<FamilyResponse> getFamilies(UUID treeId) {
        List<Family> families = familySearchService.getFamiliesByTreeIdOrThrowNodeNotFoundException(treeId);
        List<FamilyResponse> familyResponses = new ArrayList<>();

        for (Family family : families) {
            FamilyResponse response = new FamilyResponse();
            response.setId(family.getId());

            if (nonNull(family.getFather())) {
                response.setFatherFirstname(family.getFather().getFirstname());
                response.setFatherLastname(family.getFather().getLastname());
                response.setFatherBirthDate(family.getFather().getBirthDate());
            }
            if (nonNull(family.getMother())) {
                response.setMotherFirstname(family.getMother().getFirstname());
                response.setMotherLastname(family.getMother().getLastname());
                response.setMotherBirthDate(family.getMother().getBirthDate());
            }
            response.setFamilyRelationshipType(family.getFamilyRelationshipType());
            familyResponses.add(response);
        }
        return familyResponses;
    }
}
