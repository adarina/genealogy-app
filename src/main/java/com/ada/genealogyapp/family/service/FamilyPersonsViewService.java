package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.dto.FamilyChildResponse;
import com.ada.genealogyapp.family.dto.FamilyParentResponse;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.person.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
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
        return nonNull(father) ? new FamilyParentResponse(father.getId(), father.getFirstname(), father.getLastname(), father.getBirthDate()) : null;
    }

    public FamilyParentResponse getMotherInformation(UUID treeId, UUID familyId) {
        Family family = familyManagementService.validateTreeAndFamily(treeId, familyId);
        Person mother = family.getMother();
        return nonNull(mother) ? new FamilyParentResponse(mother.getId(), mother.getFirstname(), mother.getLastname(), mother.getBirthDate()) : null;
    }

    public Set<FamilyChildResponse> getChildrenInformation(UUID treeId, UUID familyId) {
        Family family = familyManagementService.validateTreeAndFamily(treeId, familyId);
        Set<FamilyChildResponse> childrenInformation = new HashSet<>();

        for (Person child : family.getChildren()) {
            FamilyChildResponse familyChildResponse = new FamilyChildResponse(child.getId(), child.getFirstname(), child.getLastname(), child.getBirthDate(), child.getGenderType().toString());
            childrenInformation.add(familyChildResponse);
        }
        return childrenInformation;
    }
}
