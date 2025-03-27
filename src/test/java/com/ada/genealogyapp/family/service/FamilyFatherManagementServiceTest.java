package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.family.dto.params.AddPersonToFamilyParams;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FamilyFatherManagementServiceTest {

    @Mock
    private FamilyService familyService;

    @InjectMocks
    private FamilyFatherManagementService familyFatherManagementService;


    @Test
    void addFatherToFamily_shouldAddFatherToFamily() {
        String userId = "user123";
        String treeId = "tree123";
        String familyId = "family123";
        String personId = "person123";
        AddPersonToFamilyParams params = AddPersonToFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .personId(personId)
                .build();

        doNothing().when(familyService).addFatherToFamily(params);

        assertDoesNotThrow(() ->
                familyFatherManagementService.addFatherToFamily(params));
        verify(familyService, times(1)).addFatherToFamily(params);
    }

    @Test
    void addFatherToFamily_shouldThrowExceptionWhenFamilyNotFound() {
        String userId = "user123";
        String treeId = "tree123";
        String familyId = "family123";
        String personId = "person123";
        AddPersonToFamilyParams params = AddPersonToFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .personId(personId)
                .build();

        doThrow(new NodeNotFoundException("Family not found with ID: " + familyId))
                .when(familyService).addFatherToFamily(params);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () ->
                familyFatherManagementService.addFatherToFamily(params));
        assertEquals("Family not found with ID: family123", exception.getMessage());
        verify(familyService, times(1)).addFatherToFamily(params);
    }

    @Test
    void addFatherToFamily_shouldThrowExceptionWhenPersonNotFound() {
        String userId = "user123";
        String treeId = "tree123";
        String familyId = "family123";
        String personId = "person123";
        AddPersonToFamilyParams params = AddPersonToFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .personId(personId)
                .build();

        doThrow(new NodeNotFoundException("Person not found with ID: " + personId))
                .when(familyService).addFatherToFamily(params);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () ->
                familyFatherManagementService.addFatherToFamily(params));
        assertEquals("Person not found with ID: person123", exception.getMessage());
        verify(familyService, times(1)).addFatherToFamily(params);
    }

    @Test
    void addFatherToFamily_shouldThrowExceptionWhenTreeNotFound() {
        String userId = "user123";
        String treeId = "tree123";
        String familyId = "family123";
        String personId = "person123";
        AddPersonToFamilyParams params = AddPersonToFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .personId(personId)
                .build();

        doThrow(new NodeNotFoundException("Tree not found with ID: " + treeId))
                .when(familyService).addFatherToFamily(params);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () ->
                familyFatherManagementService.addFatherToFamily(params));
        assertEquals("Tree not found with ID: tree123", exception.getMessage());
        verify(familyService, times(1)).addFatherToFamily(params);
    }

    @Test
    void addFatherToFamily_shouldThrowExceptionWhenUserNotFound() {
        String userId = "user123";
        String treeId = "tree123";
        String familyId = "family123";
        String personId = "person123";
        AddPersonToFamilyParams params = AddPersonToFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .personId(personId)
                .build();

        doThrow(new NodeNotFoundException("User not found with ID: " + userId))
                .when(familyService).addFatherToFamily(params);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () ->
                familyFatherManagementService.addFatherToFamily(params));
        assertEquals("User not found with ID: user123", exception.getMessage());
        verify(familyService, times(1)).addFatherToFamily(params);
    }
}
