package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.type.StatusType;
import com.ada.genealogyapp.family.dto.params.DeleteFamilyParams;
import com.ada.genealogyapp.family.dto.params.UpdateFamilyParams;
import com.ada.genealogyapp.family.dto.params.UpdateFamilyRequestParams;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FamilyManagementServiceTest {

    @Mock
    FamilyService familyService;
    @Mock
    FamilyValidationService familyValidationService;
    @InjectMocks
    FamilyManagementService familyManagementService;

    @Test
    void updateFamily_shouldInvokeValidationAndService() {
        String userId = "user123";
        String treeId = "tree123";
        String familyId = "family123";
        StatusType status = StatusType.MARRIED;

        FamilyRequest familyRequest = FamilyRequest.builder()
                .status(status)
                .build();

        UpdateFamilyRequestParams requestParams = UpdateFamilyRequestParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .familyRequest(familyRequest)
                .build();

        Family family = Family.builder()
                .status(status)
                .build();

        UpdateFamilyParams params = UpdateFamilyParams.builder()
                .userId(requestParams.getUserId())
                .treeId(requestParams.getTreeId())
                .familyId(requestParams.getFamilyId())
                .family(family)
                .build();

        familyManagementService.updateFamily(requestParams);

        verify(familyValidationService).validateFamily(family);
        verify(familyService).updateFamily(params);
    }

    @Test
    void deleteFamily_shouldInvokeService() {
        String userId = "user123";
        String treeId = "tree123";
        String familyId = "family123";

        DeleteFamilyParams params = DeleteFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .build();

        familyManagementService.deleteFamily(params);

        verify(familyService).deleteFamily(params);
    }
}