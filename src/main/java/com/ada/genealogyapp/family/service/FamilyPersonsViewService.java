package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.dto.FamilyChildrenResponse;
import com.ada.genealogyapp.family.dto.FamilyParentResponse;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.person.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
@Slf4j
public class FamilyPersonsViewService {

    private final FamilyManagementService familyManagementService;

    public FamilyPersonsViewService(FamilyManagementService familyManagementService) {
        this.familyManagementService = familyManagementService;
    }

    public FamilyParentResponse getFatherInformation(UUID treeId, UUID familyId) {
        Family family = familyManagementService.validateTreeAndFamily(treeId, familyId);
        Person father = family.getFather();
        return nonNull(father) ? new FamilyParentResponse(father.getId(), father.getFirstname(), father.getLastname(), father.getBirthdate()) : null;
    }

    public FamilyParentResponse getMotherInformation(UUID treeId, UUID familyId) {
        Family family = familyManagementService.validateTreeAndFamily(treeId, familyId);
        Person mother = family.getMother();
        return nonNull(mother) ? new FamilyParentResponse(mother.getId(), mother.getFirstname(), mother.getLastname(), mother.getBirthdate()) : null;
    }

    public FamilyChildrenResponse getChildrenInformation(UUID treeId, UUID familyId) {
        Family family = familyManagementService.validateTreeAndFamily(treeId, familyId);
        return FamilyChildrenResponse.entityToDtoMapper().apply(family.getChildren());
    }
}
