package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.exceptions.ValidationException;
import com.ada.genealogyapp.family.type.StatusType;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TreeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FamilyCreationServiceTest {

    @Mock
    TreeService treeService;

    @Mock
    FamilyService familyService;

    @Mock
    FamilyValidationService familyValidationService;

    @InjectMocks
    FamilyCreationService familyCreationService;


    Tree tree;
    UUID treeId;
    FamilyRequest familyRequest;

    @BeforeEach
    void setUp() {

        treeId = UUID.randomUUID();
        tree = new Tree();
        familyRequest = new FamilyRequest(StatusType.MARRIED);
    }

    @Test
    void createFamily_shouldCreateFamilyWhenValidRequest() {

        when(treeService.findTreeById(treeId)).thenReturn(tree);

        Family family = familyCreationService.createFamily(treeId, familyRequest);

        assertEquals(StatusType.MARRIED, family.getStatus());

        verify(treeService).findTreeById(treeId);
        verify(familyValidationService).validateFamily(family);
        verify(familyService).saveFamily(family);
    }

    @Test
    void createFamily_shouldThrowExceptionWhenTreeDoesNotExist() {

        doThrow(new NodeNotFoundException("Tree not found"))
                .when(treeService).findTreeById(treeId);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () ->
                familyCreationService.createFamily(treeId, familyRequest)
        );

        assertEquals("Tree not found", exception.getMessage());
        verify(treeService).findTreeById(treeId);
        verifyNoInteractions(familyValidationService);
        verifyNoInteractions(familyService);
    }

    @Test
    void createFamily_shouldNotSaveFamilyWhenValidationFails() {

        when(treeService.findTreeById(treeId)).thenReturn(tree);
        doThrow(new ValidationException("Family validation failed"))
                .when(familyValidationService).validateFamily(any(Family.class));

        ValidationException exception = assertThrows(ValidationException.class, () ->
                familyCreationService.createFamily(treeId, familyRequest)
        );

        assertEquals("Family validation failed", exception.getMessage());
        verify(treeService).findTreeById(treeId);
        verify(familyValidationService).validateFamily(any(Family.class));
        verify(familyService, never()).saveFamily(any());
    }
}