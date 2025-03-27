package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.dto.params.DeleteFamilyParams;
import com.ada.genealogyapp.family.dto.params.UpdateFamilyParams;
import com.ada.genealogyapp.family.dto.params.UpdateFamilyRequestParams;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FamilyManagementService {

    private final FamilyService familyService;

    private final FamilyValidationService familyValidationService;


    @TransactionalInNeo4j
    public void updateFamily(UpdateFamilyRequestParams params) {
        Family family = Family.builder()
                .status(params.getFamilyRequest().getStatus())
                .build();

        familyValidationService.validateFamily(family);
        familyService.updateFamily(UpdateFamilyParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .familyId(params.getFamilyId())
                .family(family)
                .build());
    }

    @TransactionalInNeo4j
    public void deleteFamily(DeleteFamilyParams params) {
        familyService.deleteFamily(params);
    }
}
