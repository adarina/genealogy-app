package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.dto.params.CreateFamilyRequestParams;
import com.ada.genealogyapp.family.dto.params.SaveFamilyParams;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import com.ada.genealogyapp.family.model.Family;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class FamilyCreationService {

    private final FamilyService familyService;

    private final FamilyValidationService familyValidationService;

    @TransactionalInNeo4j
    public Family createFamily(CreateFamilyRequestParams params) {
        Family family = Family.builder()
                .status(params.getFamilyRequest().getStatus())
                .name("null & null")
                .build();
        familyValidationService.validateFamily(family);
        familyService.saveFamily(SaveFamilyParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .familyId(family.getId())
                .family(family)
                .build());
        return family;
    }
}