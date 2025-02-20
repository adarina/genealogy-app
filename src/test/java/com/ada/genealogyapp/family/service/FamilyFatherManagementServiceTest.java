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

        doNothing().when(familyService).addFatherToFamily(userId, treeId, familyId, personId);

        assertDoesNotThrow(() ->
                familyFatherManagementService.addFatherToFamily(userId, treeId, familyId, personId));

        verify(familyService, times(1)).addFatherToFamily(userId, treeId, familyId, personId);
    }

    @Test
    void addFatherToFamily_shouldThrowExceptionWhenFamilyNotFound() {
        String userId = "user123";
        String treeId = "tree123";
        String familyId = "family123";
        String personId = "person123";

        doThrow(new NodeNotFoundException("Family not found with ID: " + familyId))
                .when(familyService).addFatherToFamily(userId, treeId, familyId, personId);


        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () ->
                familyFatherManagementService.addFatherToFamily(userId, treeId, familyId, personId));
        assertEquals("Family not found with ID: family123", exception.getMessage());
        verify(familyService, times(1)).addFatherToFamily(userId, treeId, familyId, personId);
    }

    @Test
    void addFatherToFamily_shouldThrowExceptionWhenPersonNotFound() {
        String userId = "user123";
        String treeId = "tree123";
        String familyId = "family123";
        String personId = "person123";

        doThrow(new NodeNotFoundException("Person not found with ID: " + personId))
                .when(familyService).addFatherToFamily(userId, treeId, familyId, personId);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () ->
                familyFatherManagementService.addFatherToFamily(userId, treeId, familyId, personId));
        assertEquals("Person not found with ID: person123", exception.getMessage());
        verify(familyService, times(1)).addFatherToFamily(userId, treeId, familyId, personId);
    }

    @Test
    void addFatherToFamily_shouldThrowExceptionWhenTreeNotFound() {
        String userId = "user123";
        String treeId = "tree123";
        String familyId = "family123";
        String personId = "person123";

        doThrow(new NodeNotFoundException("Tree not found with ID: " + treeId))
                .when(familyService).addFatherToFamily(userId, treeId, familyId, personId);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () ->
                familyFatherManagementService.addFatherToFamily(userId, treeId, familyId, personId));
        assertEquals("Tree not found with ID: tree123", exception.getMessage());
        verify(familyService, times(1)).addFatherToFamily(userId, treeId, familyId, personId);
    }

    @Test
    void addFatherToFamily_shouldThrowExceptionWhenUserNotFound() {
        String userId = "user123";
        String treeId = "tree123";
        String familyId = "family123";
        String personId = "person123";

        doThrow(new NodeNotFoundException("User not found with ID: " + userId))
                .when(familyService).addFatherToFamily(userId, treeId, familyId, personId);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () ->
                familyFatherManagementService.addFatherToFamily(userId, treeId, familyId, personId));
        assertEquals("User not found with ID: user123", exception.getMessage());
        verify(familyService, times(1)).addFatherToFamily(userId, treeId, familyId, personId);
    }
}
