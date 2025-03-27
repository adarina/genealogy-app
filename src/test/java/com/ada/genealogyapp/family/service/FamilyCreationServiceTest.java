package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.dto.params.CreateFamilyRequestParams;
import com.ada.genealogyapp.family.dto.params.SaveFamilyParams;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.type.StatusType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FamilyCreationServiceTest {


    @Mock
    private FamilyService familyService;

    @Mock
    private FamilyValidationService familyValidationService;

    @InjectMocks
    private FamilyCreationService familyCreationService;

    @Test
    void createFamily_shouldBuildValidateAndSaveFamily() {
        String userId = "user123";
        String treeId = "tree123";
        FamilyRequest familyRequest = FamilyRequest.builder()
                .status(StatusType.MARRIED)
                .build();

        CreateFamilyRequestParams createParams = CreateFamilyRequestParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyRequest(familyRequest)
                .build();

        doNothing().when(familyValidationService).validateFamily(any(Family.class));
        doNothing().when(familyService).saveFamily(any(SaveFamilyParams.class));

        Family createdFamily = familyCreationService.createFamily(createParams);

        assertNotNull(createdFamily);
        assertEquals(StatusType.MARRIED, createdFamily.getStatus());
        assertEquals("null & null", createdFamily.getName());

        verify(familyValidationService).validateFamily(any(Family.class));
        verify(familyService).saveFamily(any(SaveFamilyParams.class));
    }
}
