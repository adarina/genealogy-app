package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.exceptions.ValidationException;
import com.ada.genealogyapp.family.type.StatusType;
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
class FamilyManagementServiceTest {

    @Mock
    TreeService treeService;

    @Mock
    FamilyService familyService;

    @Mock
    FamilyValidationService familyValidationService;

    @InjectMocks
    FamilyManagementService familyManagementService;


    String treeId;
    String familyId;
    Family family;
    FamilyRequest familyRequest;

    @BeforeEach
    void setUp() {

        treeId = String.valueOf(UUID.randomUUID());
        familyId = String.valueOf(UUID.randomUUID());
        family = new Family();
        familyRequest = new FamilyRequest(StatusType.MARRIED);
    }

    @Test
    void updateFamily_shouldUpdateFamilyWhenValidRequest() {

        doNothing().when(treeService).ensureTreeExists(treeId);
        when(familyService.findFamilyById(familyId)).thenReturn(family);

        familyManagementService.updateFamily(treeId, familyId, familyRequest);

        assertEquals(StatusType.MARRIED, family.getStatus());
        verify(treeService).ensureTreeExists(treeId);
        verify(familyService).findFamilyById(familyId);
        verify(familyValidationService).validateFamily(family);
        verify(familyService).saveFamily(family);
    }

    @Test
    void updateFamily_shouldThrowExceptionWhenTreeDoesNotExist() {

        doThrow(new NodeNotFoundException("Tree not found"))
                .when(treeService).ensureTreeExists(treeId);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () ->
                familyManagementService.updateFamily(treeId, familyId, familyRequest)
        );

        assertEquals("Tree not found", exception.getMessage());
        verify(treeService).ensureTreeExists(treeId);
        verifyNoInteractions(familyValidationService);
        verifyNoInteractions(familyService);
    }

    @Test
    void updateFamily_shouldThrowExceptionWhenFamilyDoesNotExist() {

        doNothing().when(treeService).ensureTreeExists(treeId);
        when(familyService.findFamilyById(familyId))
                .thenThrow(new NodeNotFoundException("Family not found"));

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () ->
                familyManagementService.updateFamily(treeId, familyId, familyRequest)
        );

        assertEquals("Family not found", exception.getMessage());
        verify(treeService).ensureTreeExists(treeId);
        verify(familyService).findFamilyById(familyId);
        verifyNoMoreInteractions(familyValidationService);
        verifyNoMoreInteractions(familyService);
    }

    @Test
    void updateFamily_shouldNotSaveFamilyWhenValidationFails() {

        doNothing().when(treeService).ensureTreeExists(treeId);
        when(familyService.findFamilyById(familyId)).thenReturn(family);
        doThrow(new ValidationException("Family validation failed"))
                .when(familyValidationService).validateFamily(family);

        ValidationException exception = assertThrows(ValidationException.class, () ->
                familyManagementService.updateFamily(treeId, familyId, familyRequest)
        );

        assertEquals("Family validation failed", exception.getMessage());
        verify(treeService).ensureTreeExists(treeId);
        verify(familyService).findFamilyById(familyId);
        verify(familyValidationService).validateFamily(family);
        verify(familyService, never()).saveFamily(any());
    }


    @Test
    void deleteFamily_shouldDeleteFamilyWhenTreeAndFamilyExist() {

        doNothing().when(treeService).ensureTreeExists(treeId);
        when(familyService.findFamilyById(familyId)).thenReturn(family);
        doNothing().when(familyService).deleteFamily(family);

        familyManagementService.deleteFamily(treeId, familyId);

        verify(treeService).ensureTreeExists(treeId);
        verify(familyService).findFamilyById(familyId);
        verify(familyService).deleteFamily(family);
    }


    @Test
    void deleteFamily_shouldThrowExceptionWhenTreeDoesNotExist() {

        doThrow(new NodeNotFoundException("Tree not found"))
                .when(treeService).ensureTreeExists(treeId);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () ->
                familyManagementService.deleteFamily(treeId, familyId)
        );

        assertEquals("Tree not found", exception.getMessage());
        verify(treeService).ensureTreeExists(treeId);
        verifyNoInteractions(familyService);
    }

    @Test
    void deleteFamily_shouldThrowExceptionWhenFamilyDoesNotExist() {

        doNothing().when(treeService).ensureTreeExists(treeId);
        when(familyService.findFamilyById(familyId))
                .thenThrow(new NodeNotFoundException("Family not found"));

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () ->
                familyManagementService.deleteFamily(treeId, familyId)
        );

        assertEquals("Family not found", exception.getMessage());
        verify(treeService).ensureTreeExists(treeId);
        verify(familyService).findFamilyById(familyId);
        verify(familyService, never()).deleteFamily(any());
    }
}