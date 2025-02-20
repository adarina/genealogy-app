package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.type.StatusType;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TreeService;
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
    private TreeService treeService;

    @Mock
    private FamilyService familyService;

    @Mock
    private FamilyValidationService familyValidationService;

    @InjectMocks
    private FamilyCreationService familyCreationService;

    @Test
    void createFamily_shouldValidateAndSaveFamily() {
        String userId = "user123";
        String treeId = "tree123";
        FamilyRequest familyRequest = new FamilyRequest();
        familyRequest.setStatus(StatusType.MARRIED);

        Family family = Family.builder()
                .status(StatusType.MARRIED)
                .name("null & null")
                .build();

        Family createdFamily = familyCreationService.createFamily(userId, treeId, familyRequest);

        assertNotNull(createdFamily);
        assertEquals(StatusType.MARRIED, createdFamily.getStatus());
        assertEquals("null & null", createdFamily.getName());

        verify(familyValidationService).validateFamily(createdFamily);
        verify(familyService).saveFamily(userId, treeId, createdFamily);
    }

    @Test
    void createFamily_withTreeAndStatus_shouldValidateAndSaveFamily() {
        String userId = "user123";
        Tree tree = Tree.builder().id("tree123").build();
        StatusType status = StatusType.UNKNOWN;

        Family family = Family.builder()
                .status(status)
                .name("null & null")
                .build();

        Family createdFamily = familyCreationService.createFamily(userId, tree, status);

        assertNotNull(createdFamily);
        assertEquals(status, createdFamily.getStatus());
        assertEquals("null & null", createdFamily.getName());

        verify(familyValidationService).validateFamily(createdFamily);
        verify(familyService).saveFamily(userId, tree.getId(), createdFamily);
    }
}
