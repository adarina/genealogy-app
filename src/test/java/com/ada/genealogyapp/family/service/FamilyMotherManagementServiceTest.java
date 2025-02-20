package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FamilyMotherManagementServiceTest {

    @Mock
    private FamilyService familyService;

    @InjectMocks
    private FamilyMotherManagementService familyMotherManagementService;


    @Test
    void addMotherToFamily_shouldAddMotherToFamily() {
        String userId = "user123";
        String treeId = "tree123";
        String familyId = "family123";
        String personId = "person123";

        doNothing().when(familyService).addMotherToFamily(userId, treeId, familyId, personId);

        assertDoesNotThrow(() ->
                familyMotherManagementService.addMotherToFamily(userId, treeId, familyId, personId));

        verify(familyService, times(1)).addMotherToFamily(userId, treeId, familyId, personId);
    }

    @Test
    void addMotherToFamily_shouldThrowExceptionWhenFamilyNotFound() {
        String userId = "user123";
        String treeId = "tree123";
        String familyId = "family123";
        String personId = "person123";

        doThrow(new NodeNotFoundException("Family not found with ID: " + familyId))
                .when(familyService).addMotherToFamily(userId, treeId, familyId, personId);


        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () ->
                familyMotherManagementService.addMotherToFamily(userId, treeId, familyId, personId));
        assertEquals("Family not found with ID: family123", exception.getMessage());
        verify(familyService, times(1)).addMotherToFamily(userId, treeId, familyId, personId);
    }

    @Test
    void addMotherToFamily_shouldThrowExceptionWhenPersonNotFound() {
        String userId = "user123";
        String treeId = "tree123";
        String familyId = "family123";
        String personId = "person123";

        doThrow(new NodeNotFoundException("Person not found with ID: " + personId))
                .when(familyService).addMotherToFamily(userId, treeId, familyId, personId);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () ->
                familyMotherManagementService.addMotherToFamily(userId, treeId, familyId, personId));
        assertEquals("Person not found with ID: person123", exception.getMessage());
        verify(familyService, times(1)).addMotherToFamily(userId, treeId, familyId, personId);
    }

    @Test
    void addMotherToFamily_shouldThrowExceptionWhenTreeNotFound() {
        String userId = "user123";
        String treeId = "tree123";
        String familyId = "family123";
        String personId = "person123";

        doThrow(new NodeNotFoundException("Tree not found with ID: " + treeId))
                .when(familyService).addMotherToFamily(userId, treeId, familyId, personId);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () ->
                familyMotherManagementService.addMotherToFamily(userId, treeId, familyId, personId));
        assertEquals("Tree not found with ID: tree123", exception.getMessage());
        verify(familyService, times(1)).addMotherToFamily(userId, treeId, familyId, personId);
    }

    @Test
    void addMotherToFamily_shouldThrowExceptionWhenUserNotFound() {
        String userId = "user123";
        String treeId = "tree123";
        String familyId = "family123";
        String personId = "person123";

        doThrow(new NodeNotFoundException("User not found with ID: " + userId))
                .when(familyService).addMotherToFamily(userId, treeId, familyId, personId);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () ->
                familyMotherManagementService.addMotherToFamily(userId, treeId, familyId, personId));
        assertEquals("User not found with ID: user123", exception.getMessage());
        verify(familyService, times(1)).addMotherToFamily(userId, treeId, familyId, personId);
    }
}
